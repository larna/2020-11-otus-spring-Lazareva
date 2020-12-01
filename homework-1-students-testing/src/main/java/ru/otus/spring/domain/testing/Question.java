package ru.otus.spring.domain.testing;

import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Класс описывает вопросы, которые будут заданы студентам при тестировании
 */
public class Question {
    /**
     * Собственно сам вопрос в виде текста
     */
    @Getter
    private final String question;
    /**
     * Список доступных ответов
     */
    private final List<QuestionAnswer> answers;

    public Question(String question, List<QuestionAnswer> accessibleForSelectAnswers) {
        this.question = question;
        this.answers = accessibleForSelectAnswers;
    }

    /**
     * Проверить правильность ответа
     *
     * @param checkingAnswer - проверяемый ответ
     * @return true - если ответ правильный, а иначе - false
     */
    public Boolean checkAnswer(Answer checkingAnswer) {

        Optional<QuestionAnswer> foundObject = this.answers.stream()
                .filter(answer -> checkingAnswer.getAnswer().equals(answer.getAnswer()))
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
        return answers.stream()
                .map(questionAnswer -> (Answer) questionAnswer)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question1 = (Question) o;

        if (!Objects.equals(question, question1.question)) return false;
        return Objects.equals(answers, question1.answers);
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
     * Сформировать строку для выбора ответа на вопрос
     *
     * @return ответы в виде строки с указанием порядкового номера, который будет выбирать пользователь
     */
    public String getAnswersStringForChoice() {
        if (answers.isEmpty())
            return "";
        return IntStream.range(0, answers.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, answers.get(i).getAnswer()))
                .reduce((s1, s2) -> s1 + " " + s2).orElse("");
    }

    /**
     * Требует ли ответ ввода значения или требует выбора из списка ответов
     *
     * @return true - требуется ввод, false - требуется выбор из списка
     */
    public Boolean isInputAnswer() {
        long wrongAnswerCount = answers.stream().filter(answer -> !answer.getIsRightAnswer()).count();
        return wrongAnswerCount == 0;
    }
}
