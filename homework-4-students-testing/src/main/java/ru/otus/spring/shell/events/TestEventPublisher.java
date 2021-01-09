package ru.otus.spring.shell.events;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Person;

@Service
@RequiredArgsConstructor
public class TestEventPublisher implements EventsPublisher {
    private static Logger logger = LoggerFactory.getLogger(TestEventPublisher.class);

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(Object... events) {
        if (events == null || events.length > 1 ||
                events[0] == null ||
                !(events[0] instanceof Person)) {
            logger.warn("Event object is not instance of Person");
            return;
        }
        publisher.publishEvent(new TestEvent(this, (Person) events[0]));
    }
}
