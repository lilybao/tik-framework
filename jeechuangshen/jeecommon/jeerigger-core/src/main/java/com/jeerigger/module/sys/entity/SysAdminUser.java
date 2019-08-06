package com.jeerigger.module.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeerigger.frame.base.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.Size;

@Data
public class SysAdminUser extends BaseModel<SysAdminUser> {
    /**
     * 管理员名称
     */
    private String userName;
    /**
     * 登录名
     */
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
    @TableField
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
