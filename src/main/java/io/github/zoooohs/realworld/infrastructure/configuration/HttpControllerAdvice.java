package io.github.zoooohs.realworld.infrastructure.configuration;

import io.github.zoooohs.realworld.application.exception.RealWorldException;
import io.github.zoooohs.realworld.infrastructure.model.HttpErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = HttpControllerAdvice.class)
public class HttpControllerAdvice {
    @ExceptionHandler(RealWorldException.class)
    public ResponseEntity<HttpErrorResponse> unauthorizedRequest(RealWorldException e) {
        HttpStatus status;
        switch (e.type()) {
            case BAD_REQUEST -> {
                status = HttpStatus.BAD_REQUEST;
            }
            case NOT_FOUND -> {
                status = HttpStatus.NOT_FOUND;
            }
            case UNAUTHORIZED -> {
                status = HttpStatus.UNAUTHORIZED;
            }
            case CONFLICT -> {
                status = HttpStatus.CONFLICT;
            }
            default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return ResponseEntity.status(status).body(new HttpErrorResponse(e.getMessage()));
    }
}
