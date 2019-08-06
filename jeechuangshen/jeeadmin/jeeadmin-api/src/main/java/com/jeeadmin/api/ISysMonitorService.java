package com.jeeadmin.api;

import com.jeeadmin.vo.monitor.MsaOrgUserVo;
import com.jeeadmin.vo.monitor.MsaRoleUserVo;

import java.util.List;
import java.util.Map;

/**
 * 系统监控
 */
public interface ISysMonitorService {
    /**
     * 获取部门人员占比分析
     *
     * @return
     */
    List<MsaOrgUserVo> selectMsaOrgUser();

    /**
     * 获取角色人员占比分析
     *
     * @return
     */
    List<MsaRoleUserVo> selectMsaRoleUser();

    /**
     * 获取JVM信息
     *
     * @return
     */
    Map<String, String> getJvmInfo();

    /**
     * 获取系统信息
     *
     * @return
     */
    Map<String, String> getSystemInfo();

    /**
     * 获取内存信息
     *
     * @return
     */
    Map<String, String> getMemInfo();

    /**
     * 获取磁盘信息
     *
     * @return
     */
    List<Map<String, String>> getDiskListInfo();

    /**
     * 获取CPU信息
     *
     * @return
     */
    Map<String, String> getCpuInfo();

}
