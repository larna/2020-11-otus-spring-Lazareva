package ru.otus.spring.util.testing;

import ru.otus.spring.util.testing.QuestionFormatException;
import ru.otus.spring.domain.testing.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface QuestionParser {
    List<Question> parse(InputStream in) throws QuestionFormatException, IOException;
}
