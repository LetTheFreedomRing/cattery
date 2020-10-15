package com.example.cattery.controller;

import com.example.cattery.exceptions.CatNotAvailableForSaleException;
import com.example.cattery.exceptions.NotFoundException;
import com.stripe.exception.StripeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(NotFoundException ex) {
        log.error("Handling NotFoundException exception : " + ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/404error");
        modelAndView.addObject("exception", ex.getMessage());

        return modelAndView;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFound(NoHandlerFoundException ex) {
        log.error("Handling handleNoHandlerFound exception : " + ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/404error");
        modelAndView.addObject("exception", "Page not found");

        return modelAndView;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CatNotAvailableForSaleException.class)
    public ModelAndView handleCatNotAvailableForSale(CatNotAvailableForSaleException ex) {
        log.error("Handling CatNotAvailableForSaleException exception : " + ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/400error");
        modelAndView.addObject("exception", ex.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(StripeException.class)
    public ModelAndView handleStripeException(StripeException ex) {
        log.error("Handling CatNotAvailableForSaleException exception : " + ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cat/paymentResult");
        modelAndView.addObject("error", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleUnknownException(RuntimeException ex) {
        log.error("Handling RuntimeException exception : " + ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/500error");
        return modelAndView;
    }
}
