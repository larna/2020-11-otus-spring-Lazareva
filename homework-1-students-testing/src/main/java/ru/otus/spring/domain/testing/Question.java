package ru.otus.spring.domain.testing;

import lombok.Getter;

import java.lang.reflect.AnnotatedWildcardType;
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

    public Boolean checkAnswer(Answer checkingAnswer) {

        Optional<QuestionAnswer> foundObject = this.answers.stream()
                .peek(answer->System.out.println(answer.getAnswer() + " " +checkingAnswer.equals(answer)))
                .filter(answer -> checkingAnswer.equals(answer))
                .findFirst();
        if (foundObject.isPresent())
            return foundObject.get().getIsRightAnswer();

        return false;
    }

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

    public Answer getAnswerByIndex(Integer index) {
        if (answers.size() <= index)
            throw new ArrayIndexOutOfBoundsException();
        return answers.get(index);
    }

    public String getAnswerForChoice() {
        if (answers.isEmpty())
            return "";
        return IntStream.range(0, answers.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, answers.get(i).getAnswer()))
                .reduce((s1, s2) -> s1 + " " + s2).orElse("");
    }
}
