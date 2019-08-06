package com.jeeadmin.mapper;

import com.jeeadmin.entity.SysUser;
import com.jeerigger.frame.base.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
   /**
    * 根据角色UUID获取已分配的用户
    * @param roleUuid
    * @return
    */
   List<SysUser> selectUserListByRoleUuid(String roleUuid);

}
