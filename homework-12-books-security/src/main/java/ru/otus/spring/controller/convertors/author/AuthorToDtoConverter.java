package ru.otus.spring.controller.convertors.author;

import org.springframework.core.convert.converter.Converter;
import ru.otus.spring.controller.dto.AuthorDto;
import ru.otus.spring.domain.Author;

public class AuthorToDtoConverter implements Converter<Author, AuthorDto> {
    @Override
    public AuthorDto convert(Author author) {
        if(author == null)
            return null;
        return AuthorDto.builder()
                .authorId(author.getId())
                .authorName(author.getName())
                .build();
    }
}
