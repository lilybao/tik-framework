package com.jeeadmin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jeerigger.frame.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组织机构管理员  角色分配表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_orgadmin_role")
public class SysOrgAdminRole extends BaseModel<SysOrgAdminRole> {

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
