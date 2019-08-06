package com.jeerigger.activiti.vo;/*
 * @项目名称: jeechuangshen
 * @日期: 2019/3/18 0018 上午 9:01
 * @版权: 2019 河南中审科技有限公司 Inc. All rights reserved.
 * @开发公司或单位：河南中审科技有限公司研发中心
 */

import lombok.Data;

/**
 * @ClassName: CandidateVo
 * @Description: 审批节点待选择用户
 * @author: libaojian
 * @date: 2019/3/18 0018 上午 9:01
 * @version: V1.0
 */
@Data
public class CandidateVo {

    /**
     * 用户ID
     */
    String userId;
    /**
     * 用户名称
     */
    String userName;

}
