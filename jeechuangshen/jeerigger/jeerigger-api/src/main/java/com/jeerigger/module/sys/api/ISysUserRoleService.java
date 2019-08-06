package com.jeerigger.module.sys.api;

import com.jeerigger.frame.base.service.BaseService;
import com.jeerigger.module.sys.entity.SysUserRole;

import java.util.List;

/**
 * <p>
 * 人员角色关系表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-15
 */
public interface ISysUserRoleService extends BaseService<SysUserRole> {
    /**
     * 保存用户角色关系表
     *
     * @param sysUserRoleList
     * @return
     */
    boolean saveUserRole(List<SysUserRole> sysUserRoleList);

    /**
     * 根据用户ID删除用户角色关系表
     *
     * @param userUuid
     * @return
     */
    boolean deleteUserRole(String userUuid);

}
