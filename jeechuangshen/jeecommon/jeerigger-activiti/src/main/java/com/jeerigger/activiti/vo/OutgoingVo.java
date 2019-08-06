package com.jeerigger.activiti.vo;/*
 * @项目名称: jeechuangshen
 * @日期: 2019/3/7 0007 下午 1:59
 * @版权: 2019 河南中审科技有限公司 Inc. All rights reserved.
 * @开发公司或单位：河南中审科技有限公司研发中心
 */

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @ClassName: OutgoingVo
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/3/7 0007 下午 1:59
 * @version: V1.0
 */
@Data
public class OutgoingVo {

    /**
     * 流程走向名称
     */
    private String outgoingName;
    /**
     * 走向需要的参数名称和参数值
     */
    private Map<String,String> params;
    /**
     * 流转条件表达式
     */
    private String conditionExpression;
    /**
     * 流程走向下的候选人员
     */
    private List<CandidateVo> candidateVos;

}
