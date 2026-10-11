package com.example.sonarqubetest.todo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TodoExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    public ProblemDetail handleNotFound(TodoNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
