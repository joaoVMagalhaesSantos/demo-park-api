package com.magalhaes.demoparkapi.entity.exception;

public class PasswordInvalidException extends RuntimeException{
    public PasswordInvalidException(String message){
        super(message);
    }
}
