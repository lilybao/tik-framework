package com.jeerigger.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.jeerigger.common.annotation.Log;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.common.user.BaseUser;
import com.jeerigger.module.sys.util.SysLogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class SysLogAspect {
    /**
     * 定义切点(切BaseController以及所有继承了BaseController的子类)
     */
    @Pointcut("execution(public * com.jeerigger.frame.base.controller.BaseController+.*(..)))")
    public void sysLog() {
    }

    /**
     * 环绕基类为BaseController进行业务处理
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("sysLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //提前获取用户信息，防止用户退出无法记录用户登出日志
        BaseUser userData = ShiroUtil.getUserData();
        long beginTime = System.currentTimeMillis();//执行开始时间
        Throwable throwable = null;
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            throwable = ex;
            throw throwable;
        } finally {
            long endTime = System.currentTimeMillis();//执行结束时间
            if (userData == null) {
                //用户登录操作
                userData = ShiroUtil.getUserData();
            }
            saveLog(userData, joinPoint, throwable, endTime - beginTime);
        }
    }

    private void saveLog(BaseUser userData, ProceedingJoinPoint joinPoint, Throwable throwable, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = method.getAnnotation(Log.class);
        if (log != null) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String params = "";
            try {
                params = JSONObject.toJSONString(joinPoint.getArgs());
            } catch (Exception ex) {
            }
            SysLogUtil.saveLog(userData, request, throwable, params, log, time);
        }
    }
}
