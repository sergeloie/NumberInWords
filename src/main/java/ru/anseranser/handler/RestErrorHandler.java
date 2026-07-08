package ru.anseranser.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.anseranser.exception.NumberOutOfBoundException;

@RestControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(NumberOutOfBoundException.class)
    public ResponseEntity<String> handleNumberOutOfBoundException(NumberOutOfBoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
