package ru.otus.spring.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Genre;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String id;
    @NotBlank(message = "errors.name.not-blank")
    private String name;
    private String isbn;
    @NotNull(message = "errors.genre.not-null")
    private Genre genre;
    @NotNull(message = "errors.authors.not-null")
    @Valid
    private List<AuthorDto> authors;
}

