package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.controller.dto.AuthorDto;
import ru.otus.spring.controller.dto.BookDto;
import ru.otus.spring.controller.dto.GenreDto;
import ru.otus.spring.controller.editors.AuthorDtoPropertyEditor;
import ru.otus.spring.controller.editors.GenreDtoPropertyEditor;
import ru.otus.spring.controller.validators.BookFormValidator;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.authors.AuthorService;
import ru.otus.spring.services.books.BookService;
import ru.otus.spring.services.genres.GenreService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final static Integer DEFAULT_SIZE = 6;
    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final ConversionService conversionService;


    @InitBinder("bookForm")
    public void initBinderBookForm(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(GenreDto.class, new GenreDtoPropertyEditor());
        binder.registerCustomEditor(AuthorDto.class, new AuthorDtoPropertyEditor());

        binder.setValidator(BookFormValidator.getInstance());
    }

    @ModelAttribute("genres")
    public List<GenreDto> genres() {
        List<Genre> genres = genreService.findAll();
        return genres.stream()
                .map(genre -> GenreDto.builder()
                        .genreId(genre.getId())
                        .genreName(genre.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @ModelAttribute("authors")
    public List<AuthorDto> authors() {
        List<Author> authors = authorService.findAll();
        return authors.stream()
                .map(author -> AuthorDto.builder()
                        .authorId(author.getId())
                        .authorName(author.getName())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Просмотр книг
     *
     * @param page
     * @param model
     * @return
     */
    @GetMapping(value = "/books")
    public String getBooks(@RequestParam(value = "page", defaultValue = "0") @Min(0) @Valid Integer page,
                           @RequestParam(value = "bookName", defaultValue = "") String bookName,
                           Model model) {
        SearchFilter filter = SearchFilter.builder().bookName(bookName).build();
        Page<Book> books = bookService.findAllByFilter(filter, PageRequest.of(page, DEFAULT_SIZE));

        model.addAttribute("books", books.getContent());
        model.addAttribute("page", page);
        model.addAttribute("size", DEFAULT_SIZE);
        model.addAttribute("totalPages", books.getTotalPages());

        return "books/books";
    }

    /**
     * Форма поиска книги
     *
     * @return
     */
    @GetMapping(value = "/books/search")
    public String getSearchBookForm(@ModelAttribute("searchFilter") SearchFilter searchFilter, Model model) {
        return "books/search";
    }

    /**
     * Форма добавления новой книги
     *
     * @return
     */
    @GetMapping(value = "/books/new")
    public String getNewBookForm(@ModelAttribute("bookForm") BookDto bookForm, Model model) {
        return "books/input-form";
    }


    /**
     * Форма редактирования книги
     *
     * @return
     */
    @GetMapping(value = "/books/{id}/edit")
    public String getEditBookForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findById(id);
        BookDto bookForm = conversionService.convert(book, BookDto.class);
        model.addAttribute("bookForm", bookForm);
        return "books/input-form";
    }

    /**
     * Сохранение книги
     *
     * @return
     */
    @PostMapping(value = "/books")
    public String saveBook(@ModelAttribute("bookForm") @Valid BookDto bookForm,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/input-form";
        }
        Book book = conversionService.convert(bookForm, Book.class);
        Book savedBook = bookService.save(book);
        return String.format("redirect:/books/%d/preview", savedBook.getId());
    }


    /**
     * Форма удаления книги
     *
     * @return
     */
    @GetMapping(value = "/books/{id}/delete")
    public String getDeleteBookForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        return "books/delete";
    }

    /**
     * Удаление книги
     *
     * @return
     */
    @PostMapping(value = "/books/{id}/delete")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    /**
     * Подробности
     *
     * @return
     */
    @GetMapping(value = "/books/{id}/preview")
    public String getDetailBook(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        return "books/detail";
    }


}
