package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.spring.domain.Author;
import ru.otus.spring.services.authors.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/authors")
    public String getGenres(Model model){
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "authors/authors";
    }
}
