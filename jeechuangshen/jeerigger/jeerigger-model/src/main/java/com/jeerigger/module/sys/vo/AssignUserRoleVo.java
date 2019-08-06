package com.jeerigger.module.sys.vo;

import lombok.Data;

import java.util.List;

/**
 * 用户分配角色VO
 */
@Data
public class AssignUserRoleVo {
    /**
     * 用户uuid
     */
    private String userUuid;

    /**
     * 用户分配角色
     */
    private List<String> roleUuidList;
}
