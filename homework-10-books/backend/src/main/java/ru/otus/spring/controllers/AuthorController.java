package ru.otus.spring.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.domain.Author;
import ru.otus.spring.services.authors.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/authors")
    public List<Author> getAuthors(){
        return authorService.findAll();
    }
}
