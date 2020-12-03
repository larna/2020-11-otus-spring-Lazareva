package ru.otus.spring.domain.testing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import ru.otus.spring.domain.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Класс описывает тест студента
 */
@EqualsAndHashCode(exclude = {"questions", "answers"})
public class StudentTest {
    /**
     * Дата проведения теста
     */
    @Getter
    @NonNull
    private final LocalDate testDate;
    /**
     * Студент
     */
    @Getter
    @NonNull
    private final Person student;
    /**
     * Список вопросов
     */
    @Getter
    @NonNull
    private final List<Question> questions;
    /**
     * Список ответов
     */
    @Getter
    @NonNull
    private final List<Answer> answers;

    public StudentTest(LocalDate testDate, Person student, List<Question> questions) {
        this.testDate = testDate;
        this.student = student;
        this.questions = questions;
        this.answers = new ArrayList<>();
    }

    /**
     * Добавить ответ студента в тест
     *
     * @param studentAnswer - объект ответ от студента
     */
    public void addAnswer(Answer studentAnswer) {
        answers.add(studentAnswer);
    }

    /**
     * Сформировать строку с результатом теста
     * Наверное это можно каким то отдельным классом нужно сделать TestResult ( тест, общая отметка, рекомендации )
     *
     * @return строка с информацией о результатах теста
     */
    public String getResults() {
        String testHeader = String.format("Test results. Student: %s. Date: %s\n\n", student.getName(), testDate.format(DateTimeFormatter.ISO_DATE));
        StringBuilder testBody = new StringBuilder();

        IntStream.range(0, questions.size()).forEach(i -> {
            Question question = questions.get(i);
            testBody.append(i + 1).append(". ");
            testBody.append(question.getQuestion()).append("\n");

            Answer studentAnswer = answers.get(i);
            testBody.append("Your answer - ").append(studentAnswer.getAnswer()).append("\n");
            testBody.append("It is ").append(question.checkAnswer(studentAnswer) ? "right" : "wrong").append("\n\n");
        });
        return testHeader + testBody.toString();
    }

}
