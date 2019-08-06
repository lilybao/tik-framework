package com.jeerigger.module.sys.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeerigger.frame.support.util.SpringUtil;
import com.jeerigger.module.sys.entity.SysParam;
import com.jeerigger.module.sys.mapper.ParamMapper;

public class SysParamUtil {
    private static final String SYS_PARAM_LIST = "sys_param_list";
    /**
     * 系统内置字典数据 重置密码的Key
     */
    private static final String SYS_USER_PASSWORD = "sys.user.password";


    private static ParamMapper getParamMapper() {
        return SpringUtil.getBean(ParamMapper.class);
    }

    /**
     * 获取参数配置
     *
     * @param paramKey
     * @return
     */
    public static SysParam getSysParam(String paramKey) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("param_key", paramKey);
        return getParamMapper().selectOne(queryWrapper);
    }

    /**
     * 获取系统设置初始密码
     *
     * @return
     */
    public static String getInitPassword() {
        SysParam sysParam = getSysParam(SYS_USER_PASSWORD);
        if (sysParam != null) {
            return sysParam.getParamValue();
        } else {
            return "";
        }
    }

    /**
     * 根据参数key值获取value
     *
     * @param paramKey
     * @return
     */
    public static String getParamValue(String paramKey) {
        SysParam sysParam = getSysParam(paramKey);
        if (sysParam != null) {
            return sysParam.getParamValue();
        } else {
            return "";
        }
    }
}
