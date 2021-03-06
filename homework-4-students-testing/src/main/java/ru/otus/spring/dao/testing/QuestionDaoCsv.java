package ru.otus.spring.dao.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.spring.config.props.QuestionsResourceProps;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.util.testing.QuestionParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Класс для доступа к объектам Question
 */
public class QuestionDaoCsv implements QuestionDao {
    private static final Logger logger = LoggerFactory.getLogger(QuestionDaoCsv.class);
    private final QuestionParser parser;
    private final QuestionsResourceProps questionsResource;

    public QuestionDaoCsv(QuestionParser parser,
                          QuestionsResourceProps questionsResourceProps) {
        this.parser = parser;
        this.questionsResource = questionsResourceProps;
    }

    /**
     * Метод findAll перечитывает файл каждый раз когда к нему обращаются
     * Вдруг за прошедшее время с последнего обращения файлик поменяли и там другие вопросы или ответы:)
     *
     * @return список вопросов из ресурса
     */
    @Override
    public List<Question> findAll() {

        String questionsResource = this.questionsResource.getQuestionsResource();
        try (InputStream inResource = getClass().getResourceAsStream(questionsResource)) {
            return parser.parse(inResource);
        } catch (IOException | IllegalArgumentException e) {
            logger.error("IO error of {}", questionsResource, e);
            return null;
        }
    }
}
