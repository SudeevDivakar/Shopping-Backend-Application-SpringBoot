package com.inventoryapp.controller;

import com.inventoryapp.exceptions.IncorrectValueException;
import com.inventoryapp.dto.ErrorDetailsDto;
import com.inventoryapp.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class AppExHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDetailsDto> handle404(ProductNotFoundException ex) {
        ErrorDetailsDto errorDetails=
                ErrorDetailsDto.builder().errorCode(404)
                        .timestamp(java.time.LocalDateTime.now())
                        .errorMessage(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(IncorrectValueException.class)
    public ResponseEntity<ErrorDetailsDto> handle400(IncorrectValueException ex) {
        ErrorDetailsDto errorDetails=
                ErrorDetailsDto.builder().errorCode(400)
                        .timestamp(java.time.LocalDateTime.now())
                        .errorMessage(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailsDto> handle500(Exception ex){
        ErrorDetailsDto errorDetails=
                ErrorDetailsDto.builder().errorCode(500)
                        .timestamp(java.time.LocalDateTime.now())
                        .errorMessage(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }
}