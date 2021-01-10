package ru.otus.spring.util.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConditionalOnClass(QuestionParser.class)
@EnableConfigurationProperties(QuestionParserProps.class)
public class QuestionParserAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(QuestionParserAutoConfiguration.class);

    private static final char DELIMITER_DEFAULT = ',';
    private static final Integer START_ANSWER_IDX_DEFAULT = 1;
    private static final Integer MINIMAL_TOKEN_IN_ROW_DEFAULT = 3;
    private static final Map<Boolean, String> TRUE_FALSE_VALUES_DEFAULT = Map.of(true, "true", false, "false");

    private final QuestionParserProps props;

    public QuestionParserAutoConfiguration(QuestionParserProps questionParserProps) {
        this.props = questionParserProps;
    }

    @Bean
    @ConditionalOnMissingBean
    public QuestionParserConfig questionParserConfig() {

        Character delimiter = props.getDelimiter() == null ? DELIMITER_DEFAULT : props.getDelimiter();
        Integer startAnswerIndex = props.getStartAnswerIndex() == null ? START_ANSWER_IDX_DEFAULT : props.getStartAnswerIndex();
        Integer minTokenInRow = props.getMinimalTokenCountInRow() == null ? MINIMAL_TOKEN_IN_ROW_DEFAULT : props.getMinimalTokenCountInRow();
        Map<Boolean, String> trueFalseValuesMap = props.getExpectedTrueFalseValues() == null ? TRUE_FALSE_VALUES_DEFAULT : props.getExpectedTrueFalseValues();
        QuestionParserConfig config = new QuestionParserConfig(delimiter, startAnswerIndex, minTokenInRow, trueFalseValuesMap);
        logger.info("AutoConfig. creating QuestionParserConfig, default message:{}", config);
        return config;
    }

    @Bean
    @ConditionalOnMissingBean
    public QuestionParser questionParser(QuestionParserConfig questionParserConfig) {
        logger.info("AutoConfig. creating QuestionParser");
        return new QuestionParserImpl(questionParserConfig);
    }
}
