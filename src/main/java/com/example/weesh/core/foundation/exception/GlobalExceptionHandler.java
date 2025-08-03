package com.example.weesh.core.foundation.exception;

import com.example.weesh.core.foundation.log.LoggingUtil;
import com.example.weesh.core.shared.ApiResponse;
import com.example.weesh.core.user.domain.exception.DuplicateUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateUserException(DuplicateUserException ex) {
        LoggingUtil.error("Handling duplicate user exception: " + ex.getMessage(), ex);
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        LoggingUtil.error("Internal server error: " + ex.getMessage(), ex);
        return new ResponseEntity<>(ApiResponse.error("Internal server error... :( " + ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}