package com.jeerigger.module.sys.entity;

import com.jeerigger.frame.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysLog extends BaseModel<SysLog> {
    /**
     * 用户uuid
     */
    private String userUuid;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 日志标题
     */
    private String logTitle;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 操作方式
     */
    private String requestMethod;

    /**
     * 操作提交的数据
     */
    private String requestParams;

    /**
     * 操作IP地址
     */
    private String remoteAddr;

    /**
     * 是否异常
     */
    private String exceptionFlag;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 设备名称/操作系统
     */
    private String deviceName;

    /**
     * 浏览器名称
     */
    private String browserName;

    /**
     * 执行时间(毫秒)
     */
    private BigDecimal executeTime;

}
