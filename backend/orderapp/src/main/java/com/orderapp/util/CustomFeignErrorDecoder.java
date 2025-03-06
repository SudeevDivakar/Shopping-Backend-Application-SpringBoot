package com.orderapp.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderapp.dto.ErrorDetailsDto;
import com.orderapp.exceptions.FeignClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String errorMessage = "Unknown error";
            int errorCode = response.status();

            if (response.body() != null) {
                String responseBody = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);

                // Try to parse the response body as JSON (in case the remote service returns a structured error)
                try {
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    errorMessage = jsonNode.has("errorMessage") ? jsonNode.get("errorMessage").asText() : responseBody;
                } catch (Exception ignored) {
                    errorMessage = responseBody; // If parsing fails, use raw response
                }
            }

            // Create ErrorDetailsDto
            ErrorDetailsDto errorDetails = ErrorDetailsDto.builder()
                    .errorCode(errorCode)
                    .timestamp(LocalDateTime.now())
                    .errorMessage(errorMessage)
                    .build();

            return new FeignClientException(errorDetails, HttpStatus.valueOf(errorCode));

        } catch (IOException e) {
            return new RuntimeException("Error decoding response", e);
        }
    }
}
