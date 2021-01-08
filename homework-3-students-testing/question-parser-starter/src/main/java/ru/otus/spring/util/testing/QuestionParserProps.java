package ru.otus.spring.util.testing;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix="question-parser")
@Setter
@Getter
public class QuestionParserProps {
    private Character delimiter;
    private Integer startAnswerIndex;
    private Integer minimalTokenCountInRow;
    private Map<Boolean,String> expectedTrueFalseValues;
}
