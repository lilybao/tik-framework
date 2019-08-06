package com.jeeadmin.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeeadmin.entity.SysAdminUser;
import com.jeeadmin.entity.SysMenu;
import com.jeeadmin.vo.user.UpdatePwdVo;
import com.jeeadmin.vo.user.UpdateUserVo;
import com.jeerigger.frame.base.service.BaseService;
import com.jeerigger.frame.page.PageHelper;

import java.util.List;

/**
 * <p>
 * 系统管理员信息表 服务类
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface ISysAdminUserService extends BaseService<SysAdminUser> {
    /**
     * 根据登录名获取登录用户信息
     *
     * @param loginName
     * @return
     */
    SysAdminUser getAdminUserByLoginName(String loginName);

    /**
     * 查询系统管理员列表
     *
     * @param pageHelper 查询条件
     * @return
     */
    Page<SysAdminUser> selectPage(PageHelper<SysAdminUser> pageHelper);

    /**
     * 新增系统管理员数据
     *
     * @param sysAdminUser
     * @return
     */
    boolean saveAdminUser(SysAdminUser sysAdminUser);

    /**
     * 更新系统管理员信息
     *
     * @param sysAdminUser
     * @return
     */
    boolean updateAdminUser(SysAdminUser sysAdminUser);

    /**
     * 更新系统管理员信息
     *
     * @param sysAdminUser
     * @return
     */
    boolean updateAdminUserStatus(SysAdminUser sysAdminUser);

    /**
     * 管理员重置密码
     *
     * @param userUuid
     * @return
     */
    boolean resetPwd(String userUuid);

    /**
     * 登录用户修改密码
     *
     * @param updatePwdVo
     * @return
     */
    boolean changePassword(UpdatePwdVo updatePwdVo);

    /**
     * 登录用户修改个人信息
     *
     * @param updateUserVo
     * @return
     */
    boolean updateUserInfo(UpdateUserVo updateUserVo);

    /**
     * 获取系统管理员菜单
     *
     * @return
     */
    List<SysMenu> getSysAdminMenu();
}
