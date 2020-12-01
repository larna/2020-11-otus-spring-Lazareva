package ru.otus.spring.service;

import ru.otus.spring.dao.PersonDao;
import ru.otus.spring.domain.Person;

public class PersonServiceImpl implements PersonService {

    public void setDao(PersonDao dao) {
        this.dao = dao;
    }

    private PersonDao dao;

    public PersonServiceImpl() {
    }

    public Person getByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public void save(Person person) {
        dao.save(person);
    }
}
