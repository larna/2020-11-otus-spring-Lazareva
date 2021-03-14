package ru.otus.spring.controller;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.authors.AuthorService;
import ru.otus.spring.services.books.BookService;
import ru.otus.spring.services.genres.GenreService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Класс BookController")
@WebMvcTest(controllers = BookController.class)
@MockBeans({@MockBean(AuthorService.class), @MockBean(GenreService.class)})
public class BookControllerHtmlUnitTest {
    private static final String EXPECTED_BOOK_NAME = "Test Book";
    private final Genre genre = Genre.builder().id(1L).name("Genre").build();
    private final List<Author> authors = List.of(Author.builder().id(1L).name("Author").build());
    private final Book book = Book.builder().id(1L).name(EXPECTED_BOOK_NAME).genre(genre).authors(authors).build();
    @Autowired
    private WebClient webClient;
    @MockBean
    private BookService bookService;

    @DisplayName("Проверка вывода книги с помощью WebClient")
    @Test
    void shouldCorrectViewBooks() throws Exception {
        Pageable pageable = PageRequest.of(0, 6);
        Page<Book> books = new PageImpl<>(List.of(book), pageable, 1);

        when(bookService.findAllByFilter(any(), any())).thenReturn(books);

        HtmlPage page = this.webClient.getPage("/books");
        assertThat(page.getBody().getTextContent()).contains(EXPECTED_BOOK_NAME);
    }

    @DisplayName("Если не заполнены обязательные поля, то показываются сообщения об ошибках")
    @Test
    public void shouldShowMessageErrorIfDoNotFillRequiredFields() throws Exception {
        HtmlPage createMsgFormPage = webClient.getPage("/books/new");
        HtmlForm form = createMsgFormPage.getHtmlElementById("form");
        HtmlButton submit = form.getOneHtmlElementByAttribute("button", "type", "submit");
        HtmlPage pageWithError = submit.click();

        HtmlInput bookNameInput = pageWithError.getHtmlElementById("bookName");
        HtmlSelect authorsSelect = pageWithError.getHtmlElementById("authors");

        assertAll(() -> assertThat(bookNameInput.getAttributeNode("class").getValue())
                        .contains("is-invalid"),
                () -> assertThat(authorsSelect.getAttributeNode("class").getValue())
                        .contains("is-invalid"));
    }
}
