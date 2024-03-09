package com.dimas.controller.exception;

import com.dimas.exception.ApiBaseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
//public class GenericExceptionHandler implements ExceptionMapper<Exception> {
public class GenericExceptionHandler implements ExceptionMapper<Throwable> {

    private final jakarta.inject.Provider<ContainerRequestContext> containerRequestContextProvider;

    @Override
    public Response toResponse(Throwable exception) {
        return mapExceptionToResponse(exception);
    }

    private Response mapExceptionToResponse(Throwable exception) {
        // Use response from WebApplicationException as they are
        if (exception instanceof WebApplicationException) {
            // Overwrite error message
            Response originalErrorResponse = ((WebApplicationException) exception).getResponse();
            return Response.fromResponse(originalErrorResponse)
                    .entity(exception.getMessage())
                    .build();
        }
        // Special mappings
        else if (exception instanceof IllegalArgumentException) {
            return Response.status(400).entity(exception.getMessage()).build();
        }
        // Use 500 (Internal Server Error) for all other
        else {
            log.error("Failed to process request to: {}",
                    containerRequestContextProvider.get().getUriInfo().getAbsolutePath().toString(),
                    exception
            );
            return Response.serverError().entity("Internal Server Error"+ "generic").build();
        }
    }

}