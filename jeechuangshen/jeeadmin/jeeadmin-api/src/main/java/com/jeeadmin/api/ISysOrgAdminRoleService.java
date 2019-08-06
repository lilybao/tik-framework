package com.jeeadmin.api;

import com.jeeadmin.entity.SysOrgAdminRole;
import com.jeerigger.frame.base.service.BaseService;

import java.util.List;

/**
 * <p>
 * 机构管理员  角色分配表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-15
 */
public interface ISysOrgAdminRoleService extends BaseService<SysOrgAdminRole> {

    /**
     * 保存机构管理员 可分配的角色
     *
     * @param sysOrgAdminRoleList
     * @return
     */
    boolean saveOrgAdminRole(List<SysOrgAdminRole> sysOrgAdminRoleList);

    /**
     * 删除机构管理员已分配 可分配的角色
     *
     * @param userUuid
     * @return
     */
    boolean deleteOrgAdminRole(String userUuid);

    /**
     * 查看组织机构管理员已分配的可分配的角色
     *
     * @param userUuid
     * @return
     */
    List<SysOrgAdminRole> detailRoleList(String userUuid);


}
