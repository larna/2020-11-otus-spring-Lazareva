package ru.otus.spring.service.testing;

import ru.otus.spring.dao.testing.TestDao;
import ru.otus.spring.dao.testing.TestNotFoundException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.domain.testing.StudentTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Интерфейс сервиса Тестов
 */
public class TestServiceImpl implements TestService {
    private final QuestionService questionService;
    private final TestDao dao;

    public TestServiceImpl(QuestionService questionService, TestDao dao) {
        this.questionService = questionService;
        this.dao = dao;
    }

    /**
     * Создать новый тест
     *
     * @param student объект студента
     * @return новый объект теста
     * @throws DuplicateTestException если тест за указанную дату для этого студента уже существует
     */
    @Override
    public StudentTest createNewTest(Person student) throws DuplicateTestException {
        LocalDate testDate = LocalDate.now();
        if (getTestByStudentAndDate(student, testDate) != null)
            throw new DuplicateTestException(String.format("(%s %s)",
                    student.getName(), testDate.format(DateTimeFormatter.ISO_DATE)));

        List<Question> questions = questionService.getQuestions();
        StudentTest test = new StudentTest(testDate, student, questions);
        dao.save(test);
        return test;
    }

    /**
     * Получить тест для указанного студента за указанную дату
     *
     * @param student - объект студента
     * @param date    - дата прохождения теста
     * @return объект тест
     */
    @Override
    public StudentTest getTestByStudentAndDate(Person student, LocalDate date) {
        try {
            return dao.findByStudentAndDate(student, date);
        } catch (TestNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Добавить новый ответ к тесту
     *
     * @param test          объекь тест
     * @param studentAnswer ответ студента
     */
    @Override
    public void addAnswerToTest(StudentTest test, Answer studentAnswer) {
        test.addAnswer(studentAnswer);
        //тут dao ничего не делает, потому что объект в ОП, и предыдущей строкой мы уже все добавили:)
        //но если бы была база то там мы сохранили бы изменения.
        dao.save(test);
    }

    /**
     * Получить результаты тест
     *
     * @param test объект тест
     * @return результаты теста в виде строки
     */
    @Override
    public String getTestResults(StudentTest test) {
        return test.getResults();
    }
}
