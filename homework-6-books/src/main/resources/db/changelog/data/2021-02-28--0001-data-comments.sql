--liquibase formatted sql

--changeset larna:2021-02-28-0001-comments
insert into comments(`id`, `description`, `book_id`)
values (1, 'Комментарий 1', 1),
       (2, 'Комментарий 2', 1)

