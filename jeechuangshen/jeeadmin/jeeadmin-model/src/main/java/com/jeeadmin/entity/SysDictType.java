package com.jeeadmin.entity;

import com.jeerigger.frame.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDictType extends BaseModel<SysDictType> {

    private static final long serialVersionUID = 1L;
    /**
     * 字典类型
     */
    @NotNull(message = "字典类型不能为空！")
    @Size(max = 30, message = "字典类型长度最大值为30！")
    private String dictType;

    /**
     * 字典名称
     */
    @NotNull(message = "字典名称不能为空！")
    @Size(max = 30, message = "字典名称长度最大值为30！")
    private String dictName;

    /**
     * 状态（0：正常 2：停用）
     */
    @Pattern(regexp = "[02]", message = "字典状态值必须为0或1或2（0：正常 2：停用）！")
    private String dictStatus;

    /**
     * 系统字典标识(0:否 1:是)
     */
    @Pattern(regexp = "[01]", message = "系统字典标识值必须为0或1（0:否 1:是）！")
    private String sysFlag;
    /**
     * 备注信息
     */
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remarks;


}
