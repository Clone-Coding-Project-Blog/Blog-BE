package com.mizzle.blogrest.advice;

import com.mizzle.blogrest.advice.error.DefaultException;
import com.mizzle.blogrest.advice.error.InvalidParameterException;
import com.mizzle.blogrest.advice.payload.ErrorCode;
import com.mizzle.blogrest.advice.payload.ErrorResponse;
import com.mizzle.blogrest.config.security.error.CustomAuthenticationException;
import com.mizzle.blogrest.payload.response.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ApiControllerAdvice {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<?> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        
        final ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .code(e.getMessage())
                .clazz(e.getMethod())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .code(e.getMessage())
                .clazz(e.getBindingResult().getObjectName())
                .message(e.toString())
                .fieldErrors(e.getFieldErrors())
                .build();

        response.setFieldErrors(e.getFieldErrors());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> handleInvalidParameterException(InvalidParameterException e) {
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .code(e.getMessage())
                .clazz(e.getErrors().getObjectName())
                .message(e.toString())
                .fieldErrors(e.getFieldErrors())
                .build();
        
        ApiResponse data = ApiResponse.builder()
                            .check(false)
                            .information(response)
                            .build();

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ExceptionHandler(DefaultException.class)
    protected ResponseEntity<?> handleDefaultException(DefaultException e) {
        
        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse response = ErrorResponse
                .builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(e.toString())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.resolve(errorCode.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e) {
        
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.toString())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value())
                .message(e.getMessage())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    protected ResponseEntity<?> handleCustomAuthenticationException(CustomAuthenticationException e) {
        
        ErrorResponse response = ErrorResponse
                .builder()
                .status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value())
                .message(e.getMessage())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}