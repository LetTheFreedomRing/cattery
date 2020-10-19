package com.example.cattery.exceptions;

public class CatAlreadyInWishlistException extends RuntimeException {
    public CatAlreadyInWishlistException() {
    }

    public CatAlreadyInWishlistException(String message) {
        super(message);
    }

    public CatAlreadyInWishlistException(String message, Throwable cause) {
        super(message, cause);
    }
}
