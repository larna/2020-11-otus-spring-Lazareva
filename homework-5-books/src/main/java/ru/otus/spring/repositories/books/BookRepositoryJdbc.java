package ru.otus.spring.repositories.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repositories.authors.AuthorRepository;
import ru.otus.spring.repositories.books.ext.BookAuthorRelation;
import ru.otus.spring.services.books.BookNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Репозиторий для работы с книгами
 * В этом репозитории не стала делать динамически-формируемых запросов, используя больше подход Data Jpa, но
 * для большого кол-ва фильтров обойтись без какого то билдера мне кажется нереально.
 * В разных репозиториях решила сделать разные подходы.
 * Получилось много избыточного кода от которого меня избавит ORM.
 */
@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class BookRepositoryJdbc implements BookRepository {
    /**
     * Название последовательности для генерации id книг
     */
    private static final String BOOK_SEQUENCE = "BOOKS_SEQUENCE";
    private final NamedParameterJdbcOperations jdbc;
    /**
     * Репозиторий для работы с авторами
     */
    private final AuthorRepository authorDao;

    @Autowired
    public BookRepositoryJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations,
                              AuthorRepository authorDao) {
        this.jdbc = namedParameterJdbcOperations;
        this.authorDao = authorDao;
    }

    /**
     * Создать новую книгу
     *
     * @param book объект книга
     * @return объект книги
     */
    @Override
    public Book insert(final Book book) {
        final long bookId = insertBook(book);
        insertBookAuthors(bookId, book.getAuthors());
        return getById(bookId);
    }

    /**
     * Изменить книгу
     *
     * @param book объект книга
     * @return объект книги
     */
    @Override
    public Book update(Book book) {
        updateBook(book);
        deleteBookAuthorsByBookId(book.getId());
        insertBookAuthors(book.getId(), book.getAuthors());
        return book;
    }

    /**
     * Удалить книгу по её идентификатору
     *
     * @param bookId удалить книгу
     */
    @Override
    public void delete(long bookId) {
        deleteBookAuthorsByBookId(bookId);
        deleteBook(bookId);
    }

    /**
     * Получить книгу по идентификатору
     *
     * @param bookId идентификтор книги
     * @return объект книги вместе с авторами и жанром
     */
    @Override
    public Book getById(long bookId) {
        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) WHERE b.id=:id";
        List<Book> books = jdbc.query(sql, Map.of("id", bookId), new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return books.stream()
                .findFirst()
                .orElseThrow(BookNotFoundException::new);
    }

    /**
     * Получить книгу по названию и жанру
     *
     * @param genre жанр
     * @return объект книги вместе с авторами и жанром
     */
    @Override
    public List<Book> getAllByGenre(Genre genre) {
        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) WHERE b.genre_id=:genreId ORDER BY b.name";
        List<Book> books = jdbc.query(sql, Map.of("genreId", genre.getId()), new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return books;
    }

    /**
     * Получить по-страничный список книг у которых имя содержит подстроку
     *
     * @param bookName
     * @return страницу со списком книг
     */
    @Override
    public Page<Book> getAllByLikeBookName(String bookName, Pageable pageable) {
        final String countSql = "SELECT count(*) FROM books b WHERE b.name like :bookName";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bookName", "%" + bookName + "%");
        final Long count = jdbc.queryForObject(countSql, params, Long.class);
        if (count == 0)
            return Page.empty();

        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) " +
                "WHERE b.name like :bookName ORDER BY b.name LIMIT :limit OFFSET :offset";
        params.addValue("limit", pageable.getPageSize());
        params.addValue("offset", pageable.getOffset());
        List<Book> books = jdbc.query(sql, params, new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return new PageImpl<>(books, pageable, count);
    }

    /**
     * Получить по-страничный список книг по имени автора
     *
     * @param authorName
     * @return страницу со списком книг
     */
    @Override
    public Page<Book> getAllByLikeAuthorName(String authorName, Pageable pageable) {
        final String countSql = "SELECT count(distinct(ba.book_id)) FROM books_authors ba " +
                "WHERE ba.author_id IN (SELECT id FROM authors a WHERE a.name like :authorName)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("authorName", "%" + authorName + "%");
        final Long count = jdbc.queryForObject(countSql, params, Long.class);
        if (count == 0)
            return Page.empty();

        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) " +
                "WHERE b.id IN (SELECT ba.book_id FROM books_authors ba WHERE " +
                "ba.author_id IN (SELECT id FROM authors a WHERE a.name like :authorName)) " +
                "ORDER BY b.name LIMIT :limit OFFSET :offset";
        params.addValue("limit", pageable.getPageSize());
        params.addValue("offset", pageable.getOffset());
        List<Book> books = jdbc.query(sql, params, new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return new PageImpl<>(books, pageable, count);
    }

    /**
     * Получить по-страничный список книг по названию жанра
     *
     * @param genreName
     * @return страницу со списком книг
     */
    @Override
    public Page<Book> getAllByLikeGenreName(String genreName, Pageable pageable) {
        final String countSql = "SELECT count(*) FROM books b WHERE b.genre_id IN (SELECT id from genres WHERE name like :genreName)";
        final Long count = jdbc.queryForObject(countSql, Map.of("genreName", "%" + genreName + "%"), Long.class);
        if (count == 0)
            return Page.empty();

        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) WHERE b.genre_id IN (SELECT id from genres WHERE name like :genreName) " +
                "ORDER BY b.name LIMIT :limit OFFSET :offset";
        List<Book> books = jdbc.query(sql,
                Map.of("genreName", "%" + genreName + "%",
                        "limit", pageable.getPageSize(),
                        "offset", pageable.getOffset()),
                new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return new PageImpl<>(books, pageable, count);
    }

    /**
     * Получить по-страничный список книг по имени автора,  названию жанра, названию книги
     *
     * @param authorName
     * @param genreName
     * @param bookName
     * @return страницу со списком книг
     */
    @Override
    public Page<Book> getAllByLikeAuthorNameAndLikeGenreNameAndLikeBookName(String authorName, String genreName, String bookName, Pageable pageable) {
        final String countSql = "SELECT count(distinct (b.id)) FROM books b INNER JOIN books_authors ba ON (ba.book_id=b.id) " +
                "WHERE b.name like :bookName AND ba.author_id IN (SELECT id from authors a WHERE a.name like :authorName) " +
                "AND b.genre_id IN (SELECT id from genres WHERE name like :genreName)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bookName", "%" + bookName + "%");
        params.addValue("genreName", "%" + genreName + "%");
        params.addValue("authorName", "%" + authorName + "%");
        final Long count = jdbc.queryForObject(countSql, params, Long.class);
        if (count == 0)
            return Page.empty();

        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) WHERE b.name like :bookName AND " +
                "b.id IN (SELECT book_id from books_authors ba INNER JOIN authors a ON (ba.book_id=b.id AND ba.author_id=a.id) " +
                "WHERE a.name like :authorName) AND b.genre_id IN (SELECT id from genres WHERE name like :genreName) " +
                "ORDER BY b.name LIMIT :limit OFFSET :offset";
        params.addValue("limit", pageable.getPageSize());
        params.addValue("offset", pageable.getOffset());
        List<Book> books = jdbc.query(sql, params, new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return new PageImpl<>(books, pageable, count);
    }

    /**
     * Получить по-страничный список книг по имени автора,  названию книги
     *
     * @param authorName
     * @param bookName
     * @param pageable
     * @return
     */
    @Override
    public Page<Book> getAllByLikeAuthorNameAndLikeBookName(String authorName, String bookName, Pageable pageable) {
        final String countSql = "SELECT count(*) FROM books b WHERE b.name like :bookName AND " +
                "b.id IN (SELECT book_id from books_authors ba INNER JOIN authors a ON (ba.book_id=b.id AND ba.author_id=a.id) " +
                "WHERE a.name like :authorName) ";
        final Long count = jdbc.queryForObject(countSql,
                Map.of("bookName", "%" + bookName + "%",
                        "authorName", "%" + authorName + "%"),
                Long.class);
        if (count == 0)
            return Page.empty();

        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) " +
                "WHERE b.name like :bookName AND b.id IN (SELECT book_id from books_authors ba " +
                "INNER JOIN authors a ON (ba.book_id=b.id AND ba.author_id=a.id) WHERE a.name like :authorName) " +
                "ORDER BY b.name LIMIT :limit OFFSET :offset";
        List<Book> books = jdbc.query(sql,
                Map.of("bookName", "%" + bookName + "%",
                        "authorName", "%" + authorName + "%",
                        "limit", pageable.getPageSize(),
                        "offset", pageable.getOffset()),
                new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return new PageImpl<>(books, pageable, count);
    }

    /**
     * Получить по-страничный список книг по имени автора,  названию жанра
     *
     * @param authorName
     * @param genreName
     * @param pageable
     * @return
     */
    @Override
    public Page<Book> getAllByLikeAuthorNameAndLikeGenreName(String authorName, String genreName, Pageable pageable) {
        final String countSql = "SELECT count(*) FROM books b " +
                "WHERE b.id IN (SELECT book_id from books_authors ba INNER JOIN authors a ON (ba.book_id=b.id AND ba.author_id=a.id) " +
                "WHERE a.name like :authorName) AND b.genre_id IN (SELECT id from genres WHERE name like :genreName)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genreName", "%" + genreName + "%");
        params.addValue("authorName", "%" + authorName + "%");
        final Long count = jdbc.queryForObject(countSql, params, Long.class);
        if (count == 0)
            return Page.empty();

        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) " +
                "WHERE b.id IN (SELECT book_id from books_authors ba INNER JOIN authors a ON (ba.book_id=b.id AND ba.author_id=a.id) " +
                "WHERE a.name like :authorName) AND b.genre_id IN (SELECT id from genres WHERE name like :genreName) " +
                "ORDER BY b.name LIMIT :limit OFFSET :offset";
        params.addValue("limit", pageable.getPageSize());
        params.addValue("offset", pageable.getOffset());
        List<Book> books = jdbc.query(sql, params, new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return new PageImpl<>(books, pageable, count);
    }

    /**
     * Получить по-страничный список книг по названию жанра, названию книги
     *
     * @param genreName
     * @param bookName
     * @param pageable
     * @return
     */
    @Override
    public Page<Book> getAllByLikeGenreNameAndLikeBookName(String genreName, String bookName, Pageable pageable) {
        final String countSql = "SELECT count(*) FROM books b INNER JOIN genres g ON (g.id=b.genre_id) " +
                "WHERE b.name like :bookName AND b.genre_id IN (SELECT id from genres WHERE name like :genreName)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("bookName", "%" + bookName + "%");
        params.addValue("genreName", "%" + genreName + "%");
        final Long count = jdbc.queryForObject(countSql, params, Long.class);
        if (count == 0)
            return Page.empty();

        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) " +
                "WHERE b.name like :bookName AND b.genre_id IN (SELECT id from genres WHERE name like :genreName) " +
                "ORDER BY b.name LIMIT :limit OFFSET :offset";
        params.addValue("limit", pageable.getPageSize());
        params.addValue("offset", pageable.getOffset());
        List<Book> books = jdbc.query(sql, params, new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return new PageImpl<>(books, pageable, count);
    }

    /**
     * Получить по-страничный список книг согласно фильтрам
     *
     * @param specification
     * @param pageable
     * @return
     */
    @Override
    public Page<Book> getAll(BookSearchSpecification specification, Pageable pageable) {
        return specification.execute(this, pageable);
    }

    /**
     * Получить книгу по isbn, этот код должен быть уникальным для книги
     *
     * @param isbn International Standard Book Number
     * @return книгу
     */
    @Override
    public Book getByIsbn(String isbn) {
        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) WHERE b.isbn=:isbn ORDER BY b.name";
        List<Book> books = jdbc.query(sql, Map.of("isbn", isbn), new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return books.stream().findFirst().orElseThrow(BookNotFoundException::new);
    }

    /**
     * Получить список книг по автору
     *
     * @param author автор книг
     * @return список книг
     */
    @Override
    public List<Book> getAllByAuthor(Author author) {
        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) " +
                "WHERE b.id IN (SELECT book_id FROM  books_authors WHERE author_id=:authorId) ORDER BY b.name";
        List<Book> books = jdbc.query(sql, Map.of("authorId", author.getId()), new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return books;
    }

    /**
     * Постаничный вывод книг
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<Book> getAll(Pageable pageable) {
        final String countSql = "SELECT count(*) FROM books";
        Long count = jdbc.queryForObject(countSql, Map.of(), Long.class);
        if (count == 0)
            return Page.empty();

        final String sql = "SELECT b.id as book_id, b.isbn, b.name as book_name, g.id as genre_id, g.name as genre_name " +
                "FROM books b INNER JOIN genres g ON (g.id=b.genre_id) ORDER BY b.name LIMIT :limit OFFSET :offset";
        List<Book> books = jdbc.query(sql,
                Map.of("limit", pageable.getPageSize(), "offset", pageable.getOffset()),
                new BookResultSetExtractor());
        books = mergeWithAuthors(books);
        return new PageImpl<Book>(books, pageable, count);
    }

    /**
     * Проверка существования книги по идентификатору
     *
     * @param bookId идентификатор книги
     * @return true - существует, false - не существует
     */
    @Override
    public Boolean isExistsById(long bookId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM books WHERE id=:id)";
        return jdbc.queryForObject(sql, Map.of("id", bookId), Boolean.class);
    }

    /**
     * Проверка существования книги по isbn
     *
     * @param isbn
     * @return true - существует, false - не существует
     */
    @Override
    public boolean isExistsByIsbn(String isbn) {
        String sql = "SELECT EXISTS(SELECT 1 FROM books WHERE isbn=:isbn)";
        return jdbc.queryForObject(sql, Map.of("isbn", isbn), Boolean.class);
    }

    /**
     * Добавить книгу
     *
     * @param book
     * @return
     */
    private long insertBook(Book book) {
        final long bookId = book.getId() == null ? getNextId() : book.getId();
        final String sql = "INSERT INTO books(id, name, isbn, genre_id) VALUES(:id, :name, :isbn, :genreId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", bookId);
        params.addValue("name", book.getName());
        params.addValue("isbn", book.getIsbn());
        params.addValue("genreId", book.getGenre().getId());
        jdbc.update(sql, params);
        return bookId;
    }

    /**
     * Обновить книгу
     *
     * @param book
     */
    private void updateBook(Book book) {
        final String sql = "UPDATE books SET name=:name, isbn=:isbn, genre_id=:genreId WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("name", book.getName());
        params.addValue("isbn", book.getIsbn());
        params.addValue("genreId", book.getGenre().getId());
        jdbc.update(sql, params);
    }

    /**
     * Удалить книгу
     *
     * @param bookId
     */
    private void deleteBook(long bookId) {
        final String sql = "DELETE FROM books WHERE id=:id";
        jdbc.update(sql, Map.of("id", bookId));
    }


    /**
     * Установить авторов в книги
     *
     * @param books список книг
     * @return список книг
     */
    private List<Book> mergeWithAuthors(List<Book> books) {
        if (books == null || books.isEmpty())
            return books;
        List<BookAuthorRelation> relations = getBookAuthorRelations(books);
        final List<Author> authorsList = authorDao.getAllByBooks(books);

        Map<Long, Author> authorMap = authorsList.stream().collect(Collectors.toMap(Author::getId, author -> author));
        Map<Long, Book> bookMap = books.stream().collect(Collectors.toMap(Book::getId, book -> book));
        relations.forEach(relation -> {
            if (bookMap.containsKey(relation.getBookId()) && authorMap.containsKey(relation.getAuthorId()))
                bookMap.get(relation.getBookId()).getAuthors().add(authorMap.get(relation.getAuthorId()));
        });
        return books;
    }

    private List<BookAuthorRelation> getBookAuthorRelations(List<Book> books) {
        if (books == null || books.isEmpty())
            return List.of();

        List<Long> booksId = books.stream().map(Book::getId).collect(Collectors.toList());
        final String sql = "SELECT book_id, author_id FROM books_authors WHERE book_id IN (:booksId) ";

        return jdbc.query(sql, Map.of("booksId", booksId),
                (rs, i) -> new BookAuthorRelation(rs.getLong("book_id"), rs.getLong("author_id")));
    }

    /**
     * Дабавление связки книги с авторами
     *
     * @param bookId  id книги
     * @param authors авторы для книги
     */
    private void insertBookAuthors(final Long bookId, List<Author> authors) {
        if (authors == null || authors.isEmpty() || bookId == null)
            return;
        final String sql = "INSERT INTO books_authors (book_id, author_id) VALUES(:bookId, :authorId)";
        Map<String, Long>[] values = authors.stream()
                .map(author -> Map.of("bookId", bookId, "authorId", author.getId()))
                .toArray(Map[]::new);
        jdbc.batchUpdate(sql, values);
    }

    /**
     * Удаление связки книги с авторами
     *
     * @param bookId идентификатор книги
     */
    private void deleteBookAuthorsByBookId(Long bookId) {
        final String sql = "DELETE FROM books_authors WHERE book_id=:bookId";
        jdbc.update(sql, Map.of("bookId", bookId));
    }

    /**
     * Получить идентификатор из базы данных
     *
     * @return
     */
    private long getNextId() {
        return jdbc.queryForObject("SELECT NEXTVAL('" + BOOK_SEQUENCE + "')", Map.of(), Long.class);
    }

    /**
     * Класс, описывающий отображение данных полученных из базы данных, в объект
     */
    private static class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {

        @Override
        public List<Book> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                Long bookId = resultSet.getLong("book_id");
                String bookName = resultSet.getString("book_name");
                String isbn = resultSet.getString("isbn");

                Long genreId = resultSet.getLong("genre_id");
                String genreName = resultSet.getString("genre_name");
                Genre genre = new Genre(genreId, genreName);

                Book book = new Book(bookId, bookName, isbn, genre, new ArrayList<>());
                books.add(book);
            }
            return books;
        }
    }

}
