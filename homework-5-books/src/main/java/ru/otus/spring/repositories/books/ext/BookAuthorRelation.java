package ru.otus.spring.repositories.books.ext;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.spring.domain.Author;

@AllArgsConstructor
@Getter
public class BookAuthorRelation {
    private final long bookId;
    private final long authorId;
}
