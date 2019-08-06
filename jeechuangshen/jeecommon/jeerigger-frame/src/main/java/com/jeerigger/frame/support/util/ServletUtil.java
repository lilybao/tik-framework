package com.jeerigger.frame.support.util;

import com.jeerigger.frame.util.StringUtil;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet工具类
 */
public class ServletUtil {
    private static final String USER_TOKEN = "token";
    /**
     * 获取当前HttpServletRequest
     *
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        HttpServletRequest httpServletRequest = null;
        try {
            ServletRequestAttributes servletRequestAttributes = getServletRequestAttributes();
            if (servletRequestAttributes != null) {
                httpServletRequest = servletRequestAttributes.getRequest();
            }
            return httpServletRequest;
        } catch (Exception ex) {
            httpServletRequest = null;
        }
        return httpServletRequest;
    }

    /**
     * 获取当前HttpServletRequest
     *
     * @return
     */
    public static HttpServletResponse getHttpServletResponse() {
        HttpServletResponse httpServletResponse = null;
        try {
            ServletRequestAttributes servletRequestAttributes = getServletRequestAttributes();
            if (servletRequestAttributes != null) {
                httpServletResponse = servletRequestAttributes.getResponse();
            }
            return httpServletResponse;
        } catch (Exception ex) {
            httpServletResponse = null;
        }
        return httpServletResponse;
    }

    public static ServletRequestAttributes getServletRequestAttributes() {
        try {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return servletRequestAttributes;
        } catch (Exception ex) {
            return null;
        }
    }


    public static String getUserToken(HttpServletRequest httpServletRequest){
        if(httpServletRequest==null){
            httpServletRequest=getHttpServletRequest();
        }
        if(httpServletRequest==null){
            return null;
        }
        // 从请求头中获取token
        String userToken = WebUtils.toHttp(httpServletRequest).getHeader(USER_TOKEN);
        if (StringUtil.isEmpty(userToken)) {
            //请求参数中的Token
            userToken = httpServletRequest.getParameter(USER_TOKEN);
        }

        return userToken;
    }

}
