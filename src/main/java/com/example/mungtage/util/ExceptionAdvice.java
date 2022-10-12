package com.example.mungtage.util;

import com.example.mungtage.util.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse handleNotFoundException(BadRequestException e) {
        return new ErrorResponse(List.of(e.getMessage()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse processValidationError(MethodArgumentNotValidException ex) {
        return new ErrorResponse(List.of(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }
}
