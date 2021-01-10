package ru.otus.spring.dao.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.config.props.QuestionsResourceProps;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.util.testing.QuestionParserImpl;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Класс QuestionDaoCsv")
@SpringBootTest
@ActiveProfiles("test")
class QuestionDaoCsvTest {
    @Configuration
    @EnableConfigurationProperties(QuestionsResourceProps.class)
    static class QuestionDaoCsvConfig {
        @Bean
        public QuestionDao questionDao(QuestionsResourceProps questionsResourceProps) {
            return new QuestionDaoCsv(new QuestionParserImpl(), questionsResourceProps);
        }
    }

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