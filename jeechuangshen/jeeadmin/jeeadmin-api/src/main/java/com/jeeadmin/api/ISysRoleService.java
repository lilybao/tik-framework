package com.jeeadmin.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.entity.SysRole;
import com.jeeadmin.entity.SysUser;
import com.jeeadmin.vo.user.QueryUserVo;
import com.jeeadmin.vo.role.AssignMenuVo;
import com.jeeadmin.vo.role.AssignUserVo;
import com.jeerigger.frame.base.service.BaseService;
import com.jeerigger.frame.page.PageHelper;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface ISysRoleService extends BaseService<SysRole> {
    /**
     * 查询系统角色列表
     *
     * @param pageHelper
     * @return
     */
    Page<SysRole> selectPage(PageHelper<SysRole> pageHelper);

    /**
     * 更新角色状态
     *
     * @param roleUuid   角色唯一标识
     * @param roleStatus 状态 0:正常 2:停用
     * @return
     */
    boolean updateStatus(String roleUuid, String roleStatus);

    /**
     * 保存角色
     *
     * @param sysRole
     * @return
     */
    boolean saveSysRole(SysRole sysRole);

    /**
     * 更新角色
     *
     * @param sysRole
     * @return
     */
    boolean updateSysRole(SysRole sysRole);

    /**
     * 删除角色
     *
     * @param roleUuid 角色唯一标识
     * @return
     */
    boolean deleteSysRole(String roleUuid);

    /**
     * 获取用户可分配角色
     * @return
     */
    List<SysRole> selectRoleList();
    /**
     * 获取用户列表
     * @param pageHelper
     * @return
     */
    Page<SysUser> selectUserPage(PageHelper<QueryUserVo> pageHelper);
    /**
     * 角色分配用户
     * @param assignUserVo
     * @return
     */
    boolean assignUser(AssignUserVo assignUserVo);

    /**
     * 角色分配菜单
     * @param assignMenuVo
     * @return
     */
    boolean assignMenu(AssignMenuVo assignMenuVo);

}
