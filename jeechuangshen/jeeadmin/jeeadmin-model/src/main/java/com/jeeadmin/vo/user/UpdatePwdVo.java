package com.jeeadmin.vo.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpdatePwdVo {
    /**
     * 老密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String newPassword;
}
