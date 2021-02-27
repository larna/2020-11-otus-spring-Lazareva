package ru.otus.spring.controller.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.otus.spring.domain.Book;

/**
 * Событие тестирования студента
 */
public class BookEvent extends ApplicationEvent {

    @Getter
    private final Book book;

    public BookEvent(Object source, Book book) {
        super(source);
        this.book = book;
    }
}
