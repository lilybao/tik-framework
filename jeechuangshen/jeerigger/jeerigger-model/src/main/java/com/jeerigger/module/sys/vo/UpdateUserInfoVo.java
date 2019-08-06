package com.jeerigger.module.sys.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUserInfoVo {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空!")
    @Size(max = 150, message = "用户名最大长度为150!")
    private String userName;

    /**
     * 登录名
     */
    @NotNull(message = "登录名不能为空!")
    @Size(max = 50, message = "登录名最大长度为50")
    private String loginName;

    /**
     * 用户编号
     */
    @NotNull(message = "员工编号不能为空!")
    @Size(max = 20, message = "员工编号最大长度为20!")
    private String userNumber;

    /**
     * 用户性别
     */
    private String userSex;

    /**
     * 身份证件类型
     */
//    @Size(max = 2, message = "身份证件类型最大长度为2!")
    private String identityType;

    /**
     * 身份证件号码
     */
//    @Pattern(regexp = "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/", message = "身份证号必须按照国家规定填写!")
    @Size(max = 20, message = "身份证件号码最大长度为20!")
    private String identityCode;

    /**
     * 人员职务
     */
    @Size(max = 2, message = "人员职务最大长度为2!")
    private String userDuty;

    /**
     * 人员职级
     */
    @Size(max = 2, message = "人员职级最大长度为2!")
    private String userRank;

    /**
     * 联系电话
     */
//    @Pattern(regexp = "/^0\\d{2,3}-\\d{7,8}$/", message = "联系电话必须按照国家规定填写!")
    @Size(max = 20, message = "联系电话最大长度为20!")
    private String userPhone;

    /**
     * 联系手机
     */
//    @Pattern(regexp = "/^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\\\d{8}$/", message = "手机号码必须按照国家规定填写!")
    @Size(max = 12, message = "联系手机最大长度为12！")
    private String userMobile;

    /**
     * 联系地址
     */
    @Size(max = 200, message = "联系地址最大长度为200！")
    private String userAddress;

    /**
     * 邮箱地址
     */
//    @Pattern(regexp = "/^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-z]{2,}$/", message = "邮箱必须按照国家规定填写!")
    @Email(message = "邮箱地址不正确！")
    @Size(max = 50, message = "邮箱地址最大长度为50！")
    private String userEmail;

    /**
     * 组织机构uuid
     */
    @NotNull(message = "所属机构代码不能为空！")
    private String orgUuid;
    /**
     * 所属机构名称
     */
    @ApiModelProperty(
            hidden = true
    )
    @TableField(exist = false)
    private String orgName;

    /**
     * 政治面貌
     */
    @Size(max = 2, message = "政治面貌最大长度为2！")
    private String politicsStatus;

    /**
     * 用户状态（0正常 2停用 3冻结）
     */
    @Pattern(regexp = "[0123]", message = "用户状态必须为0或1或2或3 (0:正常 2:停用 3:冻结)！")
    private String userStatus;
    /**
     * 备注信息
     */
    @Size(max = 150, message = "备注信息最大长度为150！")
    private String remarks;
}
