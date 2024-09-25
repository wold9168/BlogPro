package com.hgd.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LogAspect {
    @Pointcut("execution(* com.hgd.service.impl.*.*(..))")
    public void pointCut(){
    }

//    @After("pointCut()")
//    @Before("pointCut()")
//    @AfterReturning("pointCut()")
//    @AfterThrowing("pointCut()")
//    public void after(){
//    }

    @Around("pointCut()")//一个顶上面四个
    //aop增强代码块被封装成joinPoint
    public Object aRound(ProceedingJoinPoint joinPoint){
//        Object proceed = null;
        try {
            log.warn(String.format("%s执行参数：%s",joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs())));
            Object proceed = joinPoint.proceed(joinPoint.getArgs());

            log.warn(String.format("%s的返回值：%s",joinPoint.getSignature().getName(), JSON.toJSONString(proceed)));
            return proceed;
        } catch (Throwable e) {
            log.error(String.format("%s报错：%s",joinPoint.getSignature().getName(), e.getMessage()));
            throw new RuntimeException(e);
        }
    }
}
