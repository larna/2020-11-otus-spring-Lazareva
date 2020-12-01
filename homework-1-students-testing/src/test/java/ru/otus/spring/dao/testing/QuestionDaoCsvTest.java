package ru.otus.spring.dao.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.testing.QuestionAnswer;
import ru.otus.spring.domain.testing.Question;
import ru.otus.spring.util.testing.QuestionParser;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Класс QuestionDaoCsv")
@ExtendWith(MockitoExtension.class)
class QuestionDaoCsvTest {

    private QuestionDao questionDao;
    @Mock
    private QuestionParser parser;

    @BeforeEach
    void setUp() {
        questionDao = new QuestionDaoCsv(parser, "test-resource.txt");
    }

    @DisplayName("Возвращает 5 вопросов")
    @Test
    void findAll() throws Exception {
        given(parser.parse(any()))
                .willReturn(Arrays.asList(
                        new Question("Question1", Arrays.asList(new QuestionAnswer("answer", true))),
                        new Question("Question2", Arrays.asList(new QuestionAnswer("answer", true))),
                        new Question("Question3", Arrays.asList(new QuestionAnswer("answer", true))),
                        new Question("Question4", Arrays.asList(new QuestionAnswer("answer", true))),
                        new Question("Question5", Arrays.asList(new QuestionAnswer("answer", true)))));

        assertThat(questionDao.findAll())
                .isNotNull()
                .hasSize(5)
                .contains(new Question("Question1", Arrays.asList(new QuestionAnswer("answer", true))));
    }

}