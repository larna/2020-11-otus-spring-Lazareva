package ru.otus.spring.dao.person;

import ru.otus.spring.domain.Person;

import java.util.List;

public interface PersonDao {
    /**
     * Сохранение данных о человеке
     *
     * @param student объект Person
     */
    void save(Person student);

    /**
     * Поиск по имени
     *
     * @param name - имя человека
     * @return объект Person
     * @throws PersonNotFoundException если объект не найден
     */
    Person findByNameAndSurname(String name, String surname) throws PersonNotFoundException;

    /**
     * Найти все объекты Person
     *
     * @return список всех сохраненных Person
     */
    List<Person> findAll();

    void init();
}
