package com.djz.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;


public class AspectApiImpl implements AspectApi {

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable {
        return null;
    }
}
