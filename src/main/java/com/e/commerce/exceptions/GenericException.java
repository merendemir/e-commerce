package com.e.commerce.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class GenericException extends RuntimeException {

    private HttpStatus httpStatus;

    private String errorMessage;
}
