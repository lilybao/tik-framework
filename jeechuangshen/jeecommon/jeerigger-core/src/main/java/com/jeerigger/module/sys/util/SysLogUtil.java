/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeerigger.module.sys.util;

import com.jeerigger.common.annotation.Log;
import com.jeerigger.common.user.BaseUser;
import com.jeerigger.frame.enums.FlagEnum;
import com.jeerigger.frame.support.util.UserAgentUtil;
import com.jeerigger.frame.util.StringUtil;
import com.jeerigger.module.sys.entity.SysLog;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 日志工具类
 *
 * @author ThinkGem
 * @version 2017-11-7
 */
public class SysLogUtil {

    /**
     * 保存日志
     */
    public static void saveLog(BaseUser userData, HttpServletRequest request, String params, Log log) {
        saveLog(userData, request, null, params, log, 0);
    }

    /**
     * 保存日志
     *
     * @param executeTime
     */

    public static void saveLog(BaseUser userData, HttpServletRequest request, Throwable throwable, String params, Log log, long executeTime) {
        if (userData == null || StringUtil.isEmpty(userData.getUserUuid()) || request == null) {
            return;
        }
        SysLog sysLog = new SysLog();
        sysLog.setLogType(log.logType().getLogType());
        sysLog.setLogTitle(log.logTitle());
        sysLog.setRemoteAddr(UserAgentUtil.getUserIp(request));
        sysLog.setRequestUri(request.getRequestURL().toString());
        sysLog.setRequestMethod(request.getMethod());

        if (StringUtil.isNotEmpty(params)) {
            sysLog.setRequestParams(params);
        }
        UserAgent userAgent = UserAgentUtil.getUserAgent(request);
        sysLog.setExceptionFlag(FlagEnum.NO.getCode());
        sysLog.setDeviceName(userAgent.getOperatingSystem().getName());
        sysLog.setBrowserName(userAgent.getBrowser().getName());
        sysLog.setExceptionFlag(FlagEnum.NO.getCode());
        sysLog.setUserUuid(userData.getUserUuid());
        sysLog.setCreateUser(userData.getUserUuid());
        sysLog.setUserName(userData.getUserName());
        sysLog.setExecuteTime(new BigDecimal(executeTime));

        sysLog.setExceptionFlag(throwable != null ? FlagEnum.YES.getCode() : FlagEnum.NO.getCode());
        String exceStr = StringUtil.getExceptionAsString(throwable);
        if (StringUtil.isNotEmpty(exceStr)) {
            sysLog.setExceptionInfo(exceStr);
        }
        // 如果无地址并无异常日志，则不保存信息
        if (StringUtil.isEmpty(sysLog.getRequestUri()) && StringUtil.isEmpty(sysLog.getExceptionInfo())) {
            return;
        }

        SaveLogThread saveLogThread = SaveLogThread.getInstance();

        saveLogThread.queue.offer(sysLog);

        boolean alive = saveLogThread.isAlive();
        if (!alive) {
            saveLogThread.run();
        }


    }
}
