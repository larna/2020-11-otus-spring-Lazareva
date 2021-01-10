package ru.otus.spring.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class LocalizationConfig {
    @Bean
    public MessageSource messageSource() {
        var ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:/i18n/messages");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }
}
