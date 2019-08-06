package com.jeerigger.frame.exception;


import com.jeerigger.frame.base.controller.ResultCodeEnum;

/**
 * 系统异常
 */
public class FrameException extends BaseException {

    private ResultCodeEnum ResultCode;

    /**
     * 构造函数 自定义异常类
     *
     * @param throwable
     */
    public FrameException(Throwable throwable) {
        super(throwable);
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param message
     */
    public FrameException(String message) {
        super(message);
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param message
     * @param throwable
     */
    public FrameException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param ResultCode
     */
    public FrameException(ResultCodeEnum ResultCode) {
        super(ResultCode.getMessage());
        this.ResultCode = ResultCode;
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param ResultCode
     * @param throwable
     */
    public FrameException(ResultCodeEnum ResultCode, Throwable throwable) {
        super(throwable);
        this.ResultCode = ResultCode;
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param ResultCode
     * @param message
     */
    public FrameException(ResultCodeEnum ResultCode, String message) {
        super(message);
        this.ResultCode = ResultCode;
    }

    /**
     * 构造函数 自定义异常类
     *
     * @param ResultCode
     * @param message
     * @param throwable
     */
    public FrameException(ResultCodeEnum ResultCode, String message, Throwable throwable) {
        super(message, throwable);
        this.ResultCode = ResultCode;
    }

    @Override
    public ResultCodeEnum getResultCode() {
        if (this.ResultCode != null) {
            return this.ResultCode;
        } else {
            return ResultCode.ERROR_SYSTEM_EXCEPTION;
        }
    }
}
