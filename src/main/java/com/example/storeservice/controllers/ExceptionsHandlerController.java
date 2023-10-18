package com.example.storeservice.controllers;

import com.example.storeservice.error.GeneralError;
import com.example.storeservice.error.IdsError;
import com.example.storeservice.exception.IdsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;


@ControllerAdvice
public class ExceptionsHandlerController {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody GeneralError handelGeneralException(Exception e) {
        return GeneralError.generateGeneralError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody GeneralError handelNotFoundException(Exception e) {
        return GeneralError.generateGeneralError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody GeneralError handleConstraintViolationException(ConstraintViolationException e) {
        String message = ((ConstraintViolation<?>) e.getConstraintViolations().toArray()[0]).getMessage();
        return GeneralError.generateGeneralError(HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody GeneralError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return GeneralError.generateGeneralError(HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(IdsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody IdsError handleNotFoundProducts(IdsException e) {
        return IdsError.generateIdsError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e.getIds());
    }
}
