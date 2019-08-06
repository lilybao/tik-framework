package com.jeerigger.frame.exception;


import com.jeerigger.frame.base.controller.ResultCodeEnum;

/**
 * 数据验证异常类
 */
public class ValidateException extends BaseException {

    public ValidateException(String message) {
        super(message);
    }

    @Override
    public ResultCodeEnum getResultCode() {
        return ResultCodeEnum.ERROR_VALIDATE_FAIL;
    }
}
