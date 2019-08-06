package com.jeerigger.frame.enums;

/**
 * 是否枚举类
 */
public enum FlagEnum {
    NO("0", "否"),
    YES("1", "是");
    /**
     * 码值
     */
    private String code;
    /**
     * 描述
     */
    private String desc;

    FlagEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(String code) {
        for (FlagEnum flagEnum : FlagEnum.values()) {
            if (flagEnum.code.equals(code)) {
                return flagEnum.desc;
            }
        }
        return "";
    }

    public String getCode() {
        return code;
    }

}
