package com.liewmanchoi.zheye.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wangsheng
 * @date 2019/5/16
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    @Before("execution(* com.liewmanchoi.zheye.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("args: ");
        for (Object arg : joinPoint.getArgs()) {
            if (arg == null) {
                break;
            }
            stringBuilder.append(arg.toString()).append("|");
        }
        log.info("before method: " + stringBuilder.toString());
    }

    @After("execution(* com.liewmanchoi.zheye.controller.*Controller.*(..))")
    public void afterMethod() {
        log.info("after method: " + new Date());
    }
}
