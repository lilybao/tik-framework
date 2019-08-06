package com.jeeadmin.vo.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryUserVo {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 联系电话
     */
    private String userPhone;

    /**
     * 联系手机
     */
    private String userMobile;

    /**
     * 联系地址
     */
    private String userAddress;

    /**
     * 邮箱地址
     */
    private String userEmail;

    /**
     * 组织机构UUID
     */
    private String orgUuid;

    /**
     * 用户状态
     */
    private String userStatus;
}
