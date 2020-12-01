package ru.otus.spring.util.testing;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.otus.spring.domain.testing.SimpleAnswer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.domain.testing.QuestionAnswer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Парсер вопросов
 */
public class QuestionParserImpl implements QuestionParser {
    private static final char DELIMITER = ',';
    private static final Integer START_ANSWER_INDEX = 1;

    @Override
    public List<Question> parse(final InputStream in) throws IOException, QuestionFormatException {
        CSVParser parser = CSVParser.parse(in, Charset.forName("UTF-8"), CSVFormat.RFC4180.withDelimiter(DELIMITER));
        return parser.getRecords().stream()
                .filter(row -> row.size() >= 2)
                .map(row -> this.parseQuestion(row))
                .collect(Collectors.toList());
    }

    private Question parseQuestion(final CSVRecord record) throws QuestionFormatException {
        //ожидается хотя бы один ответ на вопрос
        if (record.size() < 2)
            throw new QuestionFormatException(record.toString());

        try {
            final String questionText = record.get(0).strip();
            final List<QuestionAnswer> answers = parseAnswers(record);
            return new Question(questionText, answers);
        } catch (Exception e) {
            throw new QuestionFormatException(record.toString());
        }
    }

    private List<QuestionAnswer> parseAnswers(final CSVRecord record) throws QuestionFormatException {
        int answersCount = (record.size() - START_ANSWER_INDEX) / 2;
        if (!checkAnswerCount(record.size(), answersCount))
            throw new QuestionFormatException();

        List<QuestionAnswer> simpleAnswers = IntStream.range(START_ANSWER_INDEX, record.size())
                .filter(i -> i % 2 != 0)
                .mapToObj(i -> {
                    final String answerText = record.get(i).strip();
                    final String isCorrectAnswerText = record.get(i + 1).strip();
                    if (!checkBooleanString(isCorrectAnswerText))
                        throw new QuestionFormatException();

                    final Boolean isCorrectAnswer = Boolean.parseBoolean(isCorrectAnswerText);
                    return new QuestionAnswer(answerText, isCorrectAnswer);
                })
                .collect(Collectors.toList());
        return simpleAnswers;
    }

    private boolean checkAnswerCount(int actualElementsCount, int answerCount) {
        int expectedElementsCount = answerCount * 2 + START_ANSWER_INDEX;
        if (actualElementsCount == expectedElementsCount)
            return true;
        return false;
    }


    private Boolean checkBooleanString(String booleanString) {
        if (booleanString == null || booleanString.isEmpty())
            return false;
        final List<String> expectedBooleanValues = Arrays.asList("true", "false");

        if (expectedBooleanValues.indexOf(booleanString.toLowerCase()) == -1)
            return false;
        return true;
    }

}
