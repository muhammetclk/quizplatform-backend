package com.quizplatform.quizservice.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;


    public BusinessException(String message,HttpStatus httpStatus) {
        super(message);
        this.status=httpStatus;
    }
    
}
