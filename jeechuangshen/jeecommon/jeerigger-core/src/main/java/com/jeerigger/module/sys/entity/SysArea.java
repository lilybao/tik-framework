package com.jeerigger.module.sys.entity;

import com.jeerigger.frame.base.model.BaseTreeModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 行政区划表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysArea extends BaseTreeModel<SysArea> {

    private static final long serialVersionUID = 1L;

    /**
     * 行政区划编码
     */
    private String areaCode;

    /**
     * 行政区划名称
     */
    private String areaName;

    /**
     * 行政区划简称
     */
    private String areaShortName;

    /**
     * 行政区划类型
     */
    private String areaType;

    /**
     * 使用状态（0:正常  2:停用）
     */
    private String areaStatus;

    /**
     * 显示顺序
     */
    private Integer areaSort;

}
