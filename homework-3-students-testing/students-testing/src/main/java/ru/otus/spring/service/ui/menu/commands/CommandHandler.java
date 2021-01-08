package ru.otus.spring.service.ui.menu.commands;

public interface CommandHandler {
    void handle(String userChoice);
    Boolean isSuitableCommand(String userChoice);
    Boolean isContinue();
}
