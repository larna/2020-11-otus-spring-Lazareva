package ru.otus.spring.controller.convertors.comment;

import org.springframework.core.convert.converter.Converter;
import ru.otus.spring.controller.dto.CommentDto;
import ru.otus.spring.domain.Comment;

public class CommentToDtoConverter implements Converter<Comment, CommentDto> {
    @Override
    public CommentDto convert(Comment comment) {
        if (comment == null)
            return null;

        return CommentDto.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .build();
    }
}
