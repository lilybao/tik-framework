package com.jeeadmin.vo.monitor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 数据分析Vo类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MsaOrgUserVo {
    /**
     * 部门名称
     */
    private String orgName;
    /**
     * 对应数量
     */
    private String total;
}
