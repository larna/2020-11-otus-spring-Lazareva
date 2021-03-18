package ru.otus.spring.controller.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.otus.spring.controller.dto.BookDto;
import ru.otus.spring.controller.dto.CommentDto;
import ru.otus.spring.domain.Book;

/**
 * This Validator validates only Book instances
 */
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
//        BookDto bookForm = (BookDto) o;
//
//        if (bookForm.getGenre() == null || bookForm.getGenre().getGenreId() == null)
//            errors.rejectValue(GENRE, ERRORS_BOOK_GENRE_EMPTY);
//
//        if (bookForm.getAuthors() == null || bookForm.getAuthors().isEmpty())
//            errors.rejectValue(AUTHORS, ERRORS_BOOK_AUTHORS_EMPTY);
    }

    public static CommentFormValidator getInstance() {
        return new CommentFormValidator();
    }
}
