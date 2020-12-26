package ru.otus.spring.dao.testing;

public class ResourceAccessException extends RuntimeException {

    public ResourceAccessException() {
        super("Access denied to resource or resource not found!");
    }
}
