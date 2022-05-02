package com.mizzle.blogrest.config.security.error;

import com.mizzle.blogrest.advice.payload.ErrorCode;

import org.springframework.security.core.AuthenticationException;

import lombok.Getter;


@Getter
public class CustomAuthenticationException extends AuthenticationException{

    private ErrorCode errorCode;

    public CustomAuthenticationException(String msg, Throwable t) {
        super(msg, t);
        this.errorCode = ErrorCode.INVALID_REPRESENTATION;
    }

    public CustomAuthenticationException(String msg) {
        super(msg);
    }

    public CustomAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
