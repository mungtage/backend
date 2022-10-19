package com.example.mungtage.util.exception;

import java.util.function.Supplier;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message){
        super(message);
    }


}
