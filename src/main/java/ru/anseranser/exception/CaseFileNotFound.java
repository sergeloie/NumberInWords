package ru.anseranser.exception;

public class CaseFileNotFound extends RuntimeException {
    public CaseFileNotFound(String message) {
        super(message);
    }
}
