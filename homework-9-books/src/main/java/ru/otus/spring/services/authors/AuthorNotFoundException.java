package ru.otus.spring.services.authors;

public class AuthorNotFoundException extends AuthorServiceException {
    public AuthorNotFoundException() {
        super("Автор не найден");
    }
}
