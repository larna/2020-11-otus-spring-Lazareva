package ru.otus.spring.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс SearchFilter")
class SearchFilterTest {
    @DisplayName("Должен корректно определять что фильтр пуст, т.е. все поля фильтра пустые или null ")
    @ParameterizedTest
    @CsvSource(value = {",,", "'','',''"})
    void shouldCorrectDefineIfFilterIsEmpty(String authorName, String genreName, String bookName) {
        SearchFilter actualSearchFilter = new SearchFilter(authorName, genreName, bookName);
        assertAll(() -> assertEquals(true, actualSearchFilter.isEmpty()));
    }
    @DisplayName("Должен корректно определять что фильтр не пуст")
    @ParameterizedTest
    @CsvSource(value = {"Test, Test, Test", "Test,'',''","Test, Test,", ",Test,",",Test,Test",",,Test"})
    void shouldCorrectDefineWhatFilterIsNotEmpty(String authorName, String genreName, String bookName) {
        SearchFilter actualSearchFilter = new SearchFilter(authorName, genreName, bookName);
        assertAll(() -> assertEquals(false, actualSearchFilter.isEmpty()));
    }

    @DisplayName("Должен корректно определять имя автора пусто или нет")
    @ParameterizedTest
    @CsvSource(value = {",,,true", "'','','',true","Test,,,false"})
    void shouldCorrectDefineIfAuthorNameIsEmpty(String authorName, String genreName, String bookName, Boolean expectedValue) {
        SearchFilter actualSearchFilter = new SearchFilter(authorName, genreName, bookName);
        assertAll(() -> assertEquals(expectedValue, actualSearchFilter.isAuthorNameEmpty()));
    }

    @DisplayName("Должен корректно определять название книги пусто или нет")
    @ParameterizedTest
    @CsvSource(value = {",,,true", "'','','',true",",,Test,false"})
    void shouldCorrectDefineIfBookNameIsEmpty(String authorName, String genreName, String bookName, Boolean expectedValue) {
        SearchFilter actualSearchFilter = new SearchFilter(authorName, genreName, bookName);
        assertAll(() -> assertEquals(expectedValue, actualSearchFilter.isBookNameEmpty()));
    }

    @DisplayName("Должен корректно определять название жанра пусто или нет")
    @ParameterizedTest
    @CsvSource(value = {",,,true", "'','','',true",",Test,,false"})
    void shouldCorrectDefineIfGenreNameIsEmpty(String authorName, String genreName, String bookName, Boolean expectedValue) {
        SearchFilter actualSearchFilter = new SearchFilter(authorName, genreName, bookName);
        assertAll(() -> assertEquals(expectedValue, actualSearchFilter.isGenreNameEmpty()));
    }
}