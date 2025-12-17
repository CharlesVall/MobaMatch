package io.github.charlesvall.mobamatch.infrastructure.exception.handler;


import io.github.charlesvall.mobamatch.infrastructure.exception.InvalidBodyRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import tools.jackson.databind.exc.InvalidFormatException;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(InvalidBodyRequestException.class)
    public ProblemDetail handleInvalidBodyRequest(InvalidBodyRequestException exception, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Invalid request body");
        problem.setDetail(exception.getMessage());
        problem.setType(URI.create("/errors/invalid-body"));
        problem.setInstance(requestPath(request));
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception, WebRequest request) {
        ProblemDetail problem = requestFormatProblem(request);

        Map<String, String> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage()
                ));

        problem.setProperty("invalid_field", fieldErrors);
        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleInvalidEnum(HttpMessageNotReadableException exception, WebRequest request) {
        ProblemDetail problem = requestFormatProblem(request);

        Map<String, String> fieldErrors = new HashMap<>();

        if (exception.getCause() instanceof InvalidFormatException invalidFormat) {

            String field = invalidFormat.getPath().get(0).getPropertyName();
            Object badValue = invalidFormat.getValue();

            String[] validValues = Arrays.stream(invalidFormat.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .toArray(String[]::new);

            fieldErrors.put(
                    field,
                    "Invalid enum value '" + badValue +
                            "'. Allowed: " + Arrays.toString(validValues)
            );
        }

        problem.setProperty("invalid_field", fieldErrors);
        return problem;
    }

    private URI requestPath(WebRequest request) {
        return URI.create(((ServletWebRequest) request)
                .getRequest()
                .getRequestURI());
    }

    private ProblemDetail requestFormatProblem(WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation Error");
        problem.setType(URI.create("/errors/validation"));
        problem.setInstance(requestPath(request));
        return problem;
    }
}