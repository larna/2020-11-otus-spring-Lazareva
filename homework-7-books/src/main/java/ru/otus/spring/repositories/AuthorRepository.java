package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;

import java.util.List;

/**
 * Репозиторий для работы с авторами
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    /**
     * найти авторов по имени или части имени
     * @param authorName
     * @return
     */
    List<Author> findAllByNameLike(String authorName);

    /**
     * найти авторов по списку c id авторов
     * @param idList
     * @return
     */
    List<Author> findAllByIdIn(List<Long> idList);
}
