package com.telefonica.rickandmorty.controller;

import com.telefonica.rickandmorty.exception.ApiException;
import com.telefonica.rickandmorty.exception.NonUniqueException;
import com.telefonica.rickandmorty.model.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ErrorResponse handleApiException(ApiException exception) {
        log.error("ApiException - Endpoint {} - Status Code: {}", exception.getEndpoint(), exception.getCode());

        return ErrorResponse.builder()
                .status(exception.getCode().value())
                .description(exception.getError())
                .build();
    }
    @ExceptionHandler(NonUniqueException.class)
    public ErrorResponse handleNonUniqueException(NonUniqueException exception) {
        log.error("ApiException - Error {} - Status Code: {}", exception.getError(), exception.getCode());

        return ErrorResponse.builder()
                .status(exception.getCode().value())
                .description(exception.getError())
                .build();
    }
}
