package com.capgemini.wsb.fitnesstracker.statistics.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class StatisticsLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsLoggingAspect.class);

    @Before("execution(* com.capgemini.wsb.fitnesstracker.statistics.internal.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Before method execution: ")
                .append(className)
                .append(".")
                .append(methodName)
                .append("(");

        for (int i = 0; i < args.length; i++) {
            logMessage.append(args[i]);
            if (i < args.length - 1) {
                logMessage.append(", ");
            }
        }

        logMessage.append(")");
        logger.info(logMessage.toString());
    }

    @AfterReturning(pointcut = "execution(* com.capgemini.wsb.fitnesstracker.statistics.internal.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.info("After method execution: {}.{}() returned: {}", className, methodName, result);
    }
}
