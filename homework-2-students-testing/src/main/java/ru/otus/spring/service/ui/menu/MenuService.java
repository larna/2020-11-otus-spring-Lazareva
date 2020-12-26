package ru.otus.spring.service.ui.menu;

public interface MenuService {
    enum Command {CONTINUE, EXIT}

    ;

    void printMenu();

    Command getCommand();
}
