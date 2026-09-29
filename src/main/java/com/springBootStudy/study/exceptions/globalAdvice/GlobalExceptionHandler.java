package com.springBootStudy.study.exceptions.globalAdvice;


import com.springBootStudy.study.dtos.api.ApiError;
import com.springBootStudy.study.dtos.user.ErrorResponse;
import com.springBootStudy.study.exceptions.api.EmailAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Faca: crie um método auxiliar para a Exception.
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailExists(
            EmailAlreadyExistsException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "Conflito", ex.getMessage(), request);
    }


    private ResponseEntity<ApiError> build(
            HttpStatus status, String error,
            String msg,
            HttpServletRequest req
            ) {
        return ResponseEntity.status(status).body(new ApiError(
                status.value(),
                error,
                msg,
                req.getRequestURI(),
                LocalDateTime.now()
        ));
    }












}
