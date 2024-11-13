package com.example.api;

public class ApiError extends RuntimeException{

    public ApiError(final String s) {
        super(s);
    }
}
