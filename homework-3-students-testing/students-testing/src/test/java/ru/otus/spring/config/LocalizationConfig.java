package ru.otus.spring.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.spring.config.props.LocalizationProps;
import ru.otus.spring.config.props.QuestionsResourceProps;
import ru.otus.spring.config.props.TestProcessProps;

@TestConfiguration
public class LocalizationConfig {
    @Bean
    public MessageSource messageSource() {
        var ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:/i18n/messages");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }
}
