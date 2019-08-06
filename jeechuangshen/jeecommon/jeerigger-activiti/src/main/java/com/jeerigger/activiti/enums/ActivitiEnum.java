package com.jeerigger.activiti.enums;/*
 * @项目名称: jeechuangshen
 * @日期: 2019/3/7 0007 上午 9:28
 * @版权: 2019 河南中审科技有限公司 Inc. All rights reserved.
 * @开发公司或单位：河南中审科技有限公司研发中心
 */

/**
 * @ClassName: ActivitiEnum
 * @Description: 流程变量
 * @author: libaojian
 * @date: 2019/3/7 0007 上午 9:28
 * @version: V1.0
 */
public enum ActivitiEnum {

    COMMENTS("comments","审批意见"),
    ASSIGNEE("assignee","审批人"),
    ASSIGNEENAME("assigneeName","审批人名字"),
    INITIATOR("$INITIATOR","流程发起人"),
    PASS("pass","流程判断条件"),
    ORG("org","流程节点优先级"),
    MONEY("money","金额"),
    BUSINESSKEY("${businessKey}","业务主键"),
    STARTUSERID("${startUserId}","发起人登录名"),
    OUTGOING("outgoing","流程走向");

    private  String variableType;

    private String variableDesc;

    ActivitiEnum(String variableType,String variableDesc) {
        this.variableType=variableType;
        this.variableDesc=variableDesc;
    }

    public String getVariableType() {
        return variableType;
    }

    public String getVariableDesc() {
        return variableDesc;
    }
}
