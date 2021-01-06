package ru.otus.spring.config.props;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Настройки, связанные с вопросами
 */
@Component
@ConfigurationProperties(prefix = "questions.csv")
@Setter
public class QuestionsResourceProps {
    private String path;
    private String prefixFileName;

    public String getQuestionsResourceByLocale(Locale locale) {
        String localePostfix = "en";
        if (locale != null) {
            localePostfix = locale.getLanguage();
        }
        return String.format("%s%s_%s.csv", path, prefixFileName, localePostfix);
    }
}
