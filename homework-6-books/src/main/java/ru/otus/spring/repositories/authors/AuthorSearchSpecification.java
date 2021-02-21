package ru.otus.spring.repositories.authors;

import lombok.AllArgsConstructor;
import ru.otus.spring.controller.SearchFilter;

@AllArgsConstructor
public class AuthorSearchSpecification{
    private final SearchFilter filter;

    public Boolean isSatisfiedBy(){
        return !filter.isEmpty();
    }
    AuthorQueryBuilder buildQuery(){
        AuthorQueryBuilder builder = AuthorQueryBuilder.select();
        builder.all().fromAuthors();

        if(!filter.isBookNameEmpty() || !filter.isGenreNameEmpty()) {
            builder.innerJoinBooksAuthors()
                    .innerJoinBooks()
                    .where();

            if (!filter.isBookNameEmpty())
                builder.likeBookName(filter.getBookName());
            if (!filter.isGenreNameEmpty())
                builder.likeGenreName(filter.getGenreName());
        }else
            builder.where();

        if(!filter.isAuthorNameEmpty())
            builder.likeName(filter.getAuthorName());
        return builder;
    }
}
