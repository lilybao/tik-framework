package com.jeerigger.module.sys.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jeerigger.frame.page.PageHelper;
import com.jeerigger.module.sys.entity.SysMenu;
import com.jeerigger.module.sys.entity.SysOrg;
import com.jeerigger.module.sys.entity.SysUser;
import com.jeerigger.module.sys.vo.AssignUserRoleVo;
import com.jeerigger.module.sys.vo.UpdateUserInfoVo;
import com.jeerigger.module.sys.vo.UpdateUserPwdVo;
import com.jeerigger.module.sys.vo.UserRoleVo;


import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 根据登录名获取用户信息
     *
     * @param loginName
     * @return
     */
    SysUser getUserByLoginName(String loginName);

    /**
     * 获取用户信息
     *
     * @param pageHelper
     * @return
     */
    Page<SysUser> selectPage(PageHelper<SysUser> pageHelper);

    /**
     * 根据用户唯一标识获取用户信息
     *
     * @param userUuid
     * @return
     */
    SysUser detailUserInfo(String userUuid);

    /**
     * 新增用户信息
     *
     * @param sysUser
     * @return
     */
    boolean saveUser(SysUser sysUser);

    /**
     * 更新用户信息
     *
     * @param sysUser
     * @return
     */
    boolean updateUser(SysUser sysUser);

    /**
     * 更新个人信息
     *
     * @param updateUserInfoVo
     * @return
     */
    boolean updateUserInfo(UpdateUserInfoVo updateUserInfoVo);

    /**
     * 删除用户
     *
     * @param userUuid
     * @return
     */
    boolean deleteUser(String userUuid);

    /**
     * 更新用户状态
     *
     * @param sysUser
     * @return
     */
    boolean updateUserStatus(SysUser sysUser);

    /**
     * 重置用户密码
     *
     * @param userUuid
     * @return
     */
    boolean resetUserPassword(String userUuid);

    /**
     * 登录用户修改密码
     *
     * @param changePwdVo
     * @return
     */
    boolean changePassword(UpdateUserPwdVo changePwdVo);


    /**
     * 获取组织机构管理员机构列表
     *
     * @return
     */
    List<SysOrg> selectOrgAdminOrgList();

    /**
     * 获取用户已分配和未分配角色列表
     *
     * @return
     */
    List<UserRoleVo> selectUserRoleList(String userUuid);

    /**
     * 用户分配角色
     *
     * @param assignRoleVo
     * @return
     */
    boolean assignRole(AssignUserRoleVo assignRoleVo);

    /**
     * 根据用户ID获取用户菜单列表
     *
     * @param userUuid
     * @return
     */
    List<SysMenu> getUserMenu(String userUuid);
}
