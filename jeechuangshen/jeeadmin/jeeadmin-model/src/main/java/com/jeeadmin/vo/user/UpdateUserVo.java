package com.jeeadmin.vo.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpdateUserVo {
    /**
     * 管理员名称
     */
    @NotNull(message="管理员名称不能为空！")
    @Size(max = 30 ,message = "管理员名称长度最大30！")
    private String userName;

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
}
