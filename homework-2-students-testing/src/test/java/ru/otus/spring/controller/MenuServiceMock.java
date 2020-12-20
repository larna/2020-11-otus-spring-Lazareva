package ru.otus.spring.controller;

import ru.otus.spring.service.ui.menu.MenuService;

import java.util.concurrent.atomic.AtomicInteger;

public class MenuServiceMock implements MenuService {
    private AtomicInteger counter = new AtomicInteger(1);

    @Override
    public void printMenu() {
    }

    @Override
    public Command getCommand() {
        return counter.getAndDecrement() > 0 ? Command.CONTINUE : Command.EXIT;
    }
}
