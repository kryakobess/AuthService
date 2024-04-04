package com.example.authservice.controller.advice;

import com.example.authservice.model.dto.ApiErrorResponse;
import com.example.authservice.model.exception.AuthenticationException;
import com.example.authservice.model.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AuthControllerAdvice {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthException(Exception e) {
        log.error("Authentication exception", e);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleBadRequest(Exception e) {
        log.error("Bad request exception", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(Exception e) {
        log.error("Not found exception", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(e.getMessage()));
    }
}
