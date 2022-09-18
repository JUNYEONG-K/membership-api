package hello.tdd.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//@Component
@Aspect
@Slf4j
public class TimeTraceAop {

    @Around("execution(* hello.tdd.controller..*(..))")
    public Object timeCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("START: {}", joinPoint.toString());
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.info("END: {}, timeMs = {}ms", joinPoint.toString(), timeMs);
        }
    }
}
