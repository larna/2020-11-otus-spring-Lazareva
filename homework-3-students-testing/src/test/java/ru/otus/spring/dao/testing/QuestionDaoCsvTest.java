package ru.otus.spring.dao.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.QuestionDaoConfig;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.util.testing.QuestionParser;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Класс QuestionDaoCsv")
@SpringBootTest(classes = QuestionDaoConfig.class)
@ActiveProfiles("test")
class QuestionDaoCsvTest {
    @Autowired
    private QuestionDao questionDao;
    @MockBean
    private LocalizationService localizationService;

    @BeforeEach
    void setUp() {
        when(localizationService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
    }

    @DisplayName("Возвращает 5 вопросов")
    @Test
    void findAll() throws Exception {
        final Integer expected = 5;
        assertThat(questionDao.findAll())
                .isNotNull()
                .hasSize(expected);
    }

}