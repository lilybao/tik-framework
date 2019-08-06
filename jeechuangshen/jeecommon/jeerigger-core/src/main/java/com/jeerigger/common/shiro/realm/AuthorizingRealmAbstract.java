package com.jeerigger.common.shiro.realm;

import com.jeerigger.common.user.BaseUser;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.exception.BaseException;
import com.jeerigger.frame.exception.FrameException;
import com.jeerigger.frame.support.util.ServletUtil;
import com.jeerigger.frame.support.util.UserAgentUtil;
import com.jeerigger.frame.util.StringUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户认证基类
 */
public abstract class AuthorizingRealmAbstract extends AuthorizingRealm {

    /**
     * 获取用户信息 并验证密码
     *
     * @param loginName
     * @param password
     * @return
     */
    protected abstract BaseUser getUserData(String loginName, String password);

    /**
     * 获取用户权限
     *
     * @param userData
     * @return
     */
    protected abstract List<String> getUserPermission(BaseUser userData);

    /**
     * 获取用户角色列表
     *
     * @param userData
     * @return
     */
    protected abstract List<String> getUserRole(BaseUser userData);


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        String loginName = (String) authenticationToken.getPrincipal();// 获取用户名
        char[] credentials = (char[]) authenticationToken.getCredentials();// 字符类型密码获取(用户输入的密码)
        String password = new String(credentials);// 把字符数组转换为String类型(用户输入的密码)
        try {
            BaseUser userData = getUserData(loginName, password);
            if (userData != null && StringUtil.isNotEmpty(userData.getUserUuid())) {
                HttpServletRequest httpServletRequest = ServletUtil.getHttpServletRequest();
                if (httpServletRequest != null) {
                    userData.setDeviceType(UserAgentUtil.getDeviceType(httpServletRequest));
                }
                AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userData, password, userData.getUserName());
                return authenticationInfo;
            } else {
                throw new FrameException(ResultCodeEnum.ERROR_LOGIN_EXCEPTION);
            }
        } catch (BaseException ex) {
            throw ex;
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            throw new FrameException(ResultCodeEnum.ERROR_LOGIN_EXCEPTION, ex);
        }
    }

    /**
     * 用户授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        BaseUser userData = (BaseUser) principalCollection.getPrimaryPrincipal();
        if (userData == null || StringUtil.isEmpty(userData.getUserUuid())) {
            throw new FrameException(ResultCodeEnum.ERROR_VALIDATE_TOKEN);
        }
        SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
        List<String> userPermission = getUserPermission(userData);
        if (userPermission != null && userPermission.size() > 0) {
            authenticationInfo.addStringPermissions(userPermission);
        }
        List<String> userRole = getUserRole(userData);
        if (userRole != null && userRole.size() > 0) {
            authenticationInfo.addRoles(userRole);
        }
        return authenticationInfo;
    }

}
