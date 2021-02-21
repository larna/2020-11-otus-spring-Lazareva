package ru.otus.spring.controller.dto;

import lombok.Getter;
import ru.otus.spring.domain.Author;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Класс DTO для получения и отображения объекта автор
 */
@Getter
public class AuthorDto {
    /**
     * формат даты
     */
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    /**
     * Идентификатор автора
     */
    private final Long id;
    /**
     * Имя автора или псевдоним, если он издает свои книги под псевдонимом
     */
    private final String name;
    /**
     * Настоящее имя автора
     */
    private final String realName;
    /**
     * Дата рождения
     */
    private final String birthday;

    public AuthorDto() {
        this.id = null;
        this.name = null;
        this.realName = null;
        this.birthday = null;
    }

    public AuthorDto(String name, String realName, String birthday) {
        this.id = null;
        this.name = name;
        this.realName = realName;
        this.birthday = birthday;
    }

    public AuthorDto(Long id, String name, String realName, String birthday) {
        this.id = id;
        this.name = name;
        this.realName = realName;
        this.birthday = birthday;
    }

    private static LocalDate valueOfBirthday(String birthday) {
        if (birthday == null || birthday.isEmpty())
            return null;
        try {
            return LocalDate.parse(birthday, DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (DateTimeParseException e) {
            throw new BirthdayFormatException();
        }
    }

    private static String birthdayToString(LocalDate birthday) {
        if (birthday == null)
            return "";
        return birthday.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static Author toDomain(AuthorDto dto) {
        return new Author(dto.id, dto.name, dto.realName, valueOfBirthday(dto.birthday));
    }
    public static Author toDomain(String name, String realName, String birthday) {
        return Author.of(name, realName, valueOfBirthday(birthday));
    }

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getName(), author.getRealName(), birthdayToString(author.getBirthday()));
    }
}
