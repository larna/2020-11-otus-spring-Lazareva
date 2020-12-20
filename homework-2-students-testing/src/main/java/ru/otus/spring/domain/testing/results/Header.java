package ru.otus.spring.domain.testing.results;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.otus.spring.domain.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@ToString
public class Header {
    @Getter
    @NonNull
    private final Person person;
    @Getter
    @NonNull
    private final LocalDate date;

    public Header(@NonNull Person person,@NonNull LocalDate date) {
        this.person = person;
        this.date = date;
    }
}
