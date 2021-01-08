package ru.otus.spring.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.spring.config.props.QuestionsResourceProps;
import ru.otus.spring.dao.testing.QuestionDao;
import ru.otus.spring.dao.testing.QuestionDaoCsv;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.util.testing.QuestionParser;
import ru.otus.spring.util.testing.QuestionParserConfig;
import ru.otus.spring.util.testing.QuestionParserImpl;
import ru.otus.spring.util.testing.QuestionParserProps;

import java.util.Map;

@TestConfiguration
@EnableConfigurationProperties({QuestionsResourceProps.class})
@ComponentScan(basePackageClasses = QuestionParser.class)
public class QuestionDaoConfig {
//    @Bean QuestionParser questionParser(){
//        QuestionParserConfig config = new QuestionParserConfig(',', 1, 3, Map.of(true,"true", false, "false"));
//        QuestionParser questionParser = new QuestionParserImpl(config);
//        return questionParser;
//    }
    @Bean
    public QuestionDao questionDao(QuestionParser questionParser,
                                   QuestionsResourceProps questionsResourceProps) {
        return new QuestionDaoCsv(questionParser, questionsResourceProps);
    }
}
