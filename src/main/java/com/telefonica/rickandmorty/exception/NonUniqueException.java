package com.telefonica.rickandmorty.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NonUniqueException extends RuntimeException {
    private HttpStatus code;
    private String error;
}
