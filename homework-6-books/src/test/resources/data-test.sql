DELETE
from books_authors;
DELETE
from books;
DELETE
from authors;
DELETE
from genres;

insert into authors (id, `name`, `real_name`, `birthday`)
values (1, 'Марк Твен', 'Сэмюэл Лэнгхорн Клеменс', '1910-04-21');
insert into authors (id, `name`, `real_name`, `birthday`)
values (2, 'О. Генри', 'Уильям Сидни Портер', '1862-09-11');
insert into authors (id, `name`, `real_name`, `birthday`)
values (3, 'Кир Булычев', 'Игорь Всеволодович Можейко', '1934-10-18');
insert into authors (id, `name`, `real_name`, `birthday`)
values (4, 'Стругацкий Аркадий Натанович', null, '1925-08-28');
insert into authors (id, `name`, `real_name`, `birthday`)
values (5, 'Стругацкий Борис Натанович', null, '1933-04-15');
insert into authors (id, `name`, `real_name`, `birthday`)
values (6, 'Роберт Стивенсон', null, '1850-11-13');

insert into genres(`id`, `name`)
values (1, 'Фантастика');
insert into genres(`id`, `name`)
values (2, 'Юмор');
insert into genres(`id`, `name`)
values (3, 'Приключения');

insert into books(id, `name`, `genre_id`)
values (1, 'Трудно быть богом', 1);
insert into books(id, `name`, `genre_id`, `isbn`)
values (2, 'Гадкие лебеди', 1, '1234');
insert into books(id, `name`, `genre_id`, `isbn`)
values (3, 'Обитаемый остров', 1, '4321');
insert into books(id, `name`, `genre_id`)
values (4, 'Миллион приключений', 3);
insert into books(id, `name`, `genre_id`, `isbn`)
values (5, 'Остров сокровищ', 3, 'ISBN-1234');

insert into books_authors(`book_id`, `author_id`)
values (1, 4);
insert into books_authors(`book_id`, `author_id`)
values (1, 5);
insert into books_authors(`book_id`, `author_id`)
values (2, 4);
insert into books_authors(`book_id`, `author_id`)
values (2, 5);
insert into books_authors(`book_id`, `author_id`)
values (3, 4);
insert into books_authors(`book_id`, `author_id`)
values (3, 5);

insert into books_authors(`book_id`, `author_id`)
values (4, 3);

insert into books_authors(`book_id`, `author_id`)
values (5, 6);

insert into comments(`id`, `description`, `book_id`)
values (1, 'комментарий 1', 5);
insert into comments(`id`, `description`, `book_id`)
values (2, 'комментарий 2', 5);

alter sequence BOOKS_SEQUENCE restart with 6;