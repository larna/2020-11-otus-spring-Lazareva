package ru.otus.spring.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.controller.dto.BookDto;
import ru.otus.spring.controller.events.EventsPublisher;
import ru.otus.spring.controller.ui.View;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.books.BookNotFoundException;
import ru.otus.spring.services.books.BookService;
import ru.otus.spring.services.comments.CommentService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private final EventsPublisher eventsPublisher;
    /**
     * Сервис для работы с книгами
     */
    private final BookService bookService;
    /**
     * Сервис для работы с комментариями
     */
    private final CommentService commentService;
    /**
     * Отображение книг пользователю
     */
    private final View<Book> booksView;
    private final View<BookDto> bookDtoView;

    public BookCommands(EventsPublisher eventsPublisher,
                        BookService bookService,
                        CommentService commentService,
                        @Qualifier("booksView") View<Book> booksView,
                        @Qualifier("bookWithCommentsView") View<BookDto> bookDtoView) {
        this.eventsPublisher = eventsPublisher;
        this.bookService = bookService;
        this.commentService = commentService;
        this.booksView = booksView;
        this.bookDtoView = bookDtoView;
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
     * @param bookName  название книги
     * @param isbn      International Standard Book Number
     * @param genreId   id жанра
     * @param authorsId id авторов, разделенных через запятую
     * @return
     */
    @ShellMethod(key = {"b+", "book-insert"}, value = "New book")
    public String newBook(@ShellOption(value = {"-n", "-name"}, help = "Name of book with genre id and list of author's id") String bookName,
                          @ShellOption(value = {"-i", "-isbn"}, help = "Isbn of book", defaultValue = "") String isbn,
                          @ShellOption(value = {"-g", "-genre"}, help = "Genre id of book") Long genreId,
                          @ShellOption(value = {"-a", "-authors"}, help = "Comma separated author's id") String authorsId) {
        try {
            List<Author> authors = getAuthorsFromParameter(authorsId);
            Genre genre = Genre.builder().id(genreId).build();
            Book book = Book.builder()
                    .name(bookName)
                    .isbn(isbn)
                    .genre(genre)
                    .authors(authors)
                    .build();
            Book createdBook = bookService.save(book);
            Book bookWithAllInfo = bookService.findById(createdBook.getId());
            return booksView.getObjectView(bookWithAllInfo, "Книга успешно добавлена");
        } catch (NumberFormatException e) {
            return "Идентификатор автора должен быть числовым!";
        } catch (Exception e) {
            return "Книгу не удалось добавить. Проверьте есть ли выбранный вами жанр или авторы...";
        }
    }

    /**
     * Изменить книгу
     *
     * @param bookId    id книги
     * @param bookName  название книги
     * @param isbn      International Standard Book Number
     * @param genreId   id жанра
     * @param authorsId id авторов, разделенных через запятую
     * @return
     */
    @ShellMethod(key = {"bu", "book-update"}, value = "Update book")
    public String updateBook(@ShellOption(value = {"-id"}, help = "Book id") Long bookId,
                           @ShellOption(value = {"-n", "-name"}, help = "Name of book with genre id and list of author's id") String bookName,
                           @ShellOption(value = {"-i", "-isbn"}, help = "Isbn of book", defaultValue = "") String isbn,
                           @ShellOption(value = {"-g", "-genre"}, help = "Genre id of book") Long genreId,
                           @ShellOption(value = {"-a", "-authors"}, help = "Comma separated author's id") String authorsId) {

        try {
            List<Author> authors = getAuthorsFromParameter(authorsId);
            Genre genre = Genre.builder().id(genreId).build();
            Book book = Book.builder()
                    .id(bookId)
                    .name(bookName)
                    .isbn(isbn)
                    .genre(genre)
                    .authors(authors)
                    .build();
            eventsPublisher.publish(book);
        } catch (NumberFormatException e) {
            return "Идентификатор автора должен быть числовым!";
        }
        return "";
    }

    /**
     * Изменить название книги
     *
     * @param bookId   id книги
     * @param bookName название книги
     * @return
     */
    @ShellMethod(key = {"bun", "book-update-name"}, value = "Update book's name")
    public String updateBook(@ShellOption(value = {"-id"}, help = "Book id") Long bookId,
                             @ShellOption(value = {"-n", "-name"}, help = "Name of book with genre id and list of author's id") String bookName) {
        try {
            Book updatedBook = bookService.updateBookName(bookId, bookName);
            return booksView.getObjectView(updatedBook, "Книга успешно изменена");
        } catch (BookNotFoundException e) {
            return "Книгу с заданным id не удалось найти...";
        }
    }

    /**
     * Удалить книгу
     *
     * @param bookId id книги
     * @return
     */
    @ShellMethod(key = {"b-", "book-delete"}, value = "Delete book by id")
    public String deleteBook(@ShellOption(value = {"-id"}, help = "Delete book with id") Long bookId) {
        try {
            bookService.delete(bookId);
            return "Книга успешно удалена";
        } catch (BookNotFoundException e) {
            return "Книгу не удалось удалить. Книга с таким id не найдена...";
        }
    }

    /**
     * Найти книгу по идентиифкатору
     *
     * @param bookId id книги
     * @return
     */
    @ShellMethod(key = {"b?id", "book-by-id"}, value = "Find book by id")
    public String findBook(@ShellOption(value = {"-id"}, help = "Id of book") Long bookId) {
        try {
            Book book = bookService.findById(bookId);
            List<Comment> comments = commentService.findByBook(book.getId());
            BookDto bookDto = new BookDto(book, comments);
            return bookDtoView.getObjectView(bookDto, "Книга успешно найдена");
        } catch (BookNotFoundException e) {
            return "Книга c id=" + bookId + " не найдена";
        }
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

    private List<Author> getAuthorsFromParameter(String authorsIdString) {
        return Arrays.stream(authorsIdString.split(","))
                .map(Long::parseLong)
                .map(id -> Author.builder().id(id).build())
                .collect(Collectors.toList());
    }
}
