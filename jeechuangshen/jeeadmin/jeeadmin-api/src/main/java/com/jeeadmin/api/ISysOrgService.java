package com.jeeadmin.api;

import com.jeeadmin.entity.SysOrg;
import com.jeerigger.frame.base.service.BaseTreeService;

import java.util.List;

/**
 * <p>
 * 组织机构表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface ISysOrgService extends BaseTreeService<SysOrg> {

    /**
     * 查询下级组织机构列表
     *
     * @param orgUuid
     * @return
     */
    List<SysOrg> selectChildOrg(String orgUuid);

    /**
     * 查询组织机构列表
     *
     * @param sysOrg
     * @return
     */
    List<SysOrg> selectOrgList(SysOrg sysOrg);

    /**
     * 查看组织机构详细信息
     *
     * @param orgUuid
     * @return
     */
    SysOrg detailOrg(String orgUuid);

    /**
     * 新增组织机构
     *
     * @param sysOrg
     * @return
     */
    boolean saveSysOrg(SysOrg sysOrg);

    /**
     * 更新组织机构信息
     *
     * @param sysOrg
     * @return
     */
    boolean updateSysOrg(SysOrg sysOrg);

    /**
     * 修改组织机构状态
     *
     * @param sysOrg
     * @return
     */
    boolean updateStatus(SysOrg sysOrg);

    /**
     * 删除组织机构
     *
     * @param orgUuid
     * @return
     */
    boolean deleteSysOrg(String orgUuid);

}
