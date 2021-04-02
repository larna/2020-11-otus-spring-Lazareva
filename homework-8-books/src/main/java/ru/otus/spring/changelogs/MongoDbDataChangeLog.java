package ru.otus.spring.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Order(value=1)
@ChangeLog(order = "001")
public class MongoDbDataChangeLog {
    private Genre fantasyGenre;
    private Genre humorGenre;
    private Genre adventuresGenre;
    private Map<String, Author> authors = new HashMap<>();
    private Book book;


    @ChangeSet(order = "000", systemVersion = "1", id = "dropDB", author = "larna", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", systemVersion = "1", id = "initGenres", author = "larna", runAlways = true)
    public void initGenres(MongockTemplate template){
        fantasyGenre = template.save(Genre.builder().name("Фантастика").build());
        humorGenre = template.save(Genre.builder().name("Юмор").build());
        adventuresGenre = template.save(Genre.builder().name("Приключения").build());
    }
    @ChangeSet(order = "002", systemVersion = "1", id = "initAuthors", author = "larna", runAlways = true)
    public void initAuthors(MongockTemplate template){

        authors.put("Mark_Twen", getAuthor("Марк Твен",
                "Сэмюэл Лэнгхорн Клеменс", LocalDate.of(1910,4,21)));
        authors.put("O_Henry", getAuthor("О. Генри",
                "Уильям Сидни Портер", LocalDate.of(1862,9,11)));
        authors.put("Kir_Bulichev", getAuthor("Кир Булычев",
                "Игорь Всеволодович Можейко", LocalDate.of(1934,10,18)));
        authors.put("Strugackij_A", getAuthor("Стругацкий Аркадий Натанович", null,
                LocalDate.of(1925,8,28)));
        authors.put("Strugackij_B", getAuthor("Стругацкий Борис Натанович", null,
                LocalDate.of(1933,4,15)));
        authors.put("Stivenson", getAuthor("Роберт Стивенсон", null,
                LocalDate.of(1850,11,13)));
        authors.values().stream().forEach(author -> template.save(author));
    }
    @ChangeSet(order = "003", systemVersion = "1", id = "initBooks", author = "larna", runAlways = true)
    public void initBooks(MongockTemplate template){
        book = Book.builder()
                .name("Трудно быть богом")
                .genre(fantasyGenre)
                .authors(List.of(authors.get("Strugackij_A"),authors.get("Strugackij_B")))
                .build();
        template.save(book);
        template.save(Book.builder()
                .name("Гадкие лебеди")
                .isbn("1234")
                .genre(fantasyGenre)
                .authors(List.of(authors.get("Strugackij_A"),authors.get("Strugackij_B")))
                .build());
        template.save(Book.builder()
                .name("Обитаемый остров")
                .isbn("4321")
                .genre(fantasyGenre)
                .authors(List.of(authors.get("Strugackij_A"),authors.get("Strugackij_B")))
                .build());
        template.save(Book.builder()
                .name("Миллион приключений")
                .genre(fantasyGenre)
                .authors(List.of(authors.get("Kir_Bulichev")))
                .build());
        template.save(Book.builder()
                .name("Остров сокровищ")
                .genre(adventuresGenre)
                .authors(List.of(authors.get("Stivenson")))
                .build());
        template.save(Book.builder()
                .name("Приключения Тома Соейра")
                .genre(adventuresGenre)
                .authors(List.of(authors.get("Mark_Twen")))
                .build());
        template.save(Book.builder()
                .name("Трест, который лопнул")
                .genre(humorGenre)
                .authors(List.of(authors.get("O_Henry")))
                .build());
    }
    @ChangeSet(order = "004", systemVersion = "1", id = "initComments", author = "larna", runAlways = true)
    public void initComments(MongockTemplate template){
        template.save(Comment.builder().book(book).description("Comment!").build());
        template.save(Comment.builder().book(book).description("Other comment!").build());
    }
    private Author getAuthor(String name, String realName, LocalDate birthday){
        return Author.builder()
                .name(name)
                .realName(realName)
                .birthday(birthday)
                .build();
    }
}
