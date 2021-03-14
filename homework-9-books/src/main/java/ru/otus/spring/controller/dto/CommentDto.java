package ru.otus.spring.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String description;

    public static CommentDto of(Comment comment) {
        if (comment == null)
            return new CommentDto();
        return new CommentDto(comment.getId(), comment.getDescription());
    }

    public Comment toDomain(Long bookId) {
        Book book = Book.builder().id(bookId).build();
        return Comment.builder()
                .id(id)
                .description(description)
                .book(book)
                .build();
    }
}
