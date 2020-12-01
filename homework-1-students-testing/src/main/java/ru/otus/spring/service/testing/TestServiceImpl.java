package ru.otus.spring.service.testing;

import ru.otus.spring.dao.testing.TestDao;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.SimpleAnswer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.domain.testing.StudentTest;

import java.time.LocalDate;
import java.util.List;

public class TestServiceImpl implements TestService {
    private final QuestionService questionService;
    private final TestDao dao;

    public TestServiceImpl(QuestionService questionService, TestDao dao) {
        this.questionService = questionService;
        this.dao = dao;
    }

    @Override
    public StudentTest createNewTest(Person student) {
        List<Question> questions = questionService.getQuestions();
        return dao.createNewTest(student, questions);
    }

    @Override
    public StudentTest getTestByStudentAndDate(Person student, LocalDate date) {
        return null;
    }

    /**
     * Добавление ответа в тест студента
     * @param test
     * @param studentAnswer
     */
    @Override
    public void addAnswerToTest(StudentTest test, Answer studentAnswer) {
        //создавать здесь answer наверное неправильно и для ответов нужен свой сервис?
        //Тем более у меня их уже два типа - ответы студента и ответы на вопросы с указанием правильно/не правильно
        test.addAnswer(studentAnswer);
    }

    @Override
    public String getTestResults(StudentTest test) {
        return test.getResults();
    }
}
