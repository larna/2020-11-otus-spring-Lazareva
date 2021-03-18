package ru.otus.spring.controller.dto;

import liquibase.pro.packaged.G;
import lombok.*;
import ru.otus.spring.domain.Genre;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class GenreDto {
    @NotNull
    private Long genreId;
    @ToString.Exclude
    private String genreName;

    public GenreDto() {
        this.genreId = null;
    }
}
