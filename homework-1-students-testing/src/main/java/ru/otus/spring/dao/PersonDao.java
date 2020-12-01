package ru.otus.spring.dao;

import ru.otus.spring.domain.Person;

import java.util.List;

public interface PersonDao {

    void save(Person student);

    Person findByName(String name) throws PersonNotFoundException;

    List<Person> findAll();
}
