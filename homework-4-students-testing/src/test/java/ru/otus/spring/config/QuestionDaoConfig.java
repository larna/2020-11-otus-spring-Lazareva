package ru.otus.spring.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.otus.spring.config.props.QuestionsResourceProps;
import ru.otus.spring.dao.testing.QuestionDao;
import ru.otus.spring.dao.testing.QuestionDaoCsv;
import ru.otus.spring.util.testing.QuestionParserImpl;

@TestConfiguration
@EnableConfigurationProperties(QuestionsResourceProps.class)
public class QuestionDaoConfig {
    @Bean
    public QuestionDao questionDao(QuestionsResourceProps questionsResourceProps) {
        return new QuestionDaoCsv(new QuestionParserImpl(), questionsResourceProps);
    }
}
