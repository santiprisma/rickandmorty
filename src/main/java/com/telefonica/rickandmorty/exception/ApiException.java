package com.telefonica.rickandmorty.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
public class ApiException extends RuntimeException {
    private HttpStatus code;
    private String error;
}
