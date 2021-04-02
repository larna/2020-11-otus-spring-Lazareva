package ru.otus.spring.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.controller.ui.select.AuthorSelect;
import ru.otus.spring.controller.ui.select.BookSelect;
import ru.otus.spring.controller.ui.select.GenreSelect;
import ru.otus.spring.controller.ui.views.View;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.books.BookService;

import java.util.List;
import java.util.Optional;

/**
 * Компонент, обслуживающий работу интерфейса с книгами
 */
@ShellComponent
@ShellCommandGroup("book")
public class BookCommands {
    /**
     * Значения размера страницы по-умолчанию
     */
    private static final String DEFAULT_PARAM_PAGE_SIZE = "10";
    /**
     * Сервис для работы с книгами
     */
    private final BookService bookService;

    /**
     * Отображение книг пользователю
     */
    private final View<Book> booksView;
    private final GenreSelect genreSelect;
    private final AuthorSelect authorSelect;
    private final BookSelect bookSelect;

    public BookCommands(BookService bookService,
                        @Qualifier("booksView")View<Book> booksView,
                        GenreSelect genreSelect,
                        AuthorSelect authorSelect,
                        BookSelect bookSelect) {
        this.bookService = bookService;
        this.booksView = booksView;
        this.genreSelect = genreSelect;
        this.authorSelect = authorSelect;
        this.bookSelect = bookSelect;
    }

    /**
     * По-страничный вывод книг
     *
     * @param page - страница
     * @param size - размер страницы
     * @return
     */
    @ShellMethod(key = {"b", "books"}, value = "View all books")
    public String findAllBooks(@ShellOption(value = {"-p", "-page"}, help = "Set index of page", defaultValue = "0") Integer page,
                               @ShellOption(value = {"-s", "-size"}, help = "Set size of page", defaultValue = DEFAULT_PARAM_PAGE_SIZE) Integer size) {
        if (page < 0 || size < 0)
            return "Страница или размер не могут быть отрицательными!";
        Page<Book> books = bookService.findAll(PageRequest.of(page, size));
        return showBooks(books);
    }

    /**
     * Добавить новую книгу
     *
     * @param bookName название книги
     * @param isbn     International Standard Book Number
     * @return
     */
    @ShellMethod(key = {"b+", "book-insert"}, value = "New book")
    public String newBook(@ShellOption(value = {"-n", "-name"}, help = "Name of book with genre id and list of author's id") String bookName,
                          @ShellOption(value = {"-i", "-isbn"}, help = "Isbn of book", defaultValue = "") String isbn) {
        Genre genre = genreSelect.prompt();
        List<Author> authors = authorSelect.prompt();
        Book book = Book.builder()
                .name(bookName)
                .isbn(isbn)
                .genre(genre)
                .authors(authors)
                .build();
        bookService.save(book);
        return booksView.getObjectView(book, "Книга успешно добавлена");
    }

    /**
     * Изменить книгу
     *
     * @param bookName название книги
     * @param isbn     International Standard Book Number
     * @return
     */
    @ShellMethod(key = {"bu", "book-update"}, value = "Update book")
    public String updateBook(@ShellOption(value = {"-n", "-name"}, help = "Name of book with genre id and list of author's id") String bookName,
                             @ShellOption(value = {"-i", "-isbn"}, help = "Isbn of book", defaultValue = "") String isbn) {

        try {
            Optional<Book> selectedBook = bookSelect.prompt();
            if (!selectedBook.isPresent())
                return "Книга не выбрана";

            Book book = selectedBook.get();
            book.setGenre(genreSelect.prompt());
            book.setAuthors(authorSelect.prompt());
            book.setName(bookName);
            book.setIsbn(isbn);
            bookService.save(book);
            return booksView.getObjectView(book, "Книга успешно изменена");
        } catch (Exception e) {
            return "Книгу не удалось изменить. Проверьте есть ли выбранный вами жанр или авторы...";
        }
    }

    /**
     * Удалить книгу
     *
     * @return
     */
    @ShellMethod(key = {"b-", "book-delete"}, value = "Delete book by id")
    public String deleteBook() {
        Optional<Book> selectedBook = bookSelect.prompt();
        if (!selectedBook.isPresent())
            return "Книга не выбрана";

        Book book = selectedBook.get();

        bookService.delete(book);
        return "Книга успешно удалена";
    }

    /**
     * Найти все книги удовлетворяющие введенным критериям. Критерии между собой объендиняются по И, поиск производиться с помощью like.
     *
     * @param authorName имя автора
     * @param genreName  название жанра
     * @param bookName   название книги
     * @param page       страница
     * @param size       размер страницы
     * @return
     */
    @ShellMethod(key = {"b?", "books-find"}, value = "Find books")
    public String findBooks(@ShellOption(value = {"-a", "-author"}, help = "Name of author", defaultValue = "") String authorName,
                            @ShellOption(value = {"-g", "-genre"}, help = "Genre for author", defaultValue = "") String genreName,
                            @ShellOption(value = {"-b", "-book"}, help = "Books of author", defaultValue = "") String bookName,
                            @ShellOption(value = {"-p", "-page"}, help = "Set index of page", defaultValue = "0") Integer page,
                            @ShellOption(value = {"-s", "-size"}, help = "Set size of page", defaultValue = DEFAULT_PARAM_PAGE_SIZE) Integer size) {

        if (page < 0 || size < 0)
            return "Страница или размер не могут быть отрицательными!";

        SearchFilter searchFilter = new SearchFilter(authorName, genreName, bookName);
        Page<Book> books = bookService.findAllByFilter(searchFilter, PageRequest.of(page, size));
        return showBooks(books);
    }

    /**
     * Показать страницу книг пользователю
     *
     * @param books
     * @return
     */
    private String showBooks(Page<Book> books) {
        if (books.isEmpty())
            return "Книги не найдены";

        String message = String.format("Показано %d из %d книг", books.getNumberOfElements(), books.getTotalElements());
        return booksView.getListView(books.getContent(), message);
    }
}
