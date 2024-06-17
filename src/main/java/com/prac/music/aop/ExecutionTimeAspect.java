package com.prac.music.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
@Slf4j
public class ExecutionTimeAspect {
    @Around("execution(* com.prac..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
//        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
//        long endTime = System.currentTimeMillis();
//        System.out.println("{" + joinPoint.getSignature() + "}" + "executed in {" + (endTime - startTime) + "} ms");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            String method = request.getMethod();
            String url = request.getRequestURL().toString();
            log.info(method+"    "+url);
        }

            return proceed;
    }
}