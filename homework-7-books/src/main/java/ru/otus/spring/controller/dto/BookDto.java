package ru.otus.spring.controller.dto;

import lombok.Value;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;

import java.util.List;

@Value
public class BookDto {
    private final Book book;
    private final List<Comment> commentList;
}
