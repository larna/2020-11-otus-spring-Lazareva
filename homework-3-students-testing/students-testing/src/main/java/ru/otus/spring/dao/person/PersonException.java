package ru.otus.spring.dao.person;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PersonException extends RuntimeException{
    public PersonException(String message) {
        super(message);
    }
}
