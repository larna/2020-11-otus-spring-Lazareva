package ru.otus.spring.service.ui.login;

public class IllegalFormatStudentNameException extends RuntimeException{
    public IllegalFormatStudentNameException(String message) {
        super(message);
    }
}
