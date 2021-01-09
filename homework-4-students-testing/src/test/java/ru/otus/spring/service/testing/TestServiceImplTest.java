package ru.otus.spring.service.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.otus.spring.dao.testing.*;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.StudentTest;
import ru.otus.spring.domain.testing.results.TestResultsReport;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.io.IOService;
import ru.otus.spring.service.ui.testing.AskQuestionHandlerMock;
import ru.otus.spring.service.ui.testing.TestProcessService;
import ru.otus.spring.service.ui.testing.TestProcessServiceImpl;
import ru.otus.spring.service.ui.testing.question.AskQuestionHandler;
import ru.otus.spring.util.testing.QuestionParserImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Класс TestService")
@SpringBootTest
@ActiveProfiles("test")
@MockBean(IOService.class)
class TestServiceImplTest {
    @Configuration
    @EnableConfigurationProperties({QuestionsResourceProps.class, TestProcessProps.class})
    @ComponentScan(basePackages = {"ru.otus.spring.service", "ru.otus.spring.dao"}, lazyInit = true)
    static class TestProcessServiceConfig{
//        @Bean
//        public QuestionDao questionDao(QuestionsResourceProps questionsResourceProps) {
//            return new QuestionDaoCsv(new QuestionParserImpl(), questionsResourceProps);
//        }
        @Bean
        public QuestionService questionService(QuestionDao questionDao) {
            return new QuestionServiceImpl(questionDao);
        }

        @Bean
        public TestDao testDao() {
            return new TestDaoSimple();
        }
    }
    @Autowired
    private TestService testService;
    @MockBean
    private LocalizationService localizationService;
    @MockBean
    private TestDao testDao;
    @Autowired
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        when(localizationService.getCurrentLocale()).thenReturn(Locale.forLanguageTag("en"));
    }

    @DisplayName("Корректно создает новый тест для студента")
    @Test
    void createNewTest() {
        given(testDao.findByStudentAndDate(any(), any()))
                .willThrow(new TestNotFoundException("test"));
        testService.createNewTest(new Person("Test", "Ivan", 19));

        verify(testDao, times(1)).findByStudentAndDate(any(), any());
        verify(testDao, times(1)).save(any());
    }

    @DisplayName("Выбрасывает исключение DuplicateTestException если тест уже есть")
    @Test
    void shouldThrowExceptionIfTestAlreadyExists() {
        List<Question> questions = questionService.getQuestions();
        given(testDao.findByStudentAndDate(any(), any()))
                .willReturn(new StudentTest(LocalDate.now(), new Person("Test", "Ivan", 19), questions));

        Exception exception = assertThrows(DuplicateTestException.class,
                () -> testService.createNewTest(new Person("Test", "Ivan", 19)));
        assertTrue(exception.getMessage().contains("test already exist"));

        verify(testDao, times(1)).findByStudentAndDate(any(), any());
        verify(testDao, times(0)).save(any());
    }

    @DisplayName("Возвращает найденный тест")
    @Test
    void getTestByStudentAndDate() {
        List<Answer> answers = Arrays.asList(new Answer("answer", true));
        List<Question> questions = Arrays.asList(new Question("Question?", answers));
        StudentTest test = new StudentTest(LocalDate.now(), new Person("Testov", "Ivan", 19), questions);
        given(testDao.findByStudentAndDate(any(), any()))
                .willReturn(test);

        assertEquals(test, testService.getTestByStudentAndDate(new Person("Testov", "Ivan", 19), LocalDate.now()));
        verify(testDao, times(1)).findByStudentAndDate(any(), any());
    }

    @DisplayName("Возвращает null если тест не найден")
    @Test
    void shouldThrowExceptionIfTestNotFound() {

        given(testDao.findByStudentAndDate(any(), any()))
                .willThrow(new TestNotFoundException("test"));

        assertNull(testService.getTestByStudentAndDate(new Person("Test", "Ivan", 19), LocalDate.now()));
    }

    @DisplayName("Корректно формирует результаты тестирования")
    @ParameterizedTest
    @CsvSource(value = {"60, false, Test done", "80, true, Test failed"})
    void shouldHaveCorrectGetTestResults(Integer passPercent, Boolean expectedIsFailed, String description) {
        TestService testService = new TestServiceImpl(questionService, testDao, passPercent);
        StudentTest test = testService.createNewTest(new Person("Testov", "Ivan", 19));
        test.addAnswer(new Answer("1", true));
        test.addAnswer(new Answer("1", true));
        test.addAnswer(new Answer("82", true));
        test.addAnswer(new Answer("2", false));
        test.addAnswer(new Answer("2", false));
        TestResultsReport resultsReport = testService.getTestResults(test);
        Boolean actualIsFailed = resultsReport.getIsFailed();
        assertEquals(expectedIsFailed, actualIsFailed);
    }
}