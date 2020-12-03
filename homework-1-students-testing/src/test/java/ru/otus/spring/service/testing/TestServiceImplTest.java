package ru.otus.spring.service.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.otus.spring.dao.testing.TestDao;
import ru.otus.spring.dao.testing.TestNotFoundException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.domain.testing.QuestionAnswer;
import ru.otus.spring.domain.testing.StudentTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Класс TestService")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestServiceImplTest {
    private TestService service;
    @Mock
    private TestDao testDao;
    @Mock
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        service = new TestServiceImpl(questionService, testDao);
    }

    @DisplayName("Корректно создает новый тест для студента")
    @Test
    void createNewTest() {
        List<Question> questions = Arrays.asList(new Question("Question?", Arrays.asList(new QuestionAnswer("answer", true))));
        given(questionService.getQuestions())
                .willReturn(questions);

        given(testDao.findByStudentAndDate(any(), any()))
                .willThrow(new TestNotFoundException("test"));
        service.createNewTest(new Person("Test", 19));

        verify(testDao, times(1)).findByStudentAndDate(any(), any());
        verify(testDao, times(1)).save(any());
    }

    @DisplayName("Выбрасывает исключение DuplicateTestException если тест уже есть")
    @Test
    void shouldThrowExceptionIfTestAlreadyExists() {
        List<QuestionAnswer> answers = Arrays.asList(new QuestionAnswer("answer", true));
        List<Question> questions = Arrays.asList(new Question("Question?", answers));
        given(questionService.getQuestions())
                .willReturn(questions);

        given(testDao.findByStudentAndDate(any(), any()))
                .willReturn(new StudentTest(LocalDate.now(), new Person("Test", 19), questions));

        Exception exception = assertThrows(DuplicateTestException.class,
                () -> service.createNewTest(new Person("Test", 19)));
        assertTrue(exception.getMessage().contains("test already exist"));

        verify(testDao, times(1)).findByStudentAndDate(any(), any());
        verify(testDao, times(0)).save(any());
    }

    @DisplayName("Возвращает найденый тест")
    @Test
    void getTestByStudentAndDate() {
        List<QuestionAnswer> answers = Arrays.asList(new QuestionAnswer("answer", true));
        List<Question> questions = Arrays.asList(new Question("Question?", answers));
        StudentTest test = new StudentTest(LocalDate.now(), new Person("Testov", 19), questions);
        given(testDao.findByStudentAndDate(any(), any()))
                .willReturn(test);

        assertEquals(test, service.getTestByStudentAndDate(new Person("Testov", 19), LocalDate.now()));
        verify(testDao, times(1)).findByStudentAndDate(any(), any());
    }

    @DisplayName("Возвращает null если тест не найден")
    @Test
    void shouldThrowExceptionIfTestNotFound() {

        given(testDao.findByStudentAndDate(any(), any()))
                .willThrow(new TestNotFoundException("test"));

        assertNull(service.getTestByStudentAndDate(new Person("Test", 19), LocalDate.now()));
    }

}