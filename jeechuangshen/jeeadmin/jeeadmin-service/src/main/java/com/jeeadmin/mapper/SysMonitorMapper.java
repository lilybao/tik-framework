package com.jeeadmin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeeadmin.vo.monitor.MsaOrgUserVo;

public interface SysMonitorMapper {
    /**
     * 部门人员占比分析 top 5
     * @return
     */
    IPage<MsaOrgUserVo> selectMsaOrgUserPage(IPage<MsaOrgUserVo> page);

    /**
     * 角色人员占比分析 top 5
     * @return
     */
    IPage<MsaOrgUserVo> selectMsaRoleUserPage(IPage<MsaOrgUserVo> page);
}
