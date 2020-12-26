package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ru.otus.spring.dao.person.PersonDao;
import ru.otus.spring.dao.person.PersonDaoSimple;
import ru.otus.spring.dao.testing.QuestionDao;
import ru.otus.spring.dao.testing.QuestionDaoCsv;
import ru.otus.spring.dao.testing.TestDao;
import ru.otus.spring.dao.testing.TestDaoSimple;
import ru.otus.spring.util.testing.QuestionParser;
import ru.otus.spring.util.testing.QuestionParserImpl;

@Configuration
public class DaoConfig {
    @Autowired
    private Environment env;

    @Bean
    public QuestionParser questionParser() {
        return new QuestionParserImpl();
    }

    @Bean
    public QuestionDao questionDao(QuestionParser questionParser) {
        return new QuestionDaoCsv(questionParser, env.getProperty("questions.csv"));
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
