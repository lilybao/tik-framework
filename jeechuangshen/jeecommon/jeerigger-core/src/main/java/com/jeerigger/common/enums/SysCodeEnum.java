package com.jeerigger.common.enums;

/**
 * 菜单归属
 */
public enum SysCodeEnum {
    JEE_ADMIN_SYSTEM("jeeadmin", "管理系统"),
    JEE_RIGGER_SYSTEM("jeerigger", "业务系统");
    /**
     * 码值
     */
    private String code;
    /**
     * 描述
     */
    private String desc;

    SysCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDesc(String code) {
        for (SysCodeEnum sysCodeEnum : SysCodeEnum.values()) {
            if (sysCodeEnum.code.equals(code)) {
                return sysCodeEnum.desc;
            }
        }
        return "";
    }

    public String getCode() {
        return code;
    }
}
