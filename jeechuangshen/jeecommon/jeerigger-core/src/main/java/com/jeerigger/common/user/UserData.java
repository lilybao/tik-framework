package com.jeerigger.common.user;

import lombok.Data;

@Data
public class UserData extends BaseUser {
    /**
     * 用户编号
     */
    private String userNumber;

    /**
     * 用户性别
     */
    private String userSex;

    /**
     * 身份证件类型
     */
    private String identityType;

    /**
     * 身份证件号码
     */
    private String identityCode;

    /**
     * 人员职务
     */
    private String userDuty;

    /**
     * 人员职级
     */
    private String userRank;

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
     * 政治面貌
     */
    private String politicsStatus;

    /**
     * 用户状态（0正常 2停用 3冻结）
     */
    private String userStatus;

}
