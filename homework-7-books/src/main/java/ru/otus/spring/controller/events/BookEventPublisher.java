package ru.otus.spring.controller.events;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Book;

@Service
@RequiredArgsConstructor
public class BookEventPublisher implements EventsPublisher {
    private static Logger logger = LoggerFactory.getLogger(BookEventPublisher.class);

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(Object... events) {
        if (events == null || events.length > 1 ||
                events[0] == null ||
                !(events[0] instanceof Book)) {
            logger.warn("Event object is not instance of Book");
            return;
        }
        publisher.publishEvent(new BookEvent(this, (Book) events[0]));
    }
}
