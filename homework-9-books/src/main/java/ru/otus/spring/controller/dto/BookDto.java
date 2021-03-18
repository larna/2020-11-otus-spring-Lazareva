package ru.otus.spring.controller.dto;

import lombok.*;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    private String isbn;
    @Valid
    private GenreDto genre;
    @Valid
    private List<AuthorDto> authors;
}
