package com.example.icaller_mobile.common.utils;

public class Error {
    private ErrorType type;
    private String message;

    public Error(ErrorType type, String message) {
        this.type = type;
        this.message = message;
    }

    public ErrorType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public enum ErrorType{
        email,
        password
    }
}
