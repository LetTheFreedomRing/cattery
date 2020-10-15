package com.example.cattery.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CrashController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ModelAndView modelAndView = new ModelAndView();
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                modelAndView.setViewName("errors/404error");
                modelAndView.addObject("exception", "Page was not found!");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                modelAndView.setViewName("errors/500error");
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                modelAndView.setViewName("errors/400error");
                modelAndView.addObject("exception", "Sorry, an error has occurred during your request!");
            } else {
                modelAndView.setViewName("errors/unknown");
            }
        } else {
            modelAndView.setViewName("errors/unknown");
        }
        return modelAndView;
    }


    @Override
    public String getErrorPath() {
        // deprecated in this version, but we need to implement this method, so just return null
        return null;
    }
}
