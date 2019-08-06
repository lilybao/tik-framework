package com.jeeadmin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jeerigger.frame.base.model.BaseTreeModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * <p>
 * 组织机构表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysOrg extends BaseTreeModel<SysOrg> {

    private static final long serialVersionUID = 1L;

    /**
     * 组织机构代码
     */
    @NotNull(message = "组织机构代码不能为空！")
    @Size(max = 20,message = "组织机构代码最大长度为20！")
    private String orgCode;

    /**
     * 组织机构名称
     */
    @NotNull(message = "组织机构名称不能为空！")
    @Size(max = 50,message = "组织机构名称最大长度为50！")
    private String orgName;

    /**
     * 组织机构简称
     */
    @NotNull(message = "组织机构简称不能为空！")
    @Size(max = 50,message = "组织机构简称最大长度为50！")
    private String orgShortName;

    /**
     * 组织机构类型
     */
    @NotNull(message = "组织机构类型不能为空！")
    private String orgType;

    @TableField(exist = false)
    private String orgTypeName;

    /**
     * 组织机构负责人
     */
    @Size(max = 20,message = "组织机构负责人最大长度为20！")
    private String orgLeader;

    /**
     * 组织机构所在地址
     */
    @Size(max = 60,message = "联系地址最大长度为60！")
    private String orgAddress;

    /**
     * 组织机构联系电话
     */
    private String orgPhone;

    /**
     * 组织机构邮编编码
     */
    private String orgPostalCode;

    /**
     * 显示顺序
     */
    private Integer orgSort;

    /**
     * 使用状态（0:正常 2:停用）
     */
    @Pattern(regexp = "[02]",message = "系统内置标识值必须为0或2（0:正常 2:停用）！")
    private String orgStatus;

    /**
     * 备注信息
     */
    @Size(max = 150,message = "备注信息最大长度为150！")
    private String remarks;

    /**
     * 上级组织机构信息
     */
    @ApiModelProperty(
            hidden = true
    )
    @TableField(exist = false)
    private SysOrg  parentOrg;
}
