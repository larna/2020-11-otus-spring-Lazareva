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
import ru.otus.spring.util.testing.QuestionParserImpl;

@TestConfiguration
@EnableConfigurationProperties(QuestionsResourceProps.class)
public class QuestionDaoConfig {
    @Bean
    public QuestionDao questionDao(QuestionsResourceProps questionsResourceProps,
                                   LocalizationService localizationService) {
        return new QuestionDaoCsv(new QuestionParserImpl(), questionsResourceProps, localizationService);
    }
}
