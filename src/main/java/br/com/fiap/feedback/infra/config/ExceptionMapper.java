package br.com.fiap.feedback.infra.config;

import br.com.fiap.feedback.core.exception.BusinessException;
import br.com.fiap.feedback.core.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.jboss.resteasy.reactive.server.UnwrapException;

import java.util.List;

@UnwrapException({UserNotFoundException.class, BusinessException.class, Exception.class, ConstraintViolationException.class})
public class ExceptionMapper {

    @ServerExceptionMapper
    public Response userNotFoundExceptionHandle(UserNotFoundException e) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @ServerExceptionMapper
    public Response businessExceptionHandle(BusinessException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("Erro ao processar a requisição", List.of(e.getMessage())))
                .build();
    }

    @ServerExceptionMapper
    public Response mapConstraintViolationException(ConstraintViolationException exception) {
        var errors = exception.getConstraintViolations().stream()
                .map(violation -> {
                    String fieldName = "";
                    for (Path.Node node : violation.getPropertyPath()) {
                        fieldName = node.getName();
                    }
                    return fieldName + ": " + violation.getMessage();
                })
                .toList();

        // Return a custom 400 Bad Request response
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("Erro na validação dos campos", errors))
                .build();
    }

    @ServerExceptionMapper
    public Response exceptionHandle(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
