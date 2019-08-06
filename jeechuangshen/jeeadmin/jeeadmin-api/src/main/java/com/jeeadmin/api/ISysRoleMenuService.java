package com.jeeadmin.api;

import com.jeeadmin.entity.SysRoleMenu;
import com.jeerigger.frame.base.service.BaseService;

import java.util.List;

/**
 * <p>
 * 角色菜单关系表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-15
 */
public interface ISysRoleMenuService extends BaseService<SysRoleMenu> {
    /**
     * 保存角色菜单
     *
     * @param sysRoleMenuList
     * @return
     */
    boolean saveRoleMenu(List<SysRoleMenu> sysRoleMenuList);

    /**
     * 根据角色UUID删除角色菜单
     *
     * @param roleUuid
     * @return
     */
    boolean deleteRoleMenu(String roleUuid);

    /**
     * 根据角色uuid获取已分配的菜单
     *
     * @param roleUuid
     * @return
     */
    List<SysRoleMenu> detailMenuList(String roleUuid);

}
