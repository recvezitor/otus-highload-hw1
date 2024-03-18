package com.dimas.log;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.Priorities;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Interceptor
@LogArguments
@Priority(Priorities.USER - 1)
public class LogArgumentsInterceptor {

    private final List<Class<?>> skipList = List.of(byte[].class);

    @AroundInvoke
    public Object logContext(InvocationContext context) {
        doLog(context);
        return getProceed(context);
    }

    private void doLog(InvocationContext context) {
        var aClazz = context.getMethod().getDeclaringClass();
        log.info("{}#{}", aClazz.getName(), context.getMethod().getName());
        var names = context.getMethod().getParameters();
        var values = context.getParameters();
        for (int i = 0; i < names.length; i++) {
            log.info("[{}]={}", names[i].getName(), (!isInSkipList(values[i]) ? values[i] : "#skipped#"));
        }
    }

    private boolean isInSkipList(Object obj) {
        return skipList.contains(obj.getClass());
    }

    @SneakyThrows
    private Object getProceed(InvocationContext context) {
        return context.proceed();
    }

}

