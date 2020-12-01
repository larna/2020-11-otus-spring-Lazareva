package ru.otus.spring.service.testing;

import ru.otus.spring.domain.testing.StudentTest;

public class DuplicateTestException extends RuntimeException {
    public DuplicateTestException(String message) {
        super(String.format("test already exists %s", message));
    }
}
