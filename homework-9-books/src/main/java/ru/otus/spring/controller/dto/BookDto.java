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

    public static BookDto of(Book book) {
        List<AuthorDto> authors = book.getAuthors().stream()
                .map(AuthorDto::of)
                .collect(Collectors.toList());
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .isbn(book.getIsbn())
                .genre(GenreDto.of(book.getGenre()))
                .authors(authors)
                .build();
    }

    public Book toDomain() {
        List<Author> authors = this.getAuthors().stream()
                .map(AuthorDto::toDomain)
                .collect(Collectors.toList());

        return Book.builder()
                .id(this.id)
                .name(this.name)
                .isbn(this.isbn)
                .genre(this.genre.toDomain())
                .authors(authors)
                .build();
    }
}
