package com.jeerigger.frame.support.resolver;


import com.alibaba.fastjson.JSONObject;
import com.jeerigger.frame.exception.FrameException;
import com.jeerigger.frame.support.resolver.annotation.CustomRequestBody;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;

public class CustomRequestBodyResolver implements HandlerMethodArgumentResolver {

    private static final String JSONBODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(CustomRequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        if (servletRequest.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            return bindRequestParameters(methodParameter, nativeWebRequest);
        } else {
            return null;
        }

    }

    private Object bindRequestParameters(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) throws Exception {
        CustomRequestBody customRequestBody = methodParameter.getParameterAnnotation(CustomRequestBody.class);
        String name = (customRequestBody == null || !StringUtils.hasText(customRequestBody.value()) ? methodParameter.getParameterName() : customRequestBody.value());
        Object resultObj = null;
        String requestBody = getRequestBody(nativeWebRequest);
        Class<?> parameterType = methodParameter.getParameterType();
        if (BeanUtils.isSimpleProperty(parameterType)) {
            try {
                JSONObject json = JSONObject.parseObject(requestBody);
                resultObj = json.get(name);
                if (resultObj == null) {
                    String defaultvalue = customRequestBody.defaultValue();
                    defaultvalue = defaultvalue.replaceAll("\r|\n|\\s*", "");
                    resultObj = ConvertUtils.convert(defaultvalue, parameterType);
                }
            } catch (Exception e) {
                throw new FrameException("参数("+name+")获取失败");
            }
        } else {
            try {
                resultObj = JSONObject.parseObject(requestBody, parameterType);
            } catch (Exception e) {
                throw new FrameException("参数("+name+")获取失败");
            }
        }

        if(customRequestBody.required()){
            if(resultObj==null){
                throw new IllegalAccessException(String.format("required parm %s is not present",name));
            }
        }

        return resultObj;
    }

    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        // 有就直接获取
        String jsonBody = (String) webRequest.getAttribute(JSONBODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
        // 没有就从请求中读取
        if (jsonBody == null) {
            try {
                jsonBody = IOUtils.toString(servletRequest.getReader());
                webRequest.setAttribute(JSONBODY_ATTRIBUTE, jsonBody, NativeWebRequest.SCOPE_REQUEST);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBody;
    }
}
