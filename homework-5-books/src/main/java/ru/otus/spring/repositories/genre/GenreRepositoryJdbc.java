package ru.otus.spring.repositories.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Репозиторий для работы с жанрами
 */
@Repository
@RequiredArgsConstructor
public class GenreRepositoryJdbc implements GenreRepository {
    private final NamedParameterJdbcOperations jdbc;

    /**
     * Показать все жанры
     *
     * @return список жанров
     */
    @Override
    public List<Genre> getAll() {
        final String sql = "SELECT id, name FROM genres ORDER BY name";
        return jdbc.query(sql, new GenreMapper());

    }

    /**
     * Mapper для отображения строки из базы в объект
     */
    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            return new Genre(rs.getLong("id"), rs.getString("name"));
        }
    }
}
