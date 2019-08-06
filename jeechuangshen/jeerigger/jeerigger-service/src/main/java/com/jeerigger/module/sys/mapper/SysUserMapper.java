package com.jeerigger.module.sys.mapper;

import com.jeerigger.datasource.annotation.DataSource;
import com.jeerigger.frame.base.mapper.BaseMapper;
import com.jeerigger.module.sys.entity.SysUser;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@DataSource
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户UUID获取已分配角色UUID
     *
     * @param userUuid
     * @return
     */
    List<String> getUserRoleUuid(String userUuid);

    /**
     * 根据组织机构管理员用户UUID获取 组织机构管理员分配的角色UUID
     *
     * @param userUuid
     * @return
     */
    List<String> getOrgAdminRoleUuid(String userUuid);

    /**
     * 根据组织机构管理员用户UUID获取 组织机构管理员分配可管理组织机构
     *
     * @param userUuid
     * @return
     */
    List<String> getOrgAdminOrgUuid(String userUuid);

}
