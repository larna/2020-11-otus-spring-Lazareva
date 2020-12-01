package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.dao.PersonNotFoundException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.domain.testing.SimpleAnswer;

import ru.otus.spring.domain.testing.StudentTest;
import ru.otus.spring.service.PersonService;
import ru.otus.spring.service.testing.TestService;

import java.util.Scanner;

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

            for (Question question : test.getQuestions()) {
                System.out.println(question.getQuestion());
                if (question.isInputAnswer()) {
                    System.out.println("Enter your answer:");
                } else {
                    System.out.println("Enter number of choice answer:");
                    System.out.println(question.getAnswersStringForChoice());
                }

                Answer studentAnswer;
                String studentInputAnswerText = "";
                try {
                    studentInputAnswerText = console.nextLine();
                    if (question.isInputAnswer()) {
                        studentAnswer = new SimpleAnswer(studentInputAnswerText);
                    } else {
                        int number = Integer.parseInt(studentInputAnswerText);
                        studentAnswer = question.getAnswerByNumber(number);
                    }
                } catch (Exception e) {
                    studentAnswer = new SimpleAnswer("Не удалось определить ответ. Вы ввели: " + studentInputAnswerText);
                }
                testService.addAnswerToTest(test, studentAnswer);
            }

            System.out.println();
            System.out.println("********************************");
            System.out.println(testService.getTestResults(test));
            System.out.println("********************************");
            System.out.println();

        } catch (
                PersonNotFoundException e) {
            System.out.println("Извините, пользователь с таким именем не найден");
        }

    }
}
