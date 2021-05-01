package ru.otus.spring.controller.convertors.genre;

import org.springframework.core.convert.converter.Converter;
import ru.otus.spring.controller.dto.GenreDto;
import ru.otus.spring.domain.Genre;

public class GenreToDtoConverter implements Converter<Genre, GenreDto> {
    @Override
    public GenreDto convert(Genre genre) {
        if (genre == null)
            return null;
        return GenreDto.builder()
                .genreId(genre.getId())
                .genreName(genre.getName())
                .build();
    }
}
