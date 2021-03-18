package com.ilek.homework.exception;

public class InvalidIsinException extends Exception {

    public InvalidIsinException(String message) {
        super(message);
    }

    public InvalidIsinException(String message, Throwable cause) {
        super(message, cause);
    }
}
