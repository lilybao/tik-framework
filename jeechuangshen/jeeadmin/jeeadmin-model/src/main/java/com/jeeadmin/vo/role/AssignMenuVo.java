package com.jeeadmin.vo.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AssignMenuVo {
    /**
     * 角色uuid
     */
    private String roleUuid;
    /**
     * 角色添加用户
     */
    List<String> menuUuidList;
}
