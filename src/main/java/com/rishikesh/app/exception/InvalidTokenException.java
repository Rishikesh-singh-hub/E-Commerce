package com.rishikesh.app.exception;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String msg){
        super(msg);
    }

}
