package ru.otus.spring.controller.events;

public interface EventsPublisher {
    void publish(Object... event);
}
