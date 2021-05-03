package ru.otus.spring.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.controllers.dto.AuthorDto;
import ru.otus.spring.controllers.dto.BookDto;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookToBookDtoConverter implements Converter<Book, BookDto> {

    @Override
    public BookDto convert(Book book) {
        List<AuthorDto> authors = book.getAuthors().stream()
                .map(author -> new AuthorDto(author.getId(),author.getName()))
                .collect(Collectors.toList());
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .isbn(book.getIsbn())
                .authors(authors)
                .genre(book.getGenre())
                .build();
    }
}
