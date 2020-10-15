package com.example.cattery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CatNotAvailableForSaleException extends RuntimeException {

    public CatNotAvailableForSaleException() {
        super("Unfortunately, this cat is not available for sale for now");
    }
}
