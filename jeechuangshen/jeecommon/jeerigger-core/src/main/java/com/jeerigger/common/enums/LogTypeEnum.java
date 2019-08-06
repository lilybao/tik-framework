package com.jeerigger.common.enums;

/**
 * 日志类型字典
 */
public enum LogTypeEnum {
    LOGIN("用户登录日志"),
    EXIT("用户退出日志"),
    EXCEPTION("异常日志"),
    SYSTEM("系统管理操作日志"),
    BUSSINESS("业务操作日志");
    /**
     * 日志类型描述
     */
    private String logType;

    LogTypeEnum(String logType) {
        this.logType = logType;
    }

    public String getLogType() {
        return logType;
    }
}
