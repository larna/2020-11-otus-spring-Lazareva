package ru.otus.spring.service.ui.login;

public class IllegalFormatStudentNameException extends LoginException{
    public IllegalFormatStudentNameException(String message) {
        super(message);
    }
}
