--liquibase formatted sql

--changeset larna:2021-04-29-0001-users
insert into users(`id`, `name`, `password`)
values (1, 'user', 'user'),
       (2, 'admin', 'admin'),
       (3, 'other', 'other')

