package ru.otus.spring.domain.testing.results;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Результаты тестирования
 */
@ToString
public class TestResultsReport {
    /**
     * Тест сдан/не сдан
     */
    @Getter
    @NonNull
    private final Boolean isFailed;
    /**
     * Информация о тесте: студент, дата сдачи
     */
    @Getter
    @NonNull
    private final Header header;
    /**
     * Содержимое: вопросы, правильные/неправильные ответы
     */
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
