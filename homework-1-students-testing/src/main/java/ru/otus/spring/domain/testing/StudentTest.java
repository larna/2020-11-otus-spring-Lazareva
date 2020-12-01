package ru.otus.spring.domain.testing;

import lombok.Getter;
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
public class StudentTest {
    /**
     * Дата проведения теста
     */
    @Getter
    private final LocalDate testDate;
    /**
     * Студент
     */
    @Getter
    private final Person student;
    /**
     * Список вопросов
     */
    @Getter
    private final List<Question> questions;
    /**
     * Список ответов
     */
    @Getter
    private final List<Answer> answers;

    public StudentTest(LocalDate testDate, Person student, List<Question> questions) {
        this.testDate = testDate;
        this.student = student;
        this.questions = questions;
        this.answers = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentTest test = (StudentTest) o;

        if (!Objects.equals(testDate, test.testDate)) return false;
        return Objects.equals(student, test.student);
    }

    public void addAnswer(Answer studentAnswer) {
        answers.add(studentAnswer);
    }

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
