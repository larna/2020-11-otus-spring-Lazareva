package ru.otus.spring.dao.testing;

public class DuplicateTestException extends RuntimeException {

    public DuplicateTestException(String message) {
        super(String.format("Test is already exists %s", message));
    }
}
