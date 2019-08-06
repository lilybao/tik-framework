package com.jeeadmin.api;

import com.jeeadmin.entity.SysOrgAdminOrg;
import com.jeerigger.frame.base.service.BaseService;

import java.util.List;

/**
 * <p>
 * 组织机构管理员  组织机构分配表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-15
 */
public interface ISysOrgAdminOrgService extends BaseService<SysOrgAdminOrg> {
    /**
     * 保存机构管理员分配的可管理机构
     *
     * @param sysOrgAdminOrgList
     * @return
     */
    boolean saveOrgAdminOrg(List<SysOrgAdminOrg> sysOrgAdminOrgList);

    /**
     * 删除机构管理员已分配的可管理机构
     *
     * @param userUuid
     * @return
     */
    boolean deleteOrgAdminOrg(String userUuid);

    /**
     * 查看机构管理员已分配的可管理的组织机构
     *
     * @param userUuid
     * @return
     */
    List<SysOrgAdminOrg> detailOrgList(String userUuid);


}
