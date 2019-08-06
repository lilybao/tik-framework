package com.jeeadmin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jeerigger.frame.base.model.BaseTreeModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    @NotNull(message = "行政区划编码不能为空！")
    @Size(max = 10, message = "行政区划编码最大值为10！")
    private String areaCode;

    /**
     * 行政区划名称
     */
    @NotNull(message = "行政区划名称不能为空！")
    @Size(max = 150, message = "行政区划名称长度最大值为150！")
    private String areaName;

    /**
     * 行政区划简称
     */
    @NotNull(message = "行政区划简称不能为空！")
    @Size(max = 150, message = "行政区划简称长度最大值为150！")
    private String areaShortName;

    /**
     * 行政区划类型
     */
    @NotNull(message = "行政区划类型不能为空！")
    private String areaType;

    /**
     * 行政区划类型名称
     */
    @TableField(exist = false)
    private String areaTypeName;

    /**
     * 使用状态（0:正常  2:停用）
     */
    @Pattern(regexp = "[02]", message = "状态值必须为0或2（0：正常 2：停用）！")
    private String areaStatus;

    /**
     * 显示顺序
     */
    private Integer areaSort;
    /**
     * 备注信息
     */
    String remarks;


}
