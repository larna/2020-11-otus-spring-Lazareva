package ru.otus.spring.service.testing;

public class DuplicateTestException extends RuntimeException {
    public DuplicateTestException(String message) {
        super(String.format("test already exists %s", message));
    }
}
