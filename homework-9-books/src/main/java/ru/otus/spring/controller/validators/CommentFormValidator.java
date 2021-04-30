package ru.otus.spring.controller.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.otus.spring.controller.dto.CommentDto;

public class CommentFormValidator implements Validator {
    private static final String COMMENT = "description";
    private static final String ERRORS_COMMENT_EMPTY = "errors.comment.description.empty";

    @Override
    public boolean supports(Class<?> aClass) {
        return CommentDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, COMMENT, ERRORS_COMMENT_EMPTY);
    }

    public static CommentFormValidator getInstance() {
        return new CommentFormValidator();
    }
}
