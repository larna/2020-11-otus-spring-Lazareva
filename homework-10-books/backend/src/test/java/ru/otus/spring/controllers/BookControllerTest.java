package ru.otus.spring.controllers;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.spring.controllers.dto.AuthorDto;
import ru.otus.spring.controllers.dto.BookDto;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.services.BooksConversionService;
import ru.otus.spring.services.authors.AuthorService;
import ru.otus.spring.services.books.BookNotFoundException;
import ru.otus.spring.services.books.BookService;
import ru.otus.spring.services.genres.GenreService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(BookController.class)
@DisplayName("BookControllerTest")
@MockBeans({@MockBean(AuthorService.class), @MockBean(GenreService.class)})
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @MockBean
    BookRepository bookRepository;
    @MockBean
    BooksConversionService<Book, BookDto> conversionService;

    @DisplayName("Должен корректно возвращать список книг")
    @Test
    public void shouldGivenBooks_whenGetBooks_thenReturnJsonArray() throws Exception {
        String expectedBookName = "book name";
        Genre genre = Genre.builder().id("12345").name("genre").build();
        List<AuthorDto> authors = List.of(AuthorDto.builder().id("4321").name("author").build());
        BookDto bookDto = getBookDto("1234", expectedBookName, genre, authors);
        given(conversionService.toPageOfDto(any())).willReturn(new PageImpl<>(List.of(bookDto)));

        mockMvc.perform(get("/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is(bookDto.getName())));
        verify(bookService, times(1)).findAllByFilter(any(), any());
    }

    @DisplayName("Должен возвращать статус OK(200), если книга успешно сохранена")
    @ParameterizedTest
    @CsvSource(value = {"true", "false"})
    public void shouldReturn200_ifCorrectSaveBook(Boolean isCreate) throws Exception {
        String expectedBookName = "New book name";
        String input = this.initDataAndMocksAndReturnInputJsonString(expectedBookName, false, false);

        MockHttpServletRequestBuilder requestBuilder = isCreate ?
                post("/books").contentType(MediaType.APPLICATION_JSON).content(input) :
                put("/books").contentType(MediaType.APPLICATION_JSON).content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(expectedBookName)));
        verify(bookService, times(1)).save(any());
    }

    @DisplayName("Должен возвращать статус BAD_REQUEST(400), если переданы невалидные данные")
    @ParameterizedTest
    @CsvSource(value = {"true,false,false,errors.name.not-blank, true",
            "false,true,false,errors.genre.not-null, true",
            "false,false,true,errors.authors.not-null, true",
            "true,false,false,errors.name.not-blank, false",
            "false,true,false,errors.genre.not-null, false",
            "false,false,true,errors.authors.not-null, false"})
    public void shouldReturn404_ifInvalidBook(Boolean isBookNameEmpty,
                                                           Boolean isGenreEmpty,
                                                           Boolean isAuthorsEmpty,
                                                           String errorMessage,
                                                           Boolean isCreate) throws Exception {
        String input = initDataAndMocksAndReturnInputJsonString(isBookNameEmpty ? null : "book name",
                isGenreEmpty, isAuthorsEmpty);

        MockHttpServletRequestBuilder requestBuilder = isCreate ?
                post("/books").contentType(MediaType.APPLICATION_JSON).content(input) :
                put("/books").contentType(MediaType.APPLICATION_JSON).content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(errorMessage)));
        verify(bookService, times(0)).save(any());
    }

    @DisplayName("Должен возвращать книгу")
    @Test
    public void shouldCorrectReturnBook() throws Exception {
        final String expectedBookName = "Detail of book";
        final String bookId = "1";
        Genre genre = Genre.builder().id("1").name("genre").build();
        List<AuthorDto> authorsDto = List.of(AuthorDto.builder().id("2").name("author").build());
        BookDto expected = getBookDto(bookId, expectedBookName, genre, authorsDto);

        given(bookService.findById(any())).willReturn(Book.builder().build());
        given(conversionService.toDto(any())).willReturn(expected);

        MockHttpServletRequestBuilder requestBuilder = get("/books/" + bookId);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(expectedBookName)));
        verify(bookService, times(1)).findById(eq(bookId));
    }
    @DisplayName("Должен возвращать NOT_FOUND(404) если книга не найдена")
    @Test
    public void shouldReturn404_ifBookNotFound() throws Exception {
        final String bookId = "1";
        given(bookService.findById(any())).willThrow(new BookNotFoundException());

        MockHttpServletRequestBuilder requestBuilder = get("/books/" + bookId);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
        verify(bookService, times(1)).findById(eq(bookId));
        verify(conversionService, times(0)).toDto(any());
    }
    @DisplayName("Должен удалять книгу")
    @Test
    public void shouldCorrectDeleteBook() throws Exception {
        final String bookId = "1";
        MockHttpServletRequestBuilder requestBuilder = delete("/books/" + bookId);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
        verify(bookService, times(1)).delete(eq(bookId));
    }
    @DisplayName("Должен возвращать NOT_FOUND(404) если книга не найдена при удалении")
    @Test
    public void shouldReturn404_ifBookNotFound_inDelete() throws Exception {
        final String bookId = "1";
        given(bookRepository.findById(eq(bookId))).willReturn(Optional.empty());
        Mockito.doThrow(new BookNotFoundException()).when(bookService).delete(eq(bookId));
        MockHttpServletRequestBuilder requestBuilder = delete("/books/" + bookId);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
        verify(bookService, times(1)).delete(eq(bookId));
    }
    private String initDataAndMocksAndReturnInputJsonString(String name, Boolean isGenreEmpty, Boolean isAuthorEmpty) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String bookId = "1";
        Genre genre = isGenreEmpty ? null : Genre.builder().id("1").name("genre").build();
        List<AuthorDto> authorsDto = isAuthorEmpty ? null : List.of(AuthorDto.builder().id("2").name("author").build());
        List<Author> authors = isAuthorEmpty ? null : List.of(Author.builder().id("2").build());

        BookDto expected = getBookDto(bookId, name, genre, authorsDto);
        Book book = getBook(bookId, name, genre, authors);
        given(bookService.save(any())).willReturn(book);
        given(conversionService.toDto(any())).willReturn(expected);

        return mapper.writeValueAsString(expected);
    }

    private BookDto getBookDto(String bookId, String name, Genre genre, List<AuthorDto> authors) {
        return BookDto.builder()
                .id(bookId)
                .name(name)
                .genre(genre)
                .authors(authors)
                .build();
    }

    private Book getBook(String bookId, String name, Genre genre, List<Author> authors) {
        return Book.builder()
                .id(bookId)
                .name(name)
                .genre(genre)
                .authors(authors)
                .build();
    }
}