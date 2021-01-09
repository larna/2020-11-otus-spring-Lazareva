package ru.otus.spring.service.ui.testing;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.config.QuestionDaoConfig;
import ru.otus.spring.config.TestServiceCommonConfig;
import ru.otus.spring.config.props.QuestionsResourceProps;
import ru.otus.spring.config.props.TestProcessProps;
import ru.otus.spring.dao.testing.QuestionDao;
import ru.otus.spring.dao.testing.QuestionDaoCsv;
import ru.otus.spring.dao.testing.TestDao;
import ru.otus.spring.dao.testing.TestDaoSimple;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.results.TestResultsReport;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.io.IOService;
import ru.otus.spring.service.testing.QuestionService;
import ru.otus.spring.service.testing.QuestionServiceImpl;
import ru.otus.spring.service.testing.TestService;
import ru.otus.spring.service.ui.testing.question.AskQuestionHandler;
import ru.otus.spring.util.testing.QuestionParserImpl;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@DisplayName("Класс TestProcessService")
@SpringBootTest
@ActiveProfiles("test")
class TestProcessServiceImplTest {
    @Configuration
    @EnableConfigurationProperties({QuestionsResourceProps.class, TestProcessProps.class})
    @ComponentScan(basePackages = {"ru.otus.spring.service", "ru.otus.spring.dao"}, lazyInit = true)
    static class TestProcessServiceConfig{
        @Bean
        @Primary
        public TestProcessService testProcessServiceInTest(IOService ioService,
                                                           TestService testService,
                                                           TestProcessProps testProcessProps) {
            return new TestProcessServiceImpl(ioService, testService, askQuestionServices(), testProcessProps);
        }

        public List<AskQuestionHandler> askQuestionServices() {
            return List.of(new AskQuestionHandlerMock());
        }
//        @Bean
//        public QuestionDao questionDao(QuestionsResourceProps questionsResourceProps) {
//            return new QuestionDaoCsv(new QuestionParserImpl(), questionsResourceProps);
//        }
//        @Bean
//        public QuestionService questionService(QuestionDao questionDao) {
//            return new QuestionServiceImpl(questionDao);
//        }
//
//        @Bean
//        public TestDao testDao() {
//            return new TestDaoSimple();
//        }
    }
    @MockBean
    private IOService ioService;
    @Autowired
    @Qualifier("testProcessServiceInTest")
    private TestProcessService testProcessService;
    @MockBean
    private LocalizationService localizationService;


    private static Person student = new Person("Ivanov", "Ivan", 18);
    private static final Integer expectedResultRows = 5;
    private Condition<TestResultsReport> testDone = new Condition<TestResultsReport>((report) -> !report.getIsFailed(), "Тест пройден");

    private Condition<TestResultsReport> hasFiveRows = new Condition<TestResultsReport>((report) -> report.getRows().size() == expectedResultRows,
            "Результат содержит 5 строк");

    @BeforeEach
    void setUp() {
        when(localizationService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
    }

    @DisplayName("Корректно проводит тестирование студента и возвращает результат тестирования")
    @Test
    void shouldCorrectTestStudent() {
        Predicate<TestResultsReport> allAnswerIsRight = (report) -> report.getRows().stream()
                .filter(row -> row.getStudentAnswer().getIsRightAnswer()).count() == expectedResultRows;
        Condition<TestResultsReport> rightAnswerInTest = new Condition<TestResultsReport>(allAnswerIsRight, "Все ответы правильные");

        TestResultsReport actualResultsReport = testProcessService.testStudent(student);
        assertThat(actualResultsReport)
                .isNotNull()
                .has(testDone)
                .has(hasFiveRows)
                .has(rightAnswerInTest)
                .extracting("header.person").isEqualTo(student);
    }

    @DisplayName("Возвращает результаты если тест в этот день уже пройден")
    @Test
    void shouldHaveReturnResultIfTestAlreadyExists() {
        Predicate<TestResultsReport> allAnswerIsRight = (report) -> report.getRows().stream()
                .filter(row -> row.getStudentAnswer().getIsRightAnswer()).count() == expectedResultRows;
        Condition<TestResultsReport> rightAnswerInTest = new Condition<TestResultsReport>(allAnswerIsRight, "Все ответы правильные");

        testProcessService.testStudent(student);

        when(localizationService.getMessage(eq("test.process.messages.comeback"))).thenReturn("Pardon");

        TestResultsReport actualResultsReport = testProcessService.testStudent(student);
        assertThat(actualResultsReport)
                .isNotNull()
                .has(testDone)
                .has(hasFiveRows)
                .has(rightAnswerInTest)
                .extracting("header.person").isEqualTo(student);
        verify(ioService, times(1)).outMessage("Pardon");
    }
}