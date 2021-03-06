package ru.otus.spring.service.ui.login;

public class AccessDeniedException extends RuntimeException {
    private static final String MESSAGE = "Такой пользователь не найден. Доступ запрещен.";

    public AccessDeniedException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
