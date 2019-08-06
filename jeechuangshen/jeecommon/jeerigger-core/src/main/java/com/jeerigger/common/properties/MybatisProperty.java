package com.jeerigger.common.properties;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MybatisProperty {
    private String scanBasePackage="com.jeerigger.module";
}
