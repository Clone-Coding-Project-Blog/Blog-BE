package com.mizzle.blogrest.advice.assertThat;

import java.util.List;
import java.util.Optional;

import com.mizzle.blogrest.advice.error.DefaultException;
import com.mizzle.blogrest.advice.error.InvalidParameterException;
import com.mizzle.blogrest.advice.payload.ErrorCode;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAssert extends Assert{
    public static void isValidParameter(Errors errors){
        if(errors.hasErrors()){
            throw new InvalidParameterException(errors);
        }
    }

    public static void isListNull(List<Object> values){
        if(values.isEmpty()){
            throw new DefaultException(ErrorCode.INVALID_FILE_PATH);
        }
    }

    public static void isListNull(Object[] values){
        if(values == null){
            throw new DefaultException(ErrorCode.INVALID_FILE_PATH);
        }
    }

    public static void isOptionalPresent(Optional<?> value){
        if(!value.isPresent()){
            throw new DefaultException(ErrorCode.INVALID_PARAMETER);
        }
    }

}
