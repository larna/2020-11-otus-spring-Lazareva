package ru.otus.spring.shell.events;

public interface EventsPublisher {
    void publish(Object... event);
}
