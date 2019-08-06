package com.jeerigger.common.enums;

/**
 * 用户状态枚举类
 */
public enum UserStatusEnum {
    NORMAL("0", "正常"),
    REMOVE("1", "删除"),
    DISABLE("2", "停用"),
    FREEZE("3", "冻结");
    /**
     * 码值
     */
    private String code;
    /**
     * 描述
     */
    private String desc;

    UserStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }
}
