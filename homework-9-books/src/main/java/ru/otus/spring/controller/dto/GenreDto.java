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

    public static GenreDto of(Genre genre){
        if(genre == null)
            return new GenreDto();
        return new GenreDto(genre.getId(), genre.getName());
    }
    public Genre toDomain(){
        if(genreId == null)
            return null;
        return Genre.builder().id(genreId).build();
    }
}
