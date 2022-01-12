package ru.epam.spring.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView notFoundError(Exception ex) {
        ModelAndView error = new ModelAndView("error", HttpStatus.INTERNAL_SERVER_ERROR);
        error.addObject("exception", ex.getMessage());
        return error;
    }
}
