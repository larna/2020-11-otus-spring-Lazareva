package ru.otus.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import ru.otus.spring.controllers.dto.BookDto;
import ru.otus.spring.controllers.dto.CommentDto;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.services.BooksConversionService;
import ru.otus.spring.services.BooksConversionServiceImpl;

@Configuration
public class ConversionConfig {
    @Bean
    BooksConversionService<Comment, CommentDto> commentConversionService(ConversionService conversionService){
        return new BooksConversionServiceImpl<>(conversionService, Comment.class, CommentDto.class);
    }
    @Bean
    BooksConversionService<Book, BookDto> bookConversionService(ConversionService conversionService){
        return new BooksConversionServiceImpl<>(conversionService, Book.class, BookDto.class);
    }
}
