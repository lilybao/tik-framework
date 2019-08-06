package com.jeerigger.frame.handler;

import com.alibaba.fastjson.JSONException;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.controller.ResultData;
import com.jeerigger.frame.exception.BaseException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 全局异常拦截
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //运行时异常
    @ExceptionHandler(RuntimeException.class)
    public ResultData runtimeExceptionHandler(RuntimeException ex) {
        return rtnResultData(ResultCodeEnum.ERROR_RUNTIME_EXCEPTION, ex);
    }

    //空指针异常
    @ExceptionHandler(NullPointerException.class)
    public ResultData nullPointerExceptionHandler(NullPointerException ex) {
        return rtnResultData(ResultCodeEnum.ERROR_NULLPOINTER_EXCEPTION, ex);
    }

    //类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public ResultData classCastExceptionHandler(ClassCastException ex) {

        return rtnResultData(ResultCodeEnum.ERROR_CLASSCAST_EXCEPTION, ex);
    }

    //IO异常
    @ExceptionHandler(IOException.class)
    public ResultData iOExceptionHandler(IOException ex) {
        return rtnResultData(ResultCodeEnum.ERROR_IO_EXCEPTION, ex);
    }

    //未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    public ResultData noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        return rtnResultData(ResultCodeEnum.ERROR_NOSUCHMETHOD_EXCEPTION, ex);
    }

    //数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResultData indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        return rtnResultData(ResultCodeEnum.ERROR_INDEXOUTOFBOUNDS_EXCEPTION, ex);
    }

    //数据格式化异常
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResultData dataParseException(HttpMessageNotReadableException ex) {
        return rtnResultData(ResultCodeEnum.ERROR_PARSE_EXCEPTION, ex);
    }

    //数据格式化异常
    @ExceptionHandler({JSONException.class})
    public ResultData jsonParseException(JSONException ex) {
        return rtnResultData(ResultCodeEnum.ERROR_PARSE_EXCEPTION, ex);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResultData exception(AuthenticationException ex) {
        if (ex.getCause() instanceof BaseException) {
            BaseException baseException = (BaseException) ex.getCause();
            return rtnResultData(baseException);
        }
        return rtnResultData(ResultCodeEnum.ERROR_SYSTEM_EXCEPTION, ex);
    }

    /**
     * 用户为认证
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({UnauthenticatedException.class})
    public ResultData exception(UnauthenticatedException ex) {
        return rtnResultData(ResultCodeEnum.ERROR_VALIDATE_TOKEN);
    }

    /**
     * 用户无权限
     *
     * @return
     */
    @ExceptionHandler({UnauthorizedException.class})
    public ResultData exception(UnauthorizedException ex) {
        return rtnResultData(ResultCodeEnum.ERROR_NO_PERMISSION);
    }

    //自定义异常
    @ExceptionHandler({BaseException.class})
    public ResultData ValidateException(BaseException ex) {
        return rtnResultData(ex);
    }

    //其他错误
    @ExceptionHandler({Exception.class})
    public ResultData exception(Exception ex) {
        return rtnResultData(ResultCodeEnum.ERROR_SYSTEM_EXCEPTION, ex);
    }


    private ResultData rtnResultData(BaseException ex) {
        logger.error(ex.getMessage());
        return rtnResultData(ex.getResultCode().getCode(), ex.getMessage(), "");
    }

    private ResultData rtnResultData(ResultCodeEnum ResultCode) {
        logger.error(ResultCode.getMessage());
        return rtnResultData(ResultCode.getCode(), ResultCode.getMessage(), "");
    }

    private ResultData rtnResultData(ResultCodeEnum ResultCode, Throwable ex) {
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return rtnResultData(ResultCode.getCode(), ResultCode.getMessage(), ex.getMessage());
    }

    private ResultData rtnResultData(String code, String message, String details) {
        ResultData resultData = new ResultData();
        resultData.setCode(code);
        resultData.setMessage(message);
        resultData.setDetails(details);
        return resultData;
    }

}
