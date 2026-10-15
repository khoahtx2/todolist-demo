package com.example.todolist.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.todolist.service.TodoService.*(..))")
    public void todoServiceMethods() {
    }

    @Before("todoServiceMethods()")
    public void logBefore(JoinPoint jp) {
        String user = getCurrentUser();
        String method = jp.getSignature().getName();
        Object[] args = jp.getArgs();
        log.info("[AOP BEFORE] User: '{}' -> calling method: {}({})", user, method, Arrays.toString(args));
    }

    @AfterReturning(pointcut = "todoServiceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint jp, Object result) {
        String method = jp.getSignature().getName();
        log.info("[AOP AFTER-RETURNING] Method: {} -> returned: {}", method, result);
    }

    @AfterThrowing(pointcut = "todoServiceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint jp, Throwable ex) {
        String method = jp.getSignature().getName();
        log.error("[AOP AFTER-THROWING] Method: {} -> threw exception: {}", method, ex.getMessage());
    }

    private String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
    }
}
