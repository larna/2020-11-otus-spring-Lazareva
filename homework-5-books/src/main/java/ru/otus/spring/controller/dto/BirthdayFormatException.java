package ru.otus.spring.controller.dto;

import ru.otus.spring.services.authors.AuthorServiceException;

public class BirthdayFormatException extends AuthorServiceException {
    public BirthdayFormatException() {
        super("Ошибка ввода даты рождения. Формат: дд.мм.гггг");
    }
}
