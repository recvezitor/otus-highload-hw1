package com.dimas.controller.exception;

import com.dimas.exception.MyJdbcException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Provider
@RequiredArgsConstructor
public class JdbcExceptionHandler implements ExceptionMapper<MyJdbcException> {

    @Override
    public Response toResponse(MyJdbcException exception) {
        log.error("JDBC exception", exception);
        return Response.status(400).entity(JdbcError.builder()
                .message(exception.getMessage())
                .cause(exception.getCause().getMessage())
                .build()).build();
    }

    @Data
    @Builder
    public static class JdbcError {
        private String message;
        private String cause;
    }

}