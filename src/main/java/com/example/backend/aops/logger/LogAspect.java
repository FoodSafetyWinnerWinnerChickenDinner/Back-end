package com.example.backend.aops.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {
    Logger logger =  LoggerFactory.getLogger(LogAspect.class);

    /*@Around("execution(* com.example.backend.services..*.*(..))")*/
    /*@Around("execution(* com.example.backend..*.*(..))")*/

    @Around("execution(* com.example.backend.controllers..*.*(..))")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("start - " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());
        Object result = pjp.proceed();
        logger.info("finished - " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());

        return result;
    }
}
