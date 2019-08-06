package com.jeeadmin.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.entity.SysUser;
import com.jeeadmin.vo.user.AssignRoleVo;
import com.jeeadmin.vo.user.QueryUserVo;
import com.jeerigger.frame.base.service.BaseService;
import com.jeerigger.frame.page.PageHelper;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface ISysUserService extends BaseService<SysUser> {
    /**
     * 获取用户信息
     * @param pageHelper
     * @return
     */
    Page<SysUser> selectPage(PageHelper<QueryUserVo> pageHelper);

    /**
     * 根据用户唯一标识获取用户信息
     * @param userUuid
     * @return
     */
    SysUser getUserByUuid(String userUuid);
    /**
     * 查看角色已分配用户
     * @param roleUuid
     * @return
     */
    List<SysUser> detailUserList(String roleUuid);
    /**
     * 新增用户信息
     * @param sysUser
     * @return
     */
    boolean saveUser(SysUser sysUser);

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    boolean updateUser(SysUser sysUser);

    /**
     * 删除用户
     * @param userUuid
     * @return
     */
    boolean deleteUser(String userUuid);

    /**
     * 更新用户状态
     * @param sysUser
     * @return
     */
    boolean updateUserStatus(SysUser sysUser);

    /**
     * 重置用户密码
     * @param userUuid
     * @return
     */
    boolean resetUserPwd(String userUuid);

    /**
     * 用户分配角色
     * @param assignRoleVo
     * @return
     */
    boolean assignRole(AssignRoleVo assignRoleVo);


}
