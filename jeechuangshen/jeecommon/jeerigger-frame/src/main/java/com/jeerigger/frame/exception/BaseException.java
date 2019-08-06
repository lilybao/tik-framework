package com.jeerigger.frame.exception;

import com.jeerigger.frame.base.controller.ResultCodeEnum;

/**
 * 异常基类
 */
public abstract class BaseException extends RuntimeException {

    public BaseException(Throwable throwable) {
        super(throwable);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public abstract ResultCodeEnum getResultCode();
}
