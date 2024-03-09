package com.dimas.controller.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
//@ApplicationScoped
//@LookupIfProperty(name = "rest-logging.enabled", stringValue = "true")
public class LoggingFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
//        Нельзя залогировать бади
        log.info("{} {}", crc.getMethod(), crc.getUriInfo().getAbsolutePath());
        for (String key : crc.getHeaders().keySet()) {
            log.info("[{}]:{}", key, crc.getHeaders().get(key));
        }
    }

}