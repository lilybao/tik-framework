package com.jeerigger.common.annotation;

import com.jeerigger.common.enums.LogTypeEnum;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Log {
    /**
     * 日志类型
     */
    LogTypeEnum logType();

    /**
     * 日志标题
     */
    String logTitle() default "";
}
