package com.medical.med.aop.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(com.medical.med.controller..*)")
    public void controllerMethods() {}

    @Pointcut("controllerMethods() && publicMethods()")
    public void controllerPublicMethods() {}

    @Around("controllerPublicMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime  = System.currentTimeMillis();

        HttpServletRequest request = getCurrentHttpRequest();
        String methodName = joinPoint.getSignature().toShortString();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("[START] {} {} - Вход", className, joinPoint.getSignature().getName());

        if (request != null) {
            log.info("[REQUEST] {} {}", request.getMethod(), request.getRequestURI());
        }

        log.debug("[ARGS] {}", Arrays.toString(joinPoint.getArgs()));

        Object result = null;

        try {
            result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - startTime;

            log.info("[END] {}.{} - Выход, время: {} ms", className,
                    methodName,
                    executionTime);
            if (result != null) {
                log.debug("[RESULT] {}", result);
            }

            return result;
        } catch (Exception ex) {
            long executionTime = System.currentTimeMillis() - startTime;

            log.error("[ERROR] {}.{} - Ошибка, время: {} ms", className,
                    methodName,
                    executionTime);
            log.error("[EXCEPTION] {}: {}", ex.getClass().getSimpleName(), ex.getMessage());

            throw ex;
        }
    }

    @AfterThrowing(pointcut = "controllerPublicMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.error("[THROWING] {}.{} - Исключение: {}",
                className, joinPoint.getSignature().getName(), ex.getMessage());
    }

    @AfterReturning(pointcut = "controllerPublicMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.debug("[RETURNING] {}.{} - Возврат: {}",
                className, joinPoint.getSignature().getName(), result);
    }

    private HttpServletRequest getCurrentHttpRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.
                    getRequestAttributes();

            if (attributes != null) {
                return attributes.getRequest();
            }
        } catch (Exception e) {
            log.debug("Не удалось получить HTTP запрос");
        }
        return null;
    }
}
