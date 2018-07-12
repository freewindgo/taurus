package com.cf.taurus.base.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * TimeCostAspect
 *
 * @author 于文硕
 * @since 2018/7/12 16:40
 */
@Aspect
@Component
@Slf4j
public class TimeCostAspect {

    @Pointcut(value = "@annotation(com.cf.taurus.base.interceptor.TimeCost)")
    public void timeCost(){

    }

    @Around("@annotation(timeCost)")
    public void around(ProceedingJoinPoint joinPoint, TimeCost timeCost) throws Throwable{
        Long startTime = new Date().getTime();
        joinPoint.proceed();
        Long endTime = new Date().getTime();
        Long cost = endTime - startTime;
        log.info("Class {}, Method {}, time cost {}ms", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), cost);
    }

}
