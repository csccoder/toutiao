package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
public class LogAspect {
    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void before(JoinPoint joinPoint){
        StringBuilder stringBuilder=new StringBuilder();

        stringBuilder.append("\ntoString:"+joinPoint.toString());
        stringBuilder.append("\ntoShortString:"+joinPoint.toShortString());
        stringBuilder.append("\ntoLongString:"+joinPoint.toLongString());
        stringBuilder.append("\n|kind:"+joinPoint.getKind());
        stringBuilder.append("\n|args:"+ Arrays.toString(joinPoint.getArgs()));
        stringBuilder.append("\n|signature:"+joinPoint.getSignature());
        stringBuilder.append("\n|sourceLocation:"+joinPoint.getSourceLocation());
        stringBuilder.append("\n|staticPart:"+joinPoint.getStaticPart());
        stringBuilder.append("\n|target:"+joinPoint.getTarget());
        stringBuilder.append("\n|this:"+joinPoint.getThis());

        logger.info("before method:"+stringBuilder.toString());
    }
    @After("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void after(JoinPoint joinPoint){
        logger.info("after method:"+joinPoint.toShortString());
    }
}
