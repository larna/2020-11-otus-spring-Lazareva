package ru.otus.spring.services.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.repositories.CommentRepository;

import java.util.List;

/**
 * Сервис для работы с комментариями
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
    public List<Comment> findByBookId(String bookId) {
        return repository.findAllByBook_Id(bookId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAll() {
        return repository.findAll();
    }
}
