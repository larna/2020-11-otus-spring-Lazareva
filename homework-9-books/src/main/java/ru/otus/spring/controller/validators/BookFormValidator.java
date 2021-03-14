package ru.otus.spring.controller.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.otus.spring.controller.dto.BookDto;
import ru.otus.spring.domain.Book;

/**
 * This Validator validates only Book instances
 */
public class BookFormValidator implements Validator {
    private static final String BOOK_NAME = "name";
    private static final String ERRORS_BOOK_NAME_EMPTY = "errors.book.name.empty";

    private static final String GENRE = "genre";
    private static final String ERRORS_BOOK_GENRE_EMPTY = "errors.book.genre.empty";

    private static final String AUTHORS = "authors";
    private static final String ERRORS_BOOK_AUTHORS_EMPTY = "errors.book.authors.empty";

    @Override
    public boolean supports(Class<?> aClass) {
        return BookDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, BOOK_NAME, ERRORS_BOOK_NAME_EMPTY);
        BookDto bookForm = (BookDto) o;

        if (bookForm.getGenre() == null || bookForm.getGenre().getGenreId() == null)
            errors.rejectValue(GENRE, ERRORS_BOOK_GENRE_EMPTY);

        if (bookForm.getAuthors() == null || bookForm.getAuthors().isEmpty())
            errors.rejectValue(AUTHORS, ERRORS_BOOK_AUTHORS_EMPTY);
    }

    public static BookFormValidator getInstance() {
        return new BookFormValidator();
    }
}
