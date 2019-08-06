package com.jeerigger.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jeerigger.frame.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRole extends BaseModel<SysRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色编码
     */
    @NotNull(message = "角色编码不能为空！")
    @Size(max = 30, message = "角色编码长度最大值为30！")
    private String roleCode;

    /**
     * 角色名称
     */
    @NotNull(message = "角色名称不能为空！")
    @Size(max = 50, message = "角色名称长度最大值为50！")
    private String roleName;

    /**
     * 角色类型
     */
    @NotNull(message = "角色类型不能为空！")
    private String roleType;
    /**
     * 角色类型名称
     */
    @TableField(exist = false)
    private String roleTypeName;

    /**
     * 角色状态（0:正常 2:停用）
     */
    @Pattern(regexp = "[02]", message = "角色状态值必须为0或1或2（0：正常 2：停用）！")
    private String roleStatus;

    /**
     * 角色排序
     */
    private Integer roleSort;

    /**
     * 备注
     */
    @Size(max = 150, message = "备注长度最大值为150！")
    private String remarks;

    /**
     * 系统内置（0:否 1:是 ）
     */
    @Pattern(regexp = "[01]", message = "系统字典标识值必须为0或1（0:否 1:是）！")
    private String sysFlag;


    /**
     * 角色分配菜单使用
     */
    @TableField(exist = false)
    private List<String> menuUuidList;

}
