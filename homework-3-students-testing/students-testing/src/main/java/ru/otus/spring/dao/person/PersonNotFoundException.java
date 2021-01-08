package ru.otus.spring.dao.person;

public class PersonNotFoundException extends PersonException {
    public PersonNotFoundException(String message) {
        super(String.format("Person not found %s", message));
    }
}
