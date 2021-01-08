package ru.otus.spring.config.props;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс LocalizationProps")
class LocalizationPropsTest {
    @DisplayName("Если локаль не задана, то применяется en по-умолчанию")
    @Test
    void shouldEnglishLocaleIfNotDefinedInConfig() {
        LocalizationProps props = new LocalizationProps();
        assertEquals(Locale.ENGLISH, props.getLocale());
    }

}