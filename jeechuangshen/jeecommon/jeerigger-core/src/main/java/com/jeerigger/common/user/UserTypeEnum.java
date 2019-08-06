package com.jeerigger.common.user;


public enum UserTypeEnum {
    SUPER_ADMIN_USER("0", "超级管理员"),
    SYSTEM_ADMIN_USER("1", "系统管理员"),
    ORG_ADMIN_USER("2", "机构管理员"),
    NORMAL_USER("3", "普通用户");
    /**
     * 码值
     */
    private String code;
    /**
     * 描述
     */
    private String desc;

    UserTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return this.code;
    }
    public String getDesc() {
        return this.desc;
    }
}
