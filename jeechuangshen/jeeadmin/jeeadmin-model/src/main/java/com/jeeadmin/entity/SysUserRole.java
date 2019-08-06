package com.jeeadmin.entity;

import com.jeerigger.frame.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 人员角色关系表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUserRole extends BaseModel<SysUserRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户uuid
     */
    private String userUuid;
    /**
     * 角色uuid
     */
    private String roleUuid;

}
