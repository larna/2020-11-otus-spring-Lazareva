package ru.otus.spring.controller.convertors.book;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import ru.otus.spring.controller.convertors.author.AuthorToDtoConverter;
import ru.otus.spring.controller.convertors.genre.GenreToDtoConverter;
import ru.otus.spring.controller.dto.AuthorDto;
import ru.otus.spring.controller.dto.BookDto;
import ru.otus.spring.controller.dto.GenreDto;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BookToDtoConverter implements Converter<Book, BookDto> {
    private final AuthorToDtoConverter domainToAuthorDtoConverter;
    private final GenreToDtoConverter domainToGenreDtoConverter;

    @Override
    public BookDto convert(Book book) {
        if(book == null)
            return null;
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .isbn(book.getIsbn())
                .genre(getGenreDto(book.getGenre()))
                .authors(getAuthorsDto(book.getAuthors()))
                .build();
    }
    private GenreDto getGenreDto(Genre genre) {
        return domainToGenreDtoConverter.convert(genre);
    }

    private List<AuthorDto> getAuthorsDto(List<Author> authorsList) {
        if (authorsList == null || authorsList.isEmpty())
            return List.of();

        return authorsList.stream()
                .map(domainToAuthorDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
