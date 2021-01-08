package ru.otus.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.config.props.QuestionsResourceProps;
import ru.otus.spring.dao.person.PersonDao;
import ru.otus.spring.dao.person.PersonDaoSimple;
import ru.otus.spring.dao.testing.QuestionDao;
import ru.otus.spring.dao.testing.QuestionDaoCsv;
import ru.otus.spring.dao.testing.TestDao;
import ru.otus.spring.dao.testing.TestDaoSimple;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.util.testing.QuestionParser;
import ru.otus.spring.util.testing.QuestionParserImpl;

@Configuration
public class DaoConfig {
    @Bean
    public QuestionParser questionParser() {
        return new QuestionParserImpl();
    }

    @Bean
    public QuestionDao questionDao(QuestionParser questionParser,
                                   QuestionsResourceProps questionsResourceProps) {
        return new QuestionDaoCsv(questionParser, questionsResourceProps);
    }

    @Bean(initMethod = "init")
    public PersonDao personDao() {
        return new PersonDaoSimple();
    }

    @Bean
    public TestDao testDao() {
        return new TestDaoSimple();
    }
}
