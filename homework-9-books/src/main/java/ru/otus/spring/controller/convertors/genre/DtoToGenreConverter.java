package ru.otus.spring.controller.convertors.genre;

import org.springframework.core.convert.converter.Converter;
import ru.otus.spring.controller.dto.GenreDto;
import ru.otus.spring.domain.Genre;

public class DtoToGenreConverter implements Converter<GenreDto, Genre> {
    @Override
    public Genre convert(GenreDto genreDto) {
        if (genreDto == null)
            return null;

        return Genre.builder()
                .id(genreDto.getGenreId())
                .name(genreDto.getGenreName())
                .build();
    }
}
