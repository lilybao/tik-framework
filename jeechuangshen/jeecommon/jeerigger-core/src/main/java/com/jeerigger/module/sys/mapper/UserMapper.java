package com.jeerigger.module.sys.mapper;

import com.jeerigger.datasource.annotation.DataSource;
import com.jeerigger.module.sys.entity.SysAdminUser;
import com.jeerigger.module.sys.entity.SysUser;
import com.jeerigger.module.sys.entity.UserMenu;
import com.jeerigger.module.sys.entity.UserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DataSource
public interface UserMapper {
    /**
     * 获取系统管理员信息
     *
     * @param loginName
     * @return
     */
    @Select("select * from sys_admin_user where login_name=#{loginName}")
    SysAdminUser getSysAdminUser(@Param("loginName") String loginName);

    /**
     * 获取用户信息
     *
     * @param loginName
     * @return
     */
    @Select("select * from sys_user where login_name=#{loginName}")
    SysUser getSysUser(@Param("loginName") String loginName);


    /**
     * 获取系统管理员菜单
     *
     * @param role_code
     * @return
     */
    @Select("select distinct m.* " +
            "from sys_role sr,sys_role_menu rm,sys_menu m " +
            "where sr.uuid = rm.role_uuid  " +
            "and rm.menu_uuid = m.uuid " +
            "and sr.role_code = #{role_code} " +
            "and m.sys_code = 'jeeadmin' " +
            "order by m.parent_uuid,m.menu_sort asc")
    List<UserMenu> getAdminUserMenu(@Param("role_code") String role_code);

    /**
     * 获取超级管理员菜单
     *
     * @return
     */
    @Select("select m.* from sys_menu m " +
            "where m.sys_code ='jeeadmin' " +
            "order by m.parent_uuid,m.menu_sort asc")
    List<UserMenu> getSuperAdminMenu();


    /**
     * 获取用户菜单
     *
     * @return
     */
    @Select("<script> " +
            "select distinct m.* " +
            "from sys_user u,sys_user_role ur, sys_role_menu rm,sys_menu m " +
            "where u.uuid=ur.user_uuid " +
            "and ur.role_uuid=rm.role_uuid " +
            "and rm.menu_uuid=m.uuid " +
            "and u.uuid =  #{userUuid} " +
            "and m.sys_code in " +
            "<foreach collection=\"sysCodeList\" index=\"index\" item=\"sysCode\" open=\"(\" separator=\",\" close=\")\">" +
            " #{sysCode} " +
            "</foreach> " +
            "order by m.uuid,m.menu_sort asc " +
            "</script>")
    List<UserMenu> getUserMenu(@Param("userUuid") String userUuid, @Param("sysCodeList") List<String> sysCodeList);


    /**
     * 获取用户角色
     *
     * @param userUuid
     * @return
     */
    @Select("select distinct r.uuid " +
            "from sys_user u,sys_user_role ur,sys_role r " +
            "where u.uuid = ur.user_uuid " +
            "and ur.role_uuid = r.uuid " +
            "and u.uuid =  #{userUuid} ")
    List<UserRole> getUserRole(@Param("userUuid") String userUuid);
}
