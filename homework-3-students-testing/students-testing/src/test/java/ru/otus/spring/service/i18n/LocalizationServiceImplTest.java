package ru.otus.spring.service.i18n;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.LocalizationConfig;
import ru.otus.spring.config.QuestionDaoConfig;
import ru.otus.spring.config.TestServiceCommonConfig;
import ru.otus.spring.config.props.LocalizationProps;
import ru.otus.spring.service.io.IOService;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс LocalizationService")
@SpringBootTest(classes = {LocalizationConfig.class})
@ActiveProfiles("test")
class LocalizationServiceImplTest {

    @Autowired
    private MessageSource messageSource;

    @DisplayName("Корректно получает строку из соответствующего bundle")
    @ParameterizedTest
    @CsvSource(value = {"ru, Меню", "en, Menu", "de, Speisekarte"})
    void shouldGetCorrectMessage(String languageTag, String expectedMessage) {
        Locale locale = Locale.forLanguageTag(languageTag);
        LocalizationProps props = new LocalizationProps();
        props.setLocale(locale);
        LocalizationService localizationService = new LocalizationServiceImpl(messageSource, props);
        String actualMessage = localizationService.getMessage("test.menu.title");
        assertEquals(expectedMessage, actualMessage);
    }
}