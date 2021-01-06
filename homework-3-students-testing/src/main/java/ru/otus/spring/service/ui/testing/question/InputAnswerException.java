package ru.otus.spring.service.ui.testing.question;

public class InputAnswerException extends RuntimeException{
    public InputAnswerException(Throwable cause) {
        super("Input answer error", cause);
    }
}
