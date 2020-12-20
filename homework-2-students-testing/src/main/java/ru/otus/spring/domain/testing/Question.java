package ru.otus.spring.domain.testing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Класс описывает вопросы, которые будут заданы студентам при тестировании
 */
@EqualsAndHashCode
public class Question {
    /**
     * Собственно сам вопрос в виде текста
     */
    @Getter
    @NonNull
    private final String question;
    /**
     * Список доступных ответов
     */
    @NonNull
    private final List<Answer> answers;

    public Question(@NonNull String question, @NonNull List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    /**
     * Проверить правильность ответа
     *
     * @param checkingAnswer - проверяемый ответ
     * @return true - если ответ правильный, а иначе - false
     */
    public Boolean checkAnswer(String checkingAnswer) {
        Optional<Answer> foundObject = this.answers.stream()
                .filter(answer -> answer.getAnswer().equalsIgnoreCase(checkingAnswer))
                .findFirst();
        if (foundObject.isPresent())
            return foundObject.get().getIsRightAnswer();

        return false;
    }

    /**
     * Получить список всех возможных ответов на вопрос
     *
     * @return список возможных ответов
     */
    public List<Answer> getAnswers() {
        return this.answers;
    }

    /**
     * Получить ответ по номеру
     *
     * @param number - порядковый номер вопроса в списке
     * @return объект Answer
     * @throws IllegalArgumentException - в случае если номер не попадает в границы массива
     */
    public Answer getAnswerByNumber(Integer number) throws IllegalArgumentException {
        if (answers.size() < number)
            throw new IllegalArgumentException();
        return answers.get(number - 1);
    }

    /**
     * Получить список правильных ответов
     *
     * @return список Answer
     */
    public List<Answer> getRightAnswers() {
        return answers.stream().filter(answer -> answer.getIsRightAnswer()).collect(Collectors.toList());
    }
}
