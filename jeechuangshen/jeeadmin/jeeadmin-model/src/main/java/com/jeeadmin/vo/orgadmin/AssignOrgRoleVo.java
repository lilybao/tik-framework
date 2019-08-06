package com.jeeadmin.vo.orgadmin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 组织机构管理员分配可管理的组织机构和角色
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AssignOrgRoleVo {
    /**
     * 用户uuid
     */
    private String userUuid;

    /**
     * 机构管理员  分配可管理的组织机构
     */
    List<String> orgUuidList;

    /**
     * 机构管理员  分配可分配的角色s
     */
    List<String> roleUuidList;
}
