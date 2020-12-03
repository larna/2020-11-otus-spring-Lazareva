package ru.otus.spring.controller;

import ru.otus.spring.dao.PersonNotFoundException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.domain.testing.SimpleAnswer;
import ru.otus.spring.domain.testing.StudentTest;
import ru.otus.spring.service.PersonService;
import ru.otus.spring.service.testing.TestService;

import java.util.Scanner;

/**
 * Класс обслуживающий консольный интерфейс
 */
public class TestControllerConsole implements TestController {
    private final PersonService personService;
    private final TestService testService;

    public TestControllerConsole(PersonService personService, TestService testService) {
        this.personService = personService;
        this.testService = testService;
    }

    /**
     * Функция проведения тестирования студентов
     */
    public void testing() {
        initPersons();
        try (Scanner console = new Scanner(System.in)) {
            Person student = login(console);
            StudentTest test = testStudent(student, console);
            printTestResult(test);
        } catch (
                PersonNotFoundException e) {
            System.out.println("Извините, пользователь с таким именем не найден");
        }
    }

    /**
     * Инициализация списка студентов, допущенных к тестированию
     */
    private void initPersons() {
        personService.save(new Person("Ivanov", 18));
    }

    /**
     * Вход для студента в систему тестирования
     * @param console - поток ввода
     * @return объект студент
     * @throws PersonNotFoundException выбрасывается если студент не найден
     */
    private Person login(Scanner console) throws PersonNotFoundException {
        System.out.println("Enter your surname:");
        return personService.getByName(console.nextLine());
    }

    /**
     * Сам процесс тестирования - задавание вопросов и получение ответов
     * @param student - студент
     * @param console - поток ввода
     * @return заполненный тест
     */
    private StudentTest testStudent(Person student, Scanner console) {
        StudentTest test = testService.createNewTest(student);

        for (Question question : test.getQuestions()) {
            Answer studentAnswer = askQuestion(question, console);
            testService.addAnswerToTest(test, studentAnswer);
        }
        return test;
    }

    /**
     * Опрашивание студента - задает один вопрос и ожидает ввод/выбор ответа
     * @param question объект вопрос
     * @param console поток ввода
     * @return ответ полученный от студента
     */
    private Answer askQuestion(Question question, Scanner console) {
        System.out.println(question.getQuestion());
        if (question.isInputAnswer()) {
            return getInputAnswer(console);
        }
        return getChoiceAnswer(question, console);
    }

    /**
     * Получить ответ введенный с клавиатуры
     * @param console  поток ввода
     * @return ответ студента
     */
    private Answer getInputAnswer(Scanner console) {
        System.out.println("Enter your answer:");
        String studentInputAnswerText = console.nextLine();
        return new SimpleAnswer(studentInputAnswerText);
    }

    /**
     * Получить ответ выбранный из списка
     * @param question  вопрос
     * @param console  поток ввода
     * @return ответ студента
     */
    private Answer getChoiceAnswer(Question question, Scanner console) {
        System.out.println("Enter number of choice answer:");
        System.out.println(question.getAnswersStringForChoice());
        Answer studentAnswer;
        String studentInputAnswerText = "";
        try {
            studentInputAnswerText = console.nextLine();
            int number = Integer.parseInt(studentInputAnswerText);
            studentAnswer = question.getAnswerByNumber(number);
        } catch (Exception e) {
            studentAnswer = new SimpleAnswer("Не удалось определить ответ. Вы ввели: " + studentInputAnswerText);
        }
        return studentAnswer;
    }

    /**
     * Вывод результатов теста
     * @param test объект тест
     */
    private void printTestResult(StudentTest test) {
        System.out.println();
        System.out.println("********************************");
        System.out.println(testService.getTestResults(test));
        System.out.println("********************************");
        System.out.println();
    }

}
