package ru.otus.spring.service.ui.testing;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import ru.otus.spring.config.props.QuestionsResourceProps;
import ru.otus.spring.config.props.TestProps;
import ru.otus.spring.dao.testing.QuestionDao;
import ru.otus.spring.dao.testing.TestDao;
import ru.otus.spring.dao.testing.TestDaoSimple;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.io.IOService;
import ru.otus.spring.service.testing.QuestionService;
import ru.otus.spring.service.testing.QuestionServiceImpl;
import ru.otus.spring.service.testing.TestService;
import ru.otus.spring.service.ui.testing.question.AskQuestionHandler;

import java.util.List;

@TestConfiguration
@EnableConfigurationProperties({QuestionsResourceProps.class, TestProps.class})
@ComponentScan(basePackages = {"ru.otus.spring.service", "ru.otus.spring.dao"}, lazyInit = true)
public class TestProcessContextConfig {
    @Bean
    @Primary
    public TestProcessService testProcessServiceInTest(IOService ioService,
                                                       TestService testService,
                                                       LocalizationService localizationService) {
        return new TestProcessServiceImpl(ioService, testService, askQuestionServices(), localizationService);
    }

    public List<AskQuestionHandler> askQuestionServices() {
        return List.of(new AskQuestionHandlerMock());
    }
}