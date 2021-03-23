package ru.otus.spring.controller.convertors.author;

import org.springframework.core.convert.converter.Converter;
import ru.otus.spring.controller.dto.AuthorDto;
import ru.otus.spring.domain.Author;

public class DtoToAuthorConverter implements Converter<AuthorDto, Author> {
    @Override
    public Author convert(AuthorDto authorDto) {
        if (authorDto == null)
            return null;
        return Author.builder()
                .id(authorDto.getAuthorId())
                .name(authorDto.getAuthorName())
                .build();
    }
}
