package com.example.storeservice.controllers;

import com.example.storeservice.error.GeneralError;
import com.example.storeservice.error.IdsError;
import com.example.storeservice.exception.IdsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ExceptionsHandlerController {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public GeneralError handelGeneralException(Exception e) {
        return new GeneralError(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GeneralError handelNotFoundException(Exception e) {
        return new GeneralError(e.getMessage());
    }

    @ExceptionHandler(IdsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public IdsError handleNotFoundProducts(IdsException e) {
        return new IdsError(e.getMessage(), e.getIds());
    }
}
