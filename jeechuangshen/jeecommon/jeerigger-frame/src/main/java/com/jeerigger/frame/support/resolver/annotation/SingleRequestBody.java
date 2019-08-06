package com.jeerigger.frame.support.resolver.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SingleRequestBody {
    String value();
}
