package com.jeerigger.activiti;/*
 * @项目名称: jeechuangshen
 * @日期: 2019/4/22 0022 上午 10:17
 * @版权: 2019 河南中审科技有限公司 Inc. All rights reserved.
 * @开发公司或单位：河南中审科技有限公司研发中心
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName: JeeActivitiApplication
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/4/22 0022 上午 10:17
 * @version: V1.0
 */
@SpringBootApplication
public class JeeActivitiApplication extends SpringBootServletInitializer{

    public static void main(String[] args){
        SpringApplication.run(JeeActivitiApplication.class,args);
    }

    @Override protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(JeeActivitiApplication.class);
    }
}
