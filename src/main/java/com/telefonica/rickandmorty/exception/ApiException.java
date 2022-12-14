package com.telefonica.rickandmorty.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiException extends RuntimeException {
    private String endpoint;
    private HttpStatus code;
    private String error;
}
