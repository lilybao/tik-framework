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
 * 系统参数配置表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysParam extends BaseModel<SysParam> {

    private static final long serialVersionUID = 1L;

    /**
     * 参数名称
     */
    @NotNull(message = "参数名称不能为空！")
    @Size(max = 100,message = "参数名称长度最大值为100！")
    private String paramName;

    /**
     * 参数键名
     */
    @NotNull(message = "参数键名不能为空！")
    @Size(max = 20,message = "参数键名长度最大值为20！")
    private String paramKey;

    /**
     * 参数键值
     */
    @NotNull(message = "参数键值不能为空！")
    @Size(max = 200,message = "参数键值长度最大值为200！")
    private String paramValue;

    /**
     * 系统参数标识(0:否 1:是)
     */
    @Pattern(regexp = "[01]",message = "系统内置标识值必须为0或1（0:否 1:是）！")
    private String sysFlag;

    /**
     * 备注信息
     */
    @Size(max = 150,message = "备注信息长度最大值为150！")
    private String remarks;

}
