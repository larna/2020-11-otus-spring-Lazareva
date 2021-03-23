package ru.otus.spring.controller.convertors.book;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import ru.otus.spring.controller.convertors.author.DtoToAuthorConverter;
import ru.otus.spring.controller.convertors.genre.DtoToGenreConverter;
import ru.otus.spring.controller.dto.AuthorDto;
import ru.otus.spring.controller.dto.BookDto;
import ru.otus.spring.controller.dto.GenreDto;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DtoToBookConverter implements Converter<BookDto, Book> {
    private final DtoToGenreConverter genreDtoToDomainConverter;
    private final DtoToAuthorConverter authorDtoToDomainConverter;

    @Override
    public Book convert(BookDto bookDto) {
        if (bookDto == null)
            return null;

        return Book.builder()
                .id(bookDto.getId())
                .name(bookDto.getName())
                .isbn(bookDto.getIsbn())
                .genre(getGenre(bookDto.getGenre()))
                .authors(getAuthors(bookDto.getAuthors()))
                .build();
    }

    private Genre getGenre(GenreDto genreDto) {
        return genreDtoToDomainConverter.convert(genreDto);
    }

    private List<Author> getAuthors(List<AuthorDto> authorDtoList) {
        if (authorDtoList == null || authorDtoList.isEmpty())
            return List.of();

        return authorDtoList.stream()
                .map(authorDtoToDomainConverter::convert)
                .collect(Collectors.toList());
    }
}
