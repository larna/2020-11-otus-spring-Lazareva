package ru.otus.spring.repositories.authors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
/**
 * Репозиторий для работы с авторами
 */
@Repository
public class AuthorRepositoryJdbc implements AuthorRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Autowired
    public AuthorRepositoryJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations;
    }
    /**
     * Новый автор
     *
     * @param author
     * @return
     */
    @Override
    public Author insert(Author author) {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.insertAuthor(author);
        final String sql = queryBuilder.build();
        jdbc.update(sql, queryBuilder.getParameters());
        return getAuthorByNameAndRealNameAndBirthday(author.getName(), author.getRealName(), author.getBirthday());
    }
    /**
     * Изменить автора
     *
     * @param author
     * @return
     */
    @Override
    public Author update(Author author) {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.updateAuthor(author);
        final String sql = queryBuilder.build();
        jdbc.update(sql, queryBuilder.getParameters());
        return author;
    }
    /**
     * Удалить автора
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.deleteFromAuthors().where().equalsId(id);
        final String sql = queryBuilder.build();
        jdbc.update(sql, queryBuilder.getParameters());
    }
    /**
     * Проверить существование атвора с заданным id
     *
     * @param id
     * @return
     */
    @Override
    public Boolean isExists(Long id) {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().existsAuthorById(id);
        final String sql = queryBuilder.build();
        return jdbc.queryForObject(sql, queryBuilder.getParameters(), Boolean.class);
    }
    /**
     * Получить автора по id
     *
     * @param id
     * @return
     */
    @Override
    public Author getAuthorById(Long id) {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().all().fromAuthors().where().equalsId(id);
        final String sql = queryBuilder.build();
        return jdbc.queryForObject(sql, queryBuilder.getParameters(), new AuthorMapper());
    }
    /**
     * Получить автора по псевдониму, имени, дате рождения
     *
     * @param name
     * @param realName
     * @param birthday
     * @return
     */
    @Override
    public Author getAuthorByNameAndRealNameAndBirthday(String name, String realName, LocalDate birthday) {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().all().fromAuthors()
                .where().equalsName(name).equalsRealName(realName).equalsBirthday(birthday);
        final String sql = queryBuilder.build();
        return jdbc.queryForObject(sql, queryBuilder.getParameters(), new AuthorMapper());
    }
    /**
     * Получить всех авторов для книг
     *
     * @param books
     * @return
     */
    @Override
    public List<Author> getAllByBooks(List<Book> books) {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().all().fromAuthors().where().authorsForBookIn(books);
        final String sql = queryBuilder.build();
        return jdbc.query(sql, queryBuilder.getParameters(), new AuthorMapper());
    }
    /**
     * Получить авторов по имени
     *
     * @param name
     * @return
     */
    @Override
    public List<Author> getAuthorByLikeName(String name) {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().all().fromAuthors().where().likeName(name);
        final String sql = queryBuilder.build();
        return jdbc.query(sql, queryBuilder.getParameters(), new AuthorMapper());
    }
    /**
     * По-страничный вывод всех авторов
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<Author> getAll(Pageable pageable) {
        AuthorQueryBuilder queryBuilder = AuthorQueryBuilder.select().all().fromAuthors();
        return getPage(queryBuilder, pageable);
    }
    /**
     * По-страничный вывод авторов согласно фильтру
     *
     * @param specification
     * @param pageable
     * @return
     */
    @Override
    public Page<Author> getAllByFilter(AuthorSearchSpecification specification, Pageable pageable) {
        AuthorQueryBuilder queryBuilder = specification.buildQuery();
        return getPage(queryBuilder, pageable);
    }

    /**
     * Выполнить полученный запрос и собрать страницу
     * @param queryBuilder
     * @param pageable
     * @return
     */
    private Page<Author> getPage(AuthorQueryBuilder queryBuilder, Pageable pageable) {
        String countSql = queryBuilder.buildCountQuery();
        BigInteger count = jdbc.queryForObject(countSql, queryBuilder.getParameters(), BigInteger.class);
        if (count == null || count.longValue() == 0)
            return Page.empty();

        queryBuilder.orderBy(Sort.by("name")).limit(pageable.getPageSize()).offset(pageable.getOffset());
        String sql = queryBuilder.build();

        List<Author> content = jdbc.query(sql, queryBuilder.getParameters(), new AuthorMapper());
        return new PageImpl<>(content, pageable, count.longValue());
    }
}
