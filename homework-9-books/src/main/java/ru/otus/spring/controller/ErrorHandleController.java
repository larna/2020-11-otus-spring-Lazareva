package ru.otus.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.spring.services.books.BookException;

@ControllerAdvice
public class ErrorHandleController {
    private final static Logger logger = LoggerFactory.getLogger(BookController.class);
    @ExceptionHandler({BookException.class})
    public String handleErrors(Exception ex, Model model) {
        model.addAttribute("message","Problems in books...");
        logger.error("Something went wrong", ex);
        return "error";
    }
}
