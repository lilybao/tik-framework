package com.jeerigger.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jeerigger")
public class JeeRiggerProperties {
    private ShiroProperty shiro=new ShiroProperty();
    private MybatisProperty mybatis=new MybatisProperty();
}

