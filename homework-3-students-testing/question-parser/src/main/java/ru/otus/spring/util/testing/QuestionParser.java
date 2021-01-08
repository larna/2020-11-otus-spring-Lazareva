package ru.otus.spring.util.testing;

import ru.otus.spring.domain.testing.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Интерфейс парсера вопросов
 */
public interface QuestionParser {
    /**
     * Разобрать список вопросов с ответами
     * @param in - поток из ресурса
     * @return список вопросов со списком ответов
     * @throws QuestionFormatException если в файле встречены строки строки
     * @throws IOException input/output errors
     */
    List<Question> parse(InputStream in) throws QuestionFormatException, IOException;
}
