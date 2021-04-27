package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.spring.controllers.dto.CommentDto;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.services.BooksConversionService;
import ru.otus.spring.services.books.BookNotFoundException;
import ru.otus.spring.services.books.BookService;
import ru.otus.spring.services.comments.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final BookService bookService;
    private final BooksConversionService<Comment, CommentDto> commentConversionService;

    @GetMapping(value = "/books/{bookId}/comments")
    public List<CommentDto> getComments(@PathVariable("bookId") String bookId) {
        List<Comment> comments = commentService.findByBookId(bookId);
        return commentConversionService.toListOfDto(comments);
    }

    @PostMapping(value = "/books/{bookId}/comments")
    public CommentDto createComment(@PathVariable("bookId") String bookId,
                                    @RequestBody @Valid CommentDto commentDto,
                                    BindingResult result) {
        return save(bookId, commentDto, result);
    }

    @PutMapping(value = "/books/{bookId}/comments")
    public CommentDto updateComment(@PathVariable("bookId") String bookId,
                                    @RequestBody @Valid CommentDto commentDto,
                                    BindingResult result) {
        return save(bookId, commentDto, result);
    }

    private CommentDto save(String bookId, CommentDto commentDto, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getAllErrors().toString());
        }
        try {
            Book book = bookService.findById(bookId);
            Comment comment = commentConversionService.toDomain(commentDto);
            comment.setBook(book);
            commentService.save(comment);
            return commentConversionService.toDto(comment);
        } catch (BookNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "comment save error", ex);
        }
    }

    @DeleteMapping(value = "/books/{bookId}/comments/{commentId}")
    public void deleteComment(@PathVariable("commentId") String id) {
        commentService.delete(id);
    }

    @GetMapping(value = "/books/{bookId}/comments/{commentId}")
    public CommentDto getComment(@PathVariable("commentId") String commentId) {
        Comment comment = commentService.findById(commentId);
        return commentConversionService.toDto(comment);
    }
}
