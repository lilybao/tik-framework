package com.jeeadmin.vo.orgadmin;

import com.jeeadmin.entity.SysOrgAdminOrg;
import com.jeeadmin.entity.SysOrgAdminRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 获取组织机构管理员已分配的组织机构管理员和角色
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryOrgRoleVo {
    /**
     * 用户uuid
     */
    private String userUuid;
    /**
     * 组织机构管理员已分配角色
     */
    private List<SysOrgAdminRole>  OrgAdminRoleList;
    /**
     * 组织机构管理员已分配组织机构
     */
    private List<SysOrgAdminOrg>  OrgAdminOrgList;
}
