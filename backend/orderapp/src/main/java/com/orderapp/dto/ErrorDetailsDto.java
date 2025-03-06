package com.orderapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorDetailsDto {
    private String errorMessage;
    private LocalDateTime timestamp;
    private int errorCode;
}
