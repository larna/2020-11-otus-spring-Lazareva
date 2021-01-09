package ru.otus.spring.shell.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.otus.spring.domain.Person;

/**
 * Событие тестирования студента
 */
public class TestEvent extends ApplicationEvent {

    @Getter
    private final Person student;

    public TestEvent(Object source, Person student) {
        super(source);
        this.student = student;
    }
}
