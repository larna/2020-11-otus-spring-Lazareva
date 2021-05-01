--liquibase formatted sql

--changeset larna:2021-02-28-0001-comments
insert into comments(`id`, `description`, `book_id`)
values (3, 'Комментарий 1', 2),
       (4, 'Комментарий 2', 2),
       (5, 'Комментарий 3', 2)

