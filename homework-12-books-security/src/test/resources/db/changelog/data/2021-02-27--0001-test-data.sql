--liquibase formatted sql

--changeset larna:2021-02-27-0001-genres
insert into genres(`id`, `name`)
values (1, 'Фантастика'),
       (2, 'Юмор'),
       (3, 'Приключения')

--changeset larna:2021-02-27-0001-authors
insert into authors (id, `name`, `real_name`, `birthday`)
values (1, 'Марк Твен', 'Сэмюэл Лэнгхорн Клеменс', '1910-04-21'),
       (2, 'О. Генри', 'Уильям Сидни Портер', '1862-09-11'),
       (3, 'Кир Булычев', 'Игорь Всеволодович Можейко', '1934-10-18'),
       (4, 'Стругацкий Аркадий Натанович', null, '1925-08-28'),
       (5, 'Стругацкий Борис Натанович', null, '1933-04-15'),
       (6, 'Роберт Стивенсон', null, '1850-11-13')

--changeset larna:2021-02-27-0001-books
insert into books(id, `name`, `genre_id`, `isbn`)
values (1, 'Трудно быть богом', 1, null),
       (2, 'Гадкие лебеди', 1, '1234'),
       (3, 'Обитаемый остров', 1, '4321'),
       (4, 'Миллион приключений', 3, null),
       (5, 'Остров сокровищ', 3, 'ISBN-1234')

--changeset larna:2021-02-27-0001-books-authors
insert into books_authors(`book_id`, `author_id`)
values (1, 4), (1, 5),
       (2, 4), (2, 5),
       (3, 4), (3, 5),
       (4, 3),
       (5, 6)

--changeset larna:2021-02-27-0001-comments
insert into comments(`id`, `description`, `book_id`)
values (1, 'комментарий 1', 5),
       (2, 'комментарий 2', 5)
