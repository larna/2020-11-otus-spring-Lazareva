package ru.otus.spring.util.testing;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
@Getter
@ToString
public class QuestionParserConfig {
    private final char delimiter;
    private final Integer startAnswerIndex;
    private final Integer minimalTokenCountInRow;
    private final Map<Boolean,String> expectedTrueFalseValues;

    public QuestionParserConfig(char delimiter, Integer startAnswerIndex, Integer minimalTokenCountInRow, Map<Boolean, String> expectedTrueFalseValues) {
        this.delimiter = delimiter;
        this.startAnswerIndex = startAnswerIndex;
        this.minimalTokenCountInRow = minimalTokenCountInRow;
        this.expectedTrueFalseValues = expectedTrueFalseValues;
    }

    public Boolean checkCorrectnessAnswerValue(final String correctnessAnswer){
        return expectedTrueFalseValues.values().stream()
                .filter(value->value.equalsIgnoreCase(correctnessAnswer))
                .count() > 0;
    }
    public Boolean getCorrectnessAnswer(final String correctnessAnswer){
        return expectedTrueFalseValues.keySet().stream()
                .filter(key->expectedTrueFalseValues.get(key).equalsIgnoreCase(correctnessAnswer))
                .findFirst()
                .orElse(false);
    }
}
