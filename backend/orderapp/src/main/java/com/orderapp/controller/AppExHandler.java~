package com.orderapp.controller;

import com.orderapp.dto.ErrorDetailsDto;
import com.orderapp.exceptions.FeignClientException;
import com.orderapp.exceptions.InsufficientStockException;
import com.orderapp.exceptions.OrderNotFoundException;
import com.orderapp.exceptions.OrderStatusUpdateException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class AppExHandler {
    @ExceptionHandler(FeignClientException.class)
    public ResponseEntity<ErrorDetailsDto> handleFeignErrors(FeignClientException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getErrorDetails());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDetailsDto> handleOrderNotFoundException(OrderNotFoundException ex) {
        ErrorDetailsDto errorDetails = ErrorDetailsDto.builder()
                .errorCode(404)
                .timestamp(java.time.LocalDateTime.now())
                .errorMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(OrderStatusUpdateException.class)
    public ResponseEntity<ErrorDetailsDto> handleOrderStatusUpdateException(OrderStatusUpdateException ex) {
        ErrorDetailsDto errorDetails = ErrorDetailsDto.builder()
                .errorCode(400)
                .timestamp(java.time.LocalDateTime.now())
                .errorMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorDetailsDto> handleInsufficientStock(InsufficientStockException ex) {
        ErrorDetailsDto errorDetails = ErrorDetailsDto.builder()
                .errorCode(409)
                .timestamp(java.time.LocalDateTime.now())
                .errorMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetailsDto> handle400(MethodArgumentNotValidException ex) {
        String errorMessage= ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorDetailsDto errorDetails=
                ErrorDetailsDto.builder().errorCode(400)
                        .timestamp(java.time.LocalDateTime.now())
                        .errorMessage(errorMessage).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
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
