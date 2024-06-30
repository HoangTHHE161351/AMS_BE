package vn.attendance.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ElearningBatchAspect {

    @Value("${spring-boot.batch.enable}")
    private String isBatchEnable;

    @Around("@annotation(vn.attendance.common.AMS)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        if (isBatchEnable != null && isBatchEnable.equalsIgnoreCase("true")) {
            Object[] arguments = joinPoint.getArgs();
            return joinPoint.proceed(arguments);
        }
        return null;
    }
}
