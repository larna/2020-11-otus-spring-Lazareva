package ru.otus.spring.util.testing;

import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Парсер вопросов
 * Вопрос и ответы содержаться вместе. В качестве разделителя используется символ ,
 * Формат строки вопроса:
 * Question?, answer1, true|false, answer2, true|false ... , answerN, true|false
 */
@AllArgsConstructor
public class QuestionParserImpl implements QuestionParser {
    private final QuestionParserConfig config;

    @Override
    public List<Question> parse(final InputStream in) throws IOException, QuestionFormatException {
        CSVParser parser = CSVParser.parse(in, StandardCharsets.UTF_8, CSVFormat.RFC4180.withDelimiter(config.getDelimiter()));

        final List<CSVRecord> records = parser.getRecords();
        long wrongFormatRowCount = records.stream()
                .filter(row -> row.size() >= 1 && row.size() < config.getMinimalTokenCountInRow())//должно быть не меньше 3 токенов
                .filter(row -> row.size() == 1 && !row.get(0).isEmpty()) //отсекаем пустые строки из неправильного формата
                .count();
        if (wrongFormatRowCount > 0) {
            String message = records.stream()
                    .filter(row -> row.size() >= 1 && row.size() < config.getMinimalTokenCountInRow())
                    .map(Objects::toString)
                    .reduce((s1, s2) -> s1 + " " + s2).orElse("");
            throw new QuestionFormatException(message);
        }

        return records.stream()
                .filter(row -> row.size() >= config.getMinimalTokenCountInRow())//отсекаем пустые строки
                .map(row -> parseQuestion(row))
                .collect(Collectors.toList());
    }

    private Question parseQuestion(final CSVRecord record) throws QuestionFormatException {
        //ожидается хотя бы один ответ на вопрос
        if (record.size() < 2)
            throw new QuestionFormatException(record.toString());

        try {
            final String questionText = record.get(0).strip();
            final List<Answer> answers = parseAnswers(record);
            return new Question(questionText, answers);
        } catch (Exception e) {
            throw new QuestionFormatException(record.toString());
        }
    }

    private List<Answer> parseAnswers(final CSVRecord record) throws QuestionFormatException {
        int answersCount = (record.size() - config.getStartAnswerIndex()) / 2;
        if (!checkAnswerCount(record.size(), answersCount))
            throw new QuestionFormatException();

        return IntStream.range(config.getStartAnswerIndex(), record.size())
                .filter(i -> i % 2 != 0)
                .mapToObj(i -> {
                    final String answerText = record.get(i).strip();
                    final String isCorrectAnswerText = record.get(i + 1).strip();
                    if (!config.checkCorrectnessAnswerValue(isCorrectAnswerText))
                        throw new QuestionFormatException();

                    final Boolean isCorrectAnswer = config.getCorrectnessAnswer(isCorrectAnswerText);
                    return new Answer(answerText, isCorrectAnswer);
                })
                .collect(Collectors.toList());
    }

    private boolean checkAnswerCount(int actualElementsCount, int answerCount) {
        int expectedElementsCount = answerCount * 2 + config.getStartAnswerIndex();
        return actualElementsCount == expectedElementsCount;
    }
}
