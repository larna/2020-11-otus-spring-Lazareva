package ru.otus.spring.repositories.authors;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.spring.domain.Author;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Отображение автора из строки БД в объект
 */
public class AuthorMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        Map<String, String> mapObjToDB = AuthorQueryBuilder.getMappingObjectToDb();
        long id = resultSet.getLong(mapObjToDB.get("id"));
        String name = resultSet.getString(mapObjToDB.get("name"));
        String realName = resultSet.getString(mapObjToDB.get("realName"));
        Date dateOfBirthday = resultSet.getDate(mapObjToDB.get("birthday"));
        LocalDate birthday = dateOfBirthday != null ? dateOfBirthday.toLocalDate() : null;
        return new Author(id, name, realName, birthday);
    }
}