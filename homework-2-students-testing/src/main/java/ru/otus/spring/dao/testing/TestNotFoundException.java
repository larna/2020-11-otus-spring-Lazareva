package ru.otus.spring.dao.testing;

public class TestNotFoundException extends RuntimeException {

    public TestNotFoundException(String message) {
        super(String.format("Test not found %s", message));
    }

}
