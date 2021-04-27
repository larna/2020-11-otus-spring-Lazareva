package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.spring.controllers.dto.BookDto;
import ru.otus.spring.domain.Book;
import ru.otus.spring.services.BooksConversionService;
import ru.otus.spring.services.books.BookNotFoundException;
import ru.otus.spring.services.books.BookService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final static Integer DEFAULT_SIZE = 6;
    private final BookService bookService;
    private final BooksConversionService<Book, BookDto> bookConversionService;

    @GetMapping(value = "/books")
    public Page<BookDto> getBooks(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "bookName", defaultValue = "", required = false) String bookName) {
        SearchFilter filter = SearchFilter.builder().bookName(bookName).build();
        Page<Book> books = bookService.findAllByFilter(filter, PageRequest.of(page, DEFAULT_SIZE));
        return bookConversionService.toPageOfDto(books);
    }

    @PostMapping(value = "/books")
    public BookDto saveBook(@RequestBody @Valid BookDto bookForm, BindingResult result) {
        return save(bookForm, result);
    }

    @PutMapping(value = "/books")
    public BookDto updateBook(@RequestBody @Valid BookDto bookForm, BindingResult result) {
        return save(bookForm, result);
    }

    private BookDto save(BookDto bookDto, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getAllErrors().toString());
        }
        Book book = bookConversionService.toDomain(bookDto);
        bookService.save(book);
        return bookConversionService.toDto(book);
    }

    /**
     * Подробности
     *
     * @return
     */
    @GetMapping(value = "/books/{id}")
    public BookDto getDetailBook(@PathVariable("id") String id) {
        try {
            Book book = bookService.findById(id);
            return bookConversionService.toDto(book);
        } catch (BookNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга не найдена id = " + id, ex);
        }
    }

    /**
     * Удаление книги
     */
    @DeleteMapping(value = "/books/{id}")
    public void deleteBook(@PathVariable("id") String id) {
        try {
            bookService.delete(id);
        } catch (BookNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга не найдена id = " + id, ex);
        }
    }
}
