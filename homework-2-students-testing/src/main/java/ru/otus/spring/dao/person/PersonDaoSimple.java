package ru.otus.spring.dao.person;

import ru.otus.spring.domain.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс доступа к данным о человеке (студенте)
 */
public class PersonDaoSimple implements PersonDao {
    /**
     * Хранилище студентов
     */
    private List<Person> personStore = new ArrayList<>();

    public PersonDaoSimple() {
    }

    @Override
    public void init() {
        personStore.add(new Person("Ivanov", "Ivan", 18));
        personStore.add(new Person("Petrov", "Peter", 18));
        personStore.add(new Person("Sidorov", "Alex", 18));
    }

    /**
     * Сохранение данных о человеке
     *
     * @param student объект Person
     */
    @Override
    public void save(Person student) {
        if (personStore.contains(student)) {
            return;
        }

        personStore.add(student);
    }

    /**
     * Поиск по имени
     *
     * @param name - имя человека
     * @return объект Person
     * @throws PersonNotFoundException если объект не найден
     */
    @Override
    public Person findByNameAndSurname(String name, String surname) throws PersonNotFoundException {
        Optional<Person> foundStudent = personStore.stream()
                .filter(person -> person.getFullName().equalsIgnoreCase(Person.fullNameOf(name, surname)))
                .findFirst();

        if (foundStudent.isEmpty()) {
            throw new PersonNotFoundException(" - " + name);
        }
        return foundStudent.get();
    }

    /**
     * Найти все объекты Person
     *
     * @return список всех Person
     */
    @Override
    public List<Person> findAll() {
        return personStore;
    }
}
