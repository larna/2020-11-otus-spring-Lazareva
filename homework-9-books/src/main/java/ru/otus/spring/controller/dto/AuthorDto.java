package ru.otus.spring.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Genre;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class AuthorDto {
    @NotNull
    private Long authorId;
    @ToString.Exclude
    private String authorName;

    public AuthorDto() {
        this.authorId = null;
    }
}
