package ru.otus.spring.domain.testing.results;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@ToString
public class TestResultsReport {
    @Getter
    @NonNull
    private final Boolean isFailed;
    @Getter
    @NonNull
    private final Header header;
    @Getter
    @NonNull
    private final List<ReportRow> rows;

    public TestResultsReport(@NonNull Boolean isFailed, @NonNull Header header) {
        this.isFailed = isFailed;
        this.header = header;
        this.rows = new ArrayList<>();
    }

    public void addRow(@NonNull ReportRow row) {
        rows.add(row);
    }

}
