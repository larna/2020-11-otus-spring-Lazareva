package ru.otus.spring.controller.ui.select;

import org.apache.commons.lang3.StringUtils;
import org.jline.reader.LineReader;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.otus.spring.controller.ui.views.View;
import ru.otus.spring.domain.Book;
import ru.otus.spring.services.books.BookService;

import java.util.Optional;

/**
 * Компонент для выбора книги
 */
@Component
public class BookSelect {
    private static final Integer PAGE_SIZE = 6;
    private static final String INVITE_MESSAGE = "Введите номер книги";
    private static final String START = "start";
    private static final String NEXT = "next";
    private static final String PREV = "prev";
    private static final String EXIT = "exit";
    private final View<Book> bookView;
    private LineReader lineReader;
    private final BookService bookService;

    public BookSelect(@Lazy LineReader lineReader,
                      View<Book> bookView,
                      BookService bookService) {
        this.lineReader = lineReader;
        this.bookView = bookView;
        this.bookService = bookService;
    }

    /**
     * Метод по-странично выводит список книг и ожидает от пользователя выбор в виде номера строки нужной книги
     * Номера строк на каждой странице начинаются с 1. Пользователь может перемещаться по страницам книг с помощью
     * команд next, prev.
     *
     * @return объект Optional<Book> будет пуст если пользователь выбрал команду exit или список книг пуст,
     * во всех остальных случаях будет возвращен Optional<Book>
     */
    public Optional<Book> prompt() {
        int page = 0;
        Page<Book> books = handlePaginationCommands(START, 0);
        if (books.isEmpty()) {
            System.out.println("Список книг пуст...");
            return Optional.empty();
        }
        Book selectedBook = null;
        do {
            String answer = lineReader.readLine(INVITE_MESSAGE + ": ");
            if (StringUtils.isEmpty(answer))
                continue;

            if (isExit(answer))
                return Optional.empty();

            if (isNextCommand(answer) || isPrevCommand(answer, page)) {
                books = handlePaginationCommands(answer, page);
                page = books.getNumber();
            } else {
                try {
                    selectedBook = books.getContent().get(Integer.parseInt(answer) - 1);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Номер должен быть целым числом из таблицы книг");
                }
            }
        } while (selectedBook == null);
        return Optional.of(selectedBook);
    }

    private Page<Book> handlePaginationCommands(String command, Integer page) {
        final Integer newPage = isPrevCommand(command, page) ? (page - 1) : (isNextCommand(command) ? (page + 1) : page);
        Page<Book> books = bookService.findAll(PageRequest.of(newPage, PAGE_SIZE));

        if (books.isEmpty())
            books = bookService.findAll(PageRequest.of(page, PAGE_SIZE));
        System.out.println(bookView.getListView(books.getContent(), "Для выбора книги введите её номер, " +
                "для перехода к следующей странице - next, предыдущей - prev, exit - для выхода"));
        return books;
    }

    private boolean isPrevCommand(String command, Integer page) {
        if (!PREV.equals(command))
            return false;
        if (page <= 0) {
            System.out.println("Эта первая страница и переход к предыдущей невозможен");
            return false;
        }
        return true;
    }

    private boolean isNextCommand(String command) {
        if (!NEXT.equals(command))
            return false;
        return true;
    }

    private boolean isExit(String command) {
        if (!EXIT.equals(command))
            return false;
        return true;
    }
}

