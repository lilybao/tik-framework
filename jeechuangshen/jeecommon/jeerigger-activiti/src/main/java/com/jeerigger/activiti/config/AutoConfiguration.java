package com.jeerigger.activiti.config;/*
 * @项目名称: jeechuangshen
 * @日期: 2019/3/22 0022 下午 5:31
 * @版权: 2019 河南中审科技有限公司 Inc. All rights reserved.
 * @开发公司或单位：河南中审科技有限公司研发中心
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: AutoConfiguration
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/3/22 0022 下午 5:31
 * @version: V1.0
 */
@Configuration
@MapperScan({"com.jeerigger.activiti.dao"})
public class AutoConfiguration {

}
