package com.dimas.log;

import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.Priorities;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static com.dimas.util.Util.isEnabledByProperty;
import static java.util.Objects.isNull;

@Slf4j
@Interceptor
@LogRequest
@Priority(Priorities.USER - 1)
public class LogRequestInterceptor {

    @Inject
    HttpServerRequest currentRequest;

    @AroundInvoke
    public Object logContext(InvocationContext context) {
        doLog(context);
        return getProceed(context);
    }

    private void doLog(InvocationContext context) {
        if (!isNull(currentRequest) && isEnabledByProperty("rest-logging.enabled")) {
            log.info("{} {}", currentRequest.method(), currentRequest.absoluteURI());
            for (var entry : currentRequest.headers()) {
                log.info("[{}]:{}", entry.getKey(), entry.getValue());
            }
            log.info("Request args: {}", context.getParameters());
        }
    }

    @SneakyThrows
    private Object getProceed(InvocationContext context) {
        return context.proceed();
    }

}

