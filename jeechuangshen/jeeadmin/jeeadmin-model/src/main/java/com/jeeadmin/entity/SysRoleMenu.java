package com.jeeadmin.entity;

import com.jeerigger.frame.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色菜单关系表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRoleMenu extends BaseModel<SysRoleMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色uuid
     */
    private String roleUuid;

    /**
     * 菜单uuid
     */
    private String menuUuid;

}
