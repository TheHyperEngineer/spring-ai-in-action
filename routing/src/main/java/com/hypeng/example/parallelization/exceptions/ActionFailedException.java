package com.hypeng.example.parallelization.exceptions;

public class ActionFailedException extends RuntimeException {
    public ActionFailedException(String message) {
        super(message);
    }
}