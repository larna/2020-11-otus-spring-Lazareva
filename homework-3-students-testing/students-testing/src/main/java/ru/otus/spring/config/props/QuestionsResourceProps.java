package ru.otus.spring.config.props;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.i18n.LocalizationService;

import java.util.Locale;

@Component
@ConfigurationProperties(prefix = "questions.csv")
@Setter
public class QuestionsResourceProps {
    private String path;
    private String prefixFileName;
    private final LocalizationService localizationService;

    public QuestionsResourceProps(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    public String getQuestionsResource() {
        String localePostfix = "en";
        Locale locale = localizationService.getCurrentLocale();
        if (locale != null) {
            localePostfix = locale.getLanguage();
        }
        return String.format("%s%s_%s.csv", path, prefixFileName, localePostfix);
    }
}
