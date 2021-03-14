package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.spring.controller.dto.AuthorDto;
import ru.otus.spring.controller.dto.GenreDto;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.authors.AuthorService;
import ru.otus.spring.services.books.BookService;
import ru.otus.spring.services.genres.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Класс BookController")
@WebMvcTest(controllers = BookController.class)
class BookControllerTest {
    private static final String EXPECTED_BOOK_NAME = "Test Book";
    private final Genre genre = Genre.builder().id(1L).name("Genre").build();
    private final List<Author> authors = List.of(Author.builder().id(1L).name("Author").build());
    private final Book book = Book.builder().id(1L).name(EXPECTED_BOOK_NAME).genre(genre).authors(authors).build();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @BeforeEach
    public void setup() {
        BookController bookController = new BookController(bookService, genreService, authorService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @DisplayName("Должен корректно показывать книги")
    @Test
    void shouldCorrectGetBooks() throws Exception {
        Pageable pageable = PageRequest.of(0, 6);
        Page<Book> books = new PageImpl<>(List.of(book), pageable, 1);

        given(bookService.findAllByFilter(any(),any())).willReturn(books);

        mockMvc.perform(get("/books", 0)
                .accept(MediaType.TEXT_HTML)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("size"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(view().name("books/books"))
                .andReturn().getResponse().getContentAsString().contains(EXPECTED_BOOK_NAME);
    }

    @DisplayName("Должен корректно выводить форму для новой книги")
    @Test
    void shouldGetNewBookForm() throws Exception {
        given(genreService.findAll()).willReturn(List.of(genre));
        given(authorService.findAll()).willReturn(authors);

        mockMvc.perform(get("/books/new")
                .contentType("text/html"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attributeExists("bookForm"))
                .andExpect(view().name("books/input-form"));

        verify(genreService, times(1)).findAll();
        verify(authorService, times(1)).findAll();
    }

    @DisplayName("Должен корректно выводить форму для редактирования книги")
    @Test
    void shouldGetEditBookForm() throws Exception {
        given(bookService.findById(any())).willReturn(book);

        mockMvc.perform(get("/books/{id}/edit", 1L).contentType("text/html"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/input-form"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attributeExists("bookForm"));

        verify(bookService, times(1)).findById(any());
    }

    @DisplayName("Должен корректно выводить форму для удаления книги")
    @Test
    void shouldGetDeleteBookForm() throws Exception {
        given(bookService.findById(any())).willReturn(book);

        mockMvc.perform(get("/books/{id}/delete", 1L).contentType("text/html"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/delete"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(model().attributeExists("book"));

        verify(bookService, times(1)).findById(any());
    }

    @DisplayName("Должен корректно сохранять книгу")
    @Test
    void shouldCorrectSaveBook() throws Exception {
        GenreDto genreDto = GenreDto.builder().genreId(1L).build();
        AuthorDto authorDto = AuthorDto.builder().authorId(1L).build();

        given(bookService.save(any())).willReturn(book);

        RequestBuilder requestBuilder = post("/books")
                .param("id", "")
                .param("name", EXPECTED_BOOK_NAME)
                .param("isbn", "")
                .param("genre", genreDto.toString())
                .param("authors", "[" + authorDto.toString() + "]");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/1/preview"));
        verify(bookService, times(1)).save(any());
    }

    @DisplayName("Должен отображать форму с сообщениями об ошибках, если не все поля заполнены")
    @Test
    void shouldNotSaveIfExistsError() throws Exception {
        given(bookService.save(any())).willReturn(book);
        RequestBuilder requestBuilder = post("/books")
                .param("id", "")
                .param("name", EXPECTED_BOOK_NAME)
                .param("isbn", "")
                .param("genreId", "1");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("bookForm"))
                .andExpect(view().name("books/input-form"));
        verify(bookService, times(0)).save(any());
    }

    @DisplayName("Должен корректно удалять книгу")
    @Test
    void shouldCorrectDeleteBook() throws Exception {
        mockMvc.perform(post("/books/1/delete"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        verify(bookService, times(1)).delete(any());
    }

    @DisplayName("Должен отображать информацию о книге")
    @Test
    void shouldViewDetail() throws Exception {
        given(bookService.findById(any())).willReturn(book);
        mockMvc.perform(get("/books/1/preview"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name("books/detail"));
        verify(bookService, times(1)).findById(any());
    }
}