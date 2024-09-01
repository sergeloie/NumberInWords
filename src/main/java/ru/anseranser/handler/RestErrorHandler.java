package ru.anseranser.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.anseranser.exception.NumberOutOfBoundException;

import java.io.IOException;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NumberOutOfBoundException.class)
    public ResponseEntity<String> handleNumberOutOfBoundException(NumberOutOfBoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
