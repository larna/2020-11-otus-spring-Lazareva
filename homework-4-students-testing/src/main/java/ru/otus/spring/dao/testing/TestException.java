package ru.otus.spring.dao.testing;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TestException extends RuntimeException  {
    public TestException(String message) {
        super(message);
    }
}
