package com.jeerigger.module.sys.vo;

import lombok.Data;

/**
 * 用户登录提交信息
 */
@Data
public class LoginUser {
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 登录密码
     */
    private String loginPwd;
    /**
     * 验证码
     */
    private String kaptchaCode;
    /**
     * 记住密码
     */
    private boolean rememberMe;
}
