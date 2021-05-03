package ru.otus.spring.controllers.errors;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ErrorMessage {
    private final int statusCode;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;
}

