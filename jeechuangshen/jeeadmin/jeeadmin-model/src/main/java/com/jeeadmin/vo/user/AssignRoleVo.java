package com.jeeadmin.vo.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户分配角色VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AssignRoleVo {
    /**
     * 用户uuid
     */
    private String userUuid;

    /**
     * 用户分配角色
     */
    private List<String> roleUuidList;
}
