package ru.otus.spring.service.ui.testing;

import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import ru.otus.spring.dao.testing.QuestionDao;
import ru.otus.spring.dao.testing.QuestionDaoCsv;
import ru.otus.spring.dao.testing.TestDao;
import ru.otus.spring.dao.testing.TestDaoSimple;
import ru.otus.spring.service.io.IOService;
import ru.otus.spring.service.testing.QuestionService;
import ru.otus.spring.service.testing.QuestionServiceImpl;
import ru.otus.spring.service.testing.TestService;
import ru.otus.spring.service.testing.TestServiceImpl;
import ru.otus.spring.service.ui.testing.question.AskQuestionHandler;
import ru.otus.spring.util.testing.QuestionParser;
import ru.otus.spring.util.testing.QuestionParserImpl;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "ru.otus.spring.service", lazyInit = true)
@PropertySource("classpath:application.properties")
public class TestProcessContextConfig {
    @Bean
    @Primary
    IOService ioService() {
        return Mockito.mock(IOService.class);
    }

    @Bean
    @Primary
    TestProcessService testProcessService(IOService ioService, TestService testService) {
        return new TestProcessServiceImpl(ioService, testService, askQuestionServices());
    }

    @Bean
    @Primary
    TestDao testDao() {
        return new TestDaoSimple();
    }

    @Bean
    @Primary
    TestService testService(QuestionService questionService, TestDao testDao, @Value("${test.pass.percent}") Integer passPercent) {
        return new TestServiceImpl(questionService, testDao, passPercent);
    }

    List<AskQuestionHandler> askQuestionServices() {
        return List.of(new AskQuestionHandlerMock());
    }
}