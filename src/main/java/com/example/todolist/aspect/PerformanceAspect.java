package com.example.todolist.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {
    private static final Logger log = LoggerFactory.getLogger(PerformanceAspect.class);

    @Around("execution(* com.example.todolist.service.TodoService.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = pjp.proceed();

        long duration = System.currentTimeMillis() - start;
        String method = pjp.getSignature().toShortString();
        log.info("[AOP PERFORMANCE] {} completed in {} ms", method, duration);

        return result;
    }

}
