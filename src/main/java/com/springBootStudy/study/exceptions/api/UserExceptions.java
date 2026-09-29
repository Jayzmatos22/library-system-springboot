package com.springBootStudy.study.exceptions.api;

public class UserExceptions extends RuntimeException {
    private final int status;

    public UserExceptions(String message, int status){
        super(message);
        this.status = status;
    }
}
