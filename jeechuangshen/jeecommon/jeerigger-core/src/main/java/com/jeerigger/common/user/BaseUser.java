package com.jeerigger.common.user;

import com.alibaba.fastjson.annotation.JSONField;
import eu.bitwalker.useragentutils.DeviceType;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户基础信息
 */
@Data
public abstract class BaseUser implements Serializable {
    /**
     * 用户UUID
     */
    private String userUuid;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户类型
     */
    private UserTypeEnum userType;
    /**
     * 用户客户端设备类型
     */
    private DeviceType deviceType;
    /**
     * 用户Token
     */
    @JSONField(name = "token")
    private String userToken;
    /**
     * 用户登录名
     */
    private String loginName;
    /**
     * 组织机构UUID
     */
    private String orgUuid;
    /**
     * 组织机构名称
     */
    private String orgName;

    /**
     * 用户扩展信息
     */
    private Map<String,Object> param;
}
