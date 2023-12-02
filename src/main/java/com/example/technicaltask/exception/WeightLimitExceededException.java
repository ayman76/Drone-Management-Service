package com.example.technicaltask.exception;

public class WeightLimitExceededException extends RuntimeException {
    public WeightLimitExceededException(String message) {
        super(message);
    }
}
