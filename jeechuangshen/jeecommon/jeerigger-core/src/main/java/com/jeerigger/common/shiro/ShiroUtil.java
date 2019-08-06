package com.jeerigger.common.shiro;

import com.jeerigger.common.user.BaseUser;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.exception.FrameException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Shiro工具类
 */
public class ShiroUtil {

    //验证码常量
    private static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取用户信息
     *
     * @param <T>
     * @return
     */
    public static <T extends BaseUser> T getUserData() {
        return (T) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 获取用户UUID
     *
     * @return
     */
    public static String getUserUuid() {
        return getUserData().getUserUuid();
    }

    /**
     * session存放数据
     *
     * @param key
     * @param value
     */
    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * 获取session数据
     *
     * @param key
     * @return
     */
    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    /**
     * 是否已登录
     *
     * @return
     */
    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }

    /**
     * 登出
     */
    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * 获取验证码
     *
     * @return
     * @throws FrameException
     */
    public static String getKaptchaCode() {
        Object kaptchaCode = getSessionAttribute(KAPTCHA_SESSION_KEY);
        if (kaptchaCode == null) {
            throw new FrameException(ResultCodeEnum.ERROR_KAPTCHA_FAILURE);
        }
        getSession().removeAttribute(KAPTCHA_SESSION_KEY);
        return kaptchaCode.toString();
    }

    /**
     * 存放验证码
     *
     * @param kaptchaCode
     * @throws FrameException
     */
    public static void setKaptchaCode(String kaptchaCode) throws FrameException {
        setSessionAttribute(KAPTCHA_SESSION_KEY, kaptchaCode);
    }
}
