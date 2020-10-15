package com.example.cattery.exceptions;

public class BreedAlreadyExistException extends RuntimeException {

    public BreedAlreadyExistException() {
    }

    public BreedAlreadyExistException(String message) {
        super(message);
    }

    public BreedAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
