package com.wanghui.blog.aspect;

import com.alibaba.fastjson.JSON;
import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Description:
 *  打印日志切面类
 * @Author 王辉
 * @Create 2023/3/30 21:12
 * @Version 1.0
 */
@Component
@Aspect
@Slf4j
public class LogPrintAspect {

    //环绕通知
    @Around("@annotation(com.wanghui.blog.annotation.SelfDefinedSystemLog)")
    public Object printLog(ProceedingJoinPoint pjp) throws Throwable {
        Object ret = null;
        try {
            handleBefore(pjp);
            ret = pjp.proceed();
            handleAfter(ret);
        }finally{
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }
        return ret;
    }

    //目标方法执行前执行的方法
    private void handleBefore(ProceedingJoinPoint pjp) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        SelfDefinedSystemLog selfDefinedSystemLog = getSystemLog(pjp);

        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", selfDefinedSystemLog.BusinessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", pjp.getSignature().getDeclaringTypeName(),((MethodSignature)pjp.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(pjp.getArgs()));
    }

    private SelfDefinedSystemLog getSystemLog(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(SelfDefinedSystemLog.class);
    }

    //目标方法执行后执行的方法
    private void handleAfter(Object ret) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(ret));
    }

}
