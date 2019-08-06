package com.jeerigger.module.sys.vo;

import lombok.Data;

@Data
public class UpdateUserPwdVo {
    /**
     * 老密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String newPassword;
}
