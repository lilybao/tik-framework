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
 * 字典数据表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDictData extends BaseModel<SysDictData> {

    private static final long serialVersionUID = 1L;

    /**
     * 上级字典数据uuid
     */
    private String parentUuid;

    /**
     * 字典类型
     */
    @NotNull(message = "字典类型不能为空！")
    private String dictType;

    /**
     * 字典标签
     */
    @NotNull(message = "字典标签不能为空！")
    @Size(max = 30, message = "字典标签长度最大值为30！")
    private String dictLabel;

    /**
     * 字典键值
     */
    @NotNull(message = "字典键值不能为空！")
    @Size(max = 10, message = "字典键值长度最大值为10！")
    private String dictValue;

    /**
     * 字典描述
     */
    @Size(max = 150, message = "字典描述长度最大值为150！")
    private String dictDesc;

    /**
     * 字典状态（0:正常 2:停用）
     */
    @Pattern(regexp = "[02]", message = "字典状态值必须为0或2（0：正常 2：停用）！")
    private String dictStatus;
    /**
     * 显示顺序
     */
    private Integer dictSort;
    /**
     * 是否系统内置（0:否 1:是）
     */
    @Pattern(regexp = "[01]", message = "系统内置标识值必须为0或1（0:否 1:是）！")
    private String sysFlag;
}
