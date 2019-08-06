package com.jeerigger.module.sys.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.jeerigger.frame.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author wangcy
 * @since 2018-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser extends BaseModel<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 登录密码
     */
    @JSONField(serialize = false)
    private String loginPassword;

    /**
     * 用户编号
     */
    private String userNumber;

    /**
     * 用户性别
     */
    private String userSex;

    /**
     * 身份证件类型
     */
    private String identityType;

    /**
     * 身份证件号码
     */
    private String identityCode;

    /**
     * 人员职务
     */
    private String userDuty;

    /**
     * 人员职级
     */
    private String userRank;

    /**
     * 联系电话
     */
    private String userPhone;

    /**
     * 联系手机
     */
    private String userMobile;

    /**
     * 联系地址
     */
    private String userAddress;

    /**
     * 邮箱地址
     */
    private String userEmail;

    /**
     * 组织机构uuid
     */
    private String orgUuid;
    /**
     * 所属机构名称
     */
    private String orgName;
    /**
     * 用户排序
     */
    private Integer userSort;

    /**
     * 政治面貌
     */
    private String politicsStatus;

    /**
     * 用户状态（0正常 2停用 3冻结）
     */
    private String userStatus;

    /**
     * 密码安全级别（0:初始 1:很弱 2:弱 3:安全 4:很安全）
     */
    private String pwdSecurityLevel;

    /**
     * 密码最后更新时间
     */
    private Date pwdUpdateDate;

    /**
     * 是否为机构管理员（0:否 1:是）
     */
    private String mgrFlag;

    /**
     * 冻结时间
     */
    private Date freezeDate;

    /**
     * 冻结原因
     */
    private String freezeCause;

}
