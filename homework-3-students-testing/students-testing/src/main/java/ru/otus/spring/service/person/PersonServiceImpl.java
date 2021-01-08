package ru.otus.spring.service.person;

import ru.otus.spring.dao.person.PersonDao;
import ru.otus.spring.domain.Person;

/**
 * Интерфейс сервиса для работы с личными данными людей
 */
public class PersonServiceImpl implements PersonService {

    private final PersonDao dao;

    public PersonServiceImpl(PersonDao dao) {
        this.dao = dao;
    }

    /**
     * Получить объект Person по имени
     *
     * @param name - имя
     * @return объект Person
     */
    public Person getByName(String name, String surname) {
        return dao.findByNameAndSurname(name, surname);
    }

    /**
     * Сохранить данные человека
     *
     * @param person - объект Person c  данными о человеке
     */
    @Override
    public void save(Person person) {
        dao.save(person);
    }
}
