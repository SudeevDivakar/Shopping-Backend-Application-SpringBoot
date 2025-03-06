package com.orderapp.exceptions;

import com.orderapp.dto.ErrorDetailsDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FeignClientException extends RuntimeException {
    private final ErrorDetailsDto errorDetails;
    private final HttpStatus status;

    public FeignClientException(ErrorDetailsDto errorDetails, HttpStatus status) {
        super(errorDetails.getErrorMessage());
        this.errorDetails = errorDetails;
        this.status = status;
    }
}
