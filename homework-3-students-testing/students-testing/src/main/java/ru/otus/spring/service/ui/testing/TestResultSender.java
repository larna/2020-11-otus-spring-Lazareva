package ru.otus.spring.service.ui.testing;

import ru.otus.spring.domain.testing.results.TestResultsReport;

/**
 * Интерфейс для вывода результатов тестирования
 * Их можно вывести в консоль, отправить по e-mail, в файл в разных форматах...
 */
public interface TestResultSender {
    /**
     * Метод отправки результатов
     *
     * @param result - объект с результатами тестирования
     */
    void send(TestResultsReport result);
}
