package ru.otus.spring.domain.testing.results;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;

import java.util.List;

@ToString
public class ReportRow {
    @Getter
    @NonNull
    private final Question question;
    @Getter
    @NonNull
    private final Answer studentAnswer;

    public ReportRow(@NonNull Question question, @NonNull Answer studentAnswer) {
        this.question = question;
        this.studentAnswer = studentAnswer;
    }

}
