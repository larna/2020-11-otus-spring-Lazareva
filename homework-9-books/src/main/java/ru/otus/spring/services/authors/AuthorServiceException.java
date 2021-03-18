package ru.otus.spring.services.authors;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthorServiceException extends RuntimeException {
    public AuthorServiceException(String msg) {
    }
}
