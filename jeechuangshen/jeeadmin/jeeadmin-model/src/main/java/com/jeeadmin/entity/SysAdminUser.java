package com.jeeadmin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeerigger.frame.base.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * <p>
 * 系统管理员信息表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysAdminUser extends BaseModel<SysAdminUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员名称
     */
    @NotNull(message="用户名称不能为空！")
    @Size(max = 100 ,message = "用户名称长度最大100！")
    private String userName;

    /**
     * 登录名
     */
    @NotNull(message="登录名不能为空！")
    @Size(min =4,max = 20 ,message = "登录名长度最小4最大20！")
    private String loginName;

    /**
     * 登录密码
     */
    @ApiModelProperty(
            hidden = true
    )
    @JsonIgnore
    private String loginPassword;

    /**
     * 联系电话
     */
    private String userPhone;

    /**
     * 联系手机
     */
    private String userMobile;
    /**
     * 备注信息
     */
    @Size(max = 150, message = "备注信息最大长度为150！")
    private String remarks;

    /**
     * 用户状态（0：正常 2：停用 3：冻结）
     */
    @Pattern(regexp = "[023]",message = "用户状态值必须为0或2或3（0：正常 2：停用 3：冻结）！")
    private String userStatus;

    /**
     * 显示顺序（升序）
     */
    private Integer userSort;

    /**
     * 管理员类型（0：超级系统管理员 1：系统管理员）
     */
    private String mgrType;


}
