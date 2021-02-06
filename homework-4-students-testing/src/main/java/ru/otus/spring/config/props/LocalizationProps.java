package ru.otus.spring.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/**
 * Настройки приложения связанные с локализацией
 */
@Component
@ConfigurationProperties(prefix = "application")
@Setter
public class LocalizationProps {
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    private volatile Locale locale;
    @Getter
    private List<String> accessibleLocales;

    public Locale getLocale() {
        return locale == null ? DEFAULT_LOCALE : locale;
    }
}
