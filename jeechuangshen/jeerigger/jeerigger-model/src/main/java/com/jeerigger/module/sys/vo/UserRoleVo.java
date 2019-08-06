package com.jeerigger.module.sys.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 用户角色（已分配、可分配）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserRoleVo {
    private String roleUuid;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色状态（0:正常 2:停用）
     */
    private String roleStatus;
    /**
     * 是否可选 0:否 1：是
     */
    private String disableFlag="0";
    /**
     * 是否已选中 0:否 1：是
     */
    private String checkedFlag="0";

}
