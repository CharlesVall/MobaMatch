package io.github.charlesvall.mobamatch.infrastructure.exception;

import io.github.charlesvall.mobamatch.domain.exception.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlayerNotFoundException.class)
    public ProblemDetail handlePlayerNotFound(PlayerNotFoundException exception, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Player Not Found");
        problem.setDetail(exception.getMessage());
        problem.setType(URI.create("/errors/player-not-found"));
        problem.setInstance(requestPath(request));
        return problem;
    }

    @ExceptionHandler(InvalidIdFormatException.class)
    public ProblemDetail handleInvalidIdFormat(InvalidIdFormatException exception, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Invalid ID format");
        problem.setDetail(exception.getMessage());
        problem.setType(URI.create("/errors/invalid-id"));
        problem.setInstance(requestPath(request));
        return problem;
    }

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
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation Error");
        problem.setType(URI.create("/errors/validation"));
        problem.setInstance(requestPath(request));

        Map<String, String> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage()
                ));

        problem.setProperty("invalid_fields", fieldErrors);
        return problem;
    }

    private URI requestPath(WebRequest request) {
        return URI.create(((ServletWebRequest) request)
                .getRequest()
                .getRequestURI());
    }
}
