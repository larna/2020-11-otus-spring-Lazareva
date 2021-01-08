package ru.otus.spring.util.testing;

public class QuestionFormatException extends RuntimeException {
    private static final String TEMPLATE_MESSAGE = "Wrong question format. %s \n " +
            "Question format must be: \n" +
            "question, N (count of answer), answer1, true/false( true - if answer is correct), answer2, true/false ..., answerN, true/false";

    public QuestionFormatException() {
        super(String.format(TEMPLATE_MESSAGE, ""));
    }

    public QuestionFormatException(final String row) {
        super(String.format(TEMPLATE_MESSAGE, "\n String from file: " + row));
    }
}
