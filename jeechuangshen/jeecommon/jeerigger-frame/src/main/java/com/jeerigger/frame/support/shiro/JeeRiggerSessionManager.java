package com.jeerigger.frame.support.shiro;

import com.jeerigger.frame.support.util.ServletUtil;
import com.jeerigger.frame.util.StringUtil;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class JeeRiggerSessionManager extends DefaultWebSessionManager {
    /**
     * 获取token
     * 前后端分离将从请求头中获取token
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // 从请求头中获取token
        String userToken = ServletUtil.getUserToken((HttpServletRequest)request);
        // 判断是否有值
        if (StringUtil.isNotEmpty(userToken)) {
            // 设置当前session状态
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "url");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, userToken);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return userToken;
        }
        // 若header获取不到token则尝试从cookie中获取
        return super.getSessionId(request, response);
    }
}
