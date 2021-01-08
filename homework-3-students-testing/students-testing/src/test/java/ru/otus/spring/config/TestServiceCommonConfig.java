package ru.otus.spring.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.spring.config.props.QuestionsResourceProps;
import ru.otus.spring.config.props.TestProcessProps;
import ru.otus.spring.dao.testing.QuestionDao;
import ru.otus.spring.dao.testing.TestDao;
import ru.otus.spring.dao.testing.TestDaoSimple;
import ru.otus.spring.service.testing.QuestionService;
import ru.otus.spring.service.testing.QuestionServiceImpl;

@TestConfiguration
@EnableConfigurationProperties({QuestionsResourceProps.class, TestProcessProps.class})
@ComponentScan(basePackages = {"ru.otus.spring.service", "ru.otus.spring.dao"}, lazyInit = true)
public class TestServiceCommonConfig {
    @Bean
    public QuestionService questionService(QuestionDao questionDao) {
        return new QuestionServiceImpl(questionDao);
    }

    @Bean
    public TestDao testDao() {
        return new TestDaoSimple();
    }
}
