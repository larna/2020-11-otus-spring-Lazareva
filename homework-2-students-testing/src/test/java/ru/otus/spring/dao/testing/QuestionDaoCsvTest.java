package ru.otus.spring.dao.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.domain.testing.Answer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.service.testing.TestContextConfig;
import ru.otus.spring.util.testing.QuestionParser;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Класс QuestionDaoCsv")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {QuestionDaoCsvTestContextConfig.class})
class QuestionDaoCsvTest {
    @Autowired
    private QuestionDao questionDao;

    @DisplayName("Возвращает 5 вопросов")
    @Test
    void findAll() throws Exception {
        final Integer expected = 5;
        assertThat(questionDao.findAll())
                .isNotNull()
                .hasSize(expected);
    }

}