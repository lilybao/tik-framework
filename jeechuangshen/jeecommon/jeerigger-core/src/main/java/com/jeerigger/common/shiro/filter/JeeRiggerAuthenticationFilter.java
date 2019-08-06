package com.jeerigger.common.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.controller.ResultData;
import com.jeerigger.frame.support.util.ServletUtil;
import com.jeerigger.frame.util.StringUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * 自定义过滤器
 */
public class JeeRiggerAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        if (this.isLoginRequest(request, response)) {
            return true;
        } else if (requestURI.equals("/") || requestURI.equals("/index.html")||requestURI.contains("")||requestURI.equals("/modeler.html") ) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
            //设置响应头
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            ResultData resultData = new ResultData();
            String userToken=ServletUtil.getUserToken((HttpServletRequest)request);
            if (subject.getPrincipal() != null && StringUtil.isNotEmpty(userToken)) {
                resultData.setCode(ResultCodeEnum.ERROR_VALIDATE_TOKEN.getCode());
                resultData.setMessage(ResultCodeEnum.ERROR_VALIDATE_TOKEN.getMessage());
            } else {
                resultData.setCode(ResultCodeEnum.ERROR_NO_LOGIN.getCode());
                resultData.setMessage(ResultCodeEnum.ERROR_NO_LOGIN.getMessage());
            }
            //写回给客户端
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JSONObject.toJSONString(resultData));
            //刷新和关闭输出流
            printWriter.flush();
            printWriter.close();
            return false;
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
