package ru.otus.spring.controller;

public class BirthdayFormatException extends RuntimeException {
    public BirthdayFormatException() {
        super("Ошибка ввода даты рождения. Формат: дд.мм.гггг");
    }
}
