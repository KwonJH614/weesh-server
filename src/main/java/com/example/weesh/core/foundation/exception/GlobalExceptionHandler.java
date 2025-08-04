package com.example.weesh.core.foundation.exception;

import com.example.weesh.core.foundation.log.LoggingUtil;
import com.example.weesh.core.shared.ApiResponse;
import com.example.weesh.core.user.domain.exception.DuplicateUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateUserException(DuplicateUserException ex) {
        LoggingUtil.error("Handling duplicate user exception: " + ex.getMessage(), ex);
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(ValidationException ex) {
        LoggingUtil.error("Validation error: " + ex.getMessage(), ex);
        return new ResponseEntity<>(ApiResponse.error("입력값이 올바르지 않습니다: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("입력값이 올바르지 않습니다.");
        LoggingUtil.error("Validation error: " + errorMessage, ex);
        return new ResponseEntity<>(ApiResponse.error(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleResponseStatusException(ResponseStatusException ex) {
        LoggingUtil.error("Response status error: " + ex.getReason(), ex);
        return new ResponseEntity<>(ApiResponse.error(ex.getReason()), ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        LoggingUtil.error("Internal server error: " + ex.getMessage(), ex);
        return new ResponseEntity<>(ApiResponse.error("Internal server error... :( " + ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}