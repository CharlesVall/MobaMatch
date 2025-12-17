package io.github.charlesvall.mobamatch.infrastructure.exception.handler;


import io.github.charlesvall.mobamatch.domain.exception.MatchIsNotValidException;
import io.github.charlesvall.mobamatch.domain.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;

@RestControllerAdvice
public class DomainExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException exception, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Entity Not Found");
        problem.setDetail(exception.getMessage());
        problem.setType(URI.create("/errors/player-not-found"));
        problem.setInstance(requestPath(request));
        return problem;
    }

    @ExceptionHandler(MatchIsNotValidException.class)
    public ProblemDetail handleInvalidMatch(MatchIsNotValidException exception, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Match Is Not Valid");
        problem.setDetail(exception.getMessage());
        problem.setType(URI.create("/errors/match-not-valid"));
        problem.setInstance(requestPath(request));
        return problem;
    }

    private URI requestPath(WebRequest request) {
        return URI.create(((ServletWebRequest) request)
                .getRequest()
                .getRequestURI());
    }
}
