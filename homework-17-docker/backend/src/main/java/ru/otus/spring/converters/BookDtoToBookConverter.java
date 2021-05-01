package ru.otus.spring.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.controllers.dto.AuthorDto;
import ru.otus.spring.controllers.dto.BookDto;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.authors.AuthorService;
import ru.otus.spring.services.genres.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookDtoToBookConverter implements Converter<BookDto, Book> {
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    public Book convert(BookDto bookForm) {
        List<String> authorsId = bookForm.getAuthors().stream().map(AuthorDto::getId).collect(Collectors.toList());
        List<Author> authors = authorService.findAllByIdIn(authorsId);
        Genre genre = genreService.findById(bookForm.getGenre().getId());

        return Book.builder()
                .id(bookForm.getId())
                .name(bookForm.getName())
                .isbn(bookForm.getIsbn())
                .genre(genre)
                .authors(authors)
                .build();
    }
}
