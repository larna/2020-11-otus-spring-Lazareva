package ru.otus.spring.service.testing;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import ru.otus.spring.dao.testing.QuestionDao;
import ru.otus.spring.dao.testing.QuestionDaoCsv;
import ru.otus.spring.dao.testing.TestDao;
import ru.otus.spring.service.io.IOService;
import ru.otus.spring.util.testing.QuestionParserImpl;

@Configuration
@PropertySource("classpath:application.properties")
public class TestContextConfig {
    @Bean
    TestDao testDao() {
        return Mockito.mock(TestDao.class);
    }

    @Bean
    QuestionService questionService(QuestionDao questionDao) {
        return new QuestionServiceImpl(questionDao);
    }

    @Bean
    TestService testService(QuestionService questionService, TestDao testDao, @Value("${test.pass.percent}") Integer percent) {
        return new TestServiceImpl(questionService, testDao, percent);
    }

    @Bean
    @Primary
    QuestionDao questionDao(@Value("${questions.csv}") String questionResource) {
        return new QuestionDaoCsv(new QuestionParserImpl(), questionResource);
    }
}