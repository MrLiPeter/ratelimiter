package com.lxh.exception;

public class RateLimitException extends Exception{

    public RateLimitException(String message){
        super(message);
    }

}
