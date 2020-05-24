package com.spiritual.junction.iyfAPI.exception;

public class UnAuthorizeException extends RuntimeException {

    public UnAuthorizeException(String exceptionMessage){
        super(exceptionMessage.isEmpty() ? "Unauthorized access" : exceptionMessage);
    }
}
