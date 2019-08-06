package com.jeerigger.common.enums;

/**
 * 状态枚举
 */
public enum StatusEnum {
    NORMAL("0", "正常"),
    REMOVE("1", "删除"),
    DISABLE("2", "停用");
    /**
     * 码值
     */
    private String code;
    /**
     * 描述
     */
    private String desc;

    StatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(String code) {
        for (StatusEnum statusEnum : StatusEnum.values()) {
            if (statusEnum.code.equals(code)) {
                return statusEnum.desc;
            }
        }
        return "";
    }
    public String getCode() {
        return code;
    }
}
