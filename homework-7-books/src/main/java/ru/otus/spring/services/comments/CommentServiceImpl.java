package ru.otus.spring.services.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.repositories.CommentRepository;
import ru.otus.spring.services.books.BookNotFoundException;
import ru.otus.spring.services.books.BookService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final BookService bookService;

    @Override
    public Comment save(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public List<Comment> findByBook(Long bookId) {
        return repository.findAllByBook_Id(bookId);
    }

    @Override
    public Comment findById(Long id) {
        Comment comment = repository.findById(id).orElseThrow(CommentNotFoundException::new);
        Book book = bookService.findById(comment.getBook().getId());
        comment.setBook(book);
        return comment;
    }

    @Override
    public List<Comment> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long idComment) {
        if (!repository.existsById(idComment))
            throw new CommentNotFoundException();
        repository.deleteById(idComment);
    }
}
