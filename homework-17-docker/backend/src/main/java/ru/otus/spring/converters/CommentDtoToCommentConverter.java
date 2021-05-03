package ru.otus.spring.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.controllers.dto.CommentDto;
import ru.otus.spring.domain.Comment;

@Component
@RequiredArgsConstructor
public class CommentDtoToCommentConverter implements Converter<CommentDto, Comment> {

    @Override
    public Comment convert(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .description(commentDto.getDescription())
                .build();
    }
}
