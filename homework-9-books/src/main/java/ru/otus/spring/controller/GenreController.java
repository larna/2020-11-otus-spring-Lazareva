package ru.otus.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.services.genres.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genres")
    public String getGenres(Model model){
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "genres/genres";
    }
}
