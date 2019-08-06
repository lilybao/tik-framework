package com.jeeadmin.api;

import com.jeeadmin.entity.SysUserRole;
import com.jeeadmin.vo.role.CancelUserVo;
import com.jeerigger.frame.base.service.BaseService;

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

    /**
     * 批量取消用户授权
     *
     * @param cancelUserVo
     * @return
     */
    boolean cancelRoleUser(CancelUserVo cancelUserVo);

    /**
     * 查看用户已分配角色
     *
     * @param userUuid
     * @return
     */
    List<SysUserRole> detailRoleList(String userUuid);


}
