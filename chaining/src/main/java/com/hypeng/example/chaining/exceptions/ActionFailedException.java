package com.hypeng.example.chaining.exceptions;

public class ActionFailedException extends RuntimeException {
    public ActionFailedException(String message) {
        super(message);
    }
}