package ru.otus.spring.services;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Сервис конвертации из domain в dto и обратно. Использует ConversionService и Converter's
 * @param <T> тип domain сущности
 * @param <S> тип dto
 */
public interface BooksConversionService<T, S> {
    S toDto(T domain);

    T toDomain(S dto);

    List<S> toListOfDto(List<T> list);

    Page<S> toPageOfDto(Page<T> page);
}
