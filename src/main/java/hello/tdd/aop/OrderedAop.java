package hello.tdd.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
public class OrderedAop {

    @Aspect
    @Order(2)
    @Component
    public static class LogAspect {
        @Around("execution(* hello.tdd.controller..*(..))")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[LOG] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

    @Aspect
    @Order(1)
    @Component
    public static class TimeAspect{
        @Around("execution(* hello.tdd..*(..))")
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
}
