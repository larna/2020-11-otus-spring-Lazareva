package ru.otus.spring;

//import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.dao.PersonNotFoundException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.SimpleAnswer;
import ru.otus.spring.domain.testing.StudentTest;
import ru.otus.spring.service.PersonService;
import ru.otus.spring.service.testing.TestService;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        PersonService personService = context.getBean(PersonService.class);
        TestService testService = context.getBean(TestService.class);

        personService.save(new Person("Ivanov", 18));

        try (Scanner console = new Scanner(System.in)) {

            System.out.println("Enter your surname:");
            Person student = personService.getByName(console.nextLine());
            StudentTest test = testService.createNewTest(student);

            test.getQuestions().stream().forEach(question -> {
                System.out.println(question.getQuestion());
                String studentAnswer = "";
                if (question.getAnswers().size() == 1) {
                    System.out.println("Enter your answer:");
                    studentAnswer = console.nextLine();
                    testService.addAnswerToTest(test, new SimpleAnswer(studentAnswer));
                } else {
                    Answer choiceAnswer = null;
                    System.out.println("Enter number of choice answer:");
                    System.out.println(question.getAnswerForChoice());
                    try {
                        int number = Integer.parseInt(console.nextLine());
                        choiceAnswer = question.getAnswerByIndex(number - 1);
                    } catch (NumberFormatException e) {
                        choiceAnswer = new SimpleAnswer("Не удалось распознать номер ответ");
                    }
                    testService.addAnswerToTest(test, choiceAnswer);
                }
            });
            System.out.println();
            System.out.println("********************************");
            System.out.println(testService.getTestResults(test));
        } catch (PersonNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
}
