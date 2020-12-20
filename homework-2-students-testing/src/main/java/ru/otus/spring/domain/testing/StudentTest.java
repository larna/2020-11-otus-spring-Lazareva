package ru.otus.spring.domain.testing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.testing.results.Header;
import ru.otus.spring.domain.testing.results.ReportRow;
import ru.otus.spring.domain.testing.results.TestResultsReport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Класс описывает тест студента
 * Содержит список вопросов, полученных на них от студента ответов,
 * информацию о студенте и дате прохождения теста.
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

    public StudentTest(@NonNull LocalDate testDate, @NonNull Person student, @NonNull List<Question> questions) {
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

    public Boolean isComplete() {
        return this.answers.size() == this.questions.size();
    }

    public Integer getFactPercentPassTest() {
        if (questions.size() == 0)
            return 0;

        double rightAnswerCount = answers.stream().filter(answer -> answer.getIsRightAnswer()).count();
        double allQuestionCount = questions.size();

        int percent = Long.valueOf(Math.round((rightAnswerCount / allQuestionCount) * 100)).intValue();
        return percent;
    }
}
