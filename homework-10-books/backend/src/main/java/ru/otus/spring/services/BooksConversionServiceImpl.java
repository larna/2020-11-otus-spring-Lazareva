package ru.otus.spring.services;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис конвертации из domain в dto и обратно. Использует ConversionService и Converter's
 * @param <T> тип domain сущности
 * @param <S> тип dto
 */
public class BooksConversionServiceImpl<T, S> implements BooksConversionService<T, S> {
    private final ConversionService conversionService;
    private final Class<T> domainType;
    private final Class<S> dtoType;

    public BooksConversionServiceImpl(ConversionService conversionService, Class<T> domainType, Class<S> dtoType) {
        this.conversionService = conversionService;
        this.domainType = domainType;
        this.dtoType = dtoType;
    }

    @Override
    public S toDto(T domain) {
        return conversionService.convert(domain, dtoType);
    }

    @Override
    public T toDomain(S dto) {
        return conversionService.convert(dto, domainType);
    }

    @Override
    public List<S> toListOfDto(List<T> list) {
        if (list == null)
            return List.of();
        return list.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<S> toPageOfDto(Page<T> page) {
        if (page.isEmpty())
            return Page.empty();

        List<S> content = page.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
}

