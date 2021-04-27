package ru.otus.spring.controllers.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.spring.controllers.BookController;

@RestControllerAdvice
public class ResponseErrorHandler {
    private final static Logger logger = LoggerFactory.getLogger(BookController.class);

    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<Object> handleError(ResponseStatusException ex) {
        logger.error("Handle error", ex);
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleUnknownError(Exception ex) {
        logger.error("Unknown error", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
