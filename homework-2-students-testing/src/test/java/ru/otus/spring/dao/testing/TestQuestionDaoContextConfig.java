package ru.otus.spring.dao.testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import ru.otus.spring.util.testing.QuestionParserImpl;

@Configuration
@PropertySource("classpath:application.properties")
public class TestQuestionDaoContextConfig {
    @Autowired
    Environment env;

    @Bean
    public QuestionDao questionDao(){
        return new QuestionDaoCsv(new QuestionParserImpl(), env.getProperty("questions.csv"));
    }
}