package ru.otus.spring.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.spring.controllers.dto.CommentDto;
import ru.otus.spring.domain.Comment;

@Component
@RequiredArgsConstructor
public class CommentToCommentDtoConverter implements Converter<Comment, CommentDto> {

    @Override
    public CommentDto convert(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .build();
    }
}
