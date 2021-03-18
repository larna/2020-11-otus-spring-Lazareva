package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.controller.dto.BookDto;
import ru.otus.spring.controller.dto.CommentDto;
import ru.otus.spring.controller.dto.GenreDto;
import ru.otus.spring.controller.validators.CommentFormValidator;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.services.books.BookService;
import ru.otus.spring.services.comments.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final BookService bookService;

    @InitBinder("comment")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(CommentFormValidator.getInstance());
    }

    /**
     * Форма добавления нового комментария
     *
     * @return
     */
    @GetMapping(value = "/books/{bookId}/comments/new")
    public String getNewCommentForm(@PathVariable("bookId") Long bookId,
                                    @ModelAttribute("comment") CommentDto comment,
                                    Model model) {
        Book book = bookService.findById(bookId);
        model.addAttribute("book", book);

        return "books/comments/input-form-comment";
    }

    /**
     * Форма редактирования комментария
     *
     * @return
     */
    @GetMapping(value = "/books/{bookId}/comments/{commentId}/edit")
    public String getEditCommentForm(@PathVariable("bookId") Long bookId,
                                     @PathVariable("commentId") Long commentId,
                                     Model model) {
        Book book = bookService.findById(bookId);
        Comment comment = commentService.findById(commentId);
        model.addAttribute("book", book);
        model.addAttribute("comment", commentService.domainToDto(comment));

        return "books/comments/input-form-comment";
    }

    /**
     * Сохранить комментарий
     *
     * @return
     */
    @PostMapping(value = "/books/{bookId}/comments")
    public String saveComment(@PathVariable("bookId") Long bookId,
                              @ModelAttribute("comment") CommentDto commentDto,
                              BindingResult bindingResult,
                              Model model) {
        CommentFormValidator.getInstance().validate(commentDto, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", bookService.findById(bookId));
            return "books/comments/input-form-comment";
        }
        Comment comment = commentService.dtoToDomain(commentDto,bookId);
        commentService.save(comment);
        return "redirect:/books/" + bookId + "/preview";
    }

    /**
     * Форма удаления комментария
     *
     * @return
     */
    @GetMapping(value = "/books/{bookId}/comments/{commentId}/delete")
    public String getDeleteCommentForm(@PathVariable("bookId") Long bookId,
                                       @PathVariable("commentId") Long commentId,
                                       Model model) {
        Book book = bookService.findById(bookId);
        Comment comment = commentService.findById(commentId);
        model.addAttribute("book", book);
        model.addAttribute("comment", commentService.domainToDto(comment));

        return "books/comments/delete-comment";
    }

    /**
     * Форма удаления комментария
     *
     * @return
     */
    @PostMapping(value = "/books/{bookId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("bookId") Long bookId,
                                @PathVariable("commentId") Long commentId) {
        commentService.deleteById(commentId);
        return "redirect:/books/" + bookId + "/preview";
    }
}
