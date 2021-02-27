package ru.otus.spring.services.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.repositories.CommentRepository;
import ru.otus.spring.services.books.BookNotFoundException;
import ru.otus.spring.services.books.BookService;

import java.util.List;

/**
 * Сервис для работы с комментариями к книге
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;

    @Transactional
    @Override
    public Comment save(Comment comment) {
        return repository.save(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Comment findById(Long id) {
        return repository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAll() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public void deleteById(Long commentId) {
        if (!repository.existsById(commentId))
            throw new CommentNotFoundException();
        repository.deleteById(commentId);
    }

}
