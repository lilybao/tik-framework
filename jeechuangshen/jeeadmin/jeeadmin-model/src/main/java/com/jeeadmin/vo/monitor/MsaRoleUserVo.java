package com.jeeadmin.vo.monitor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色人员使用占比分析
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MsaRoleUserVo {
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 对应数量
     */
    private String total;
}
