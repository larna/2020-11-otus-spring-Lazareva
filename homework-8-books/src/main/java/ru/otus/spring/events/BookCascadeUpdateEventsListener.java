package ru.otus.spring.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.repositories.CommentRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookCascadeUpdateEventsListener extends AbstractMongoEventListener<Book> {

    private final CommentRepository commentRepository;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Book> event) {
        super.onBeforeConvert(event);
        val book = event.getSource();
        List<Comment> comments = commentRepository.findAllByBook_Id(book.getId());
        comments.stream().forEach(comment -> comment.setBook(book));
        commentRepository.saveAll(comments);
    }
}
