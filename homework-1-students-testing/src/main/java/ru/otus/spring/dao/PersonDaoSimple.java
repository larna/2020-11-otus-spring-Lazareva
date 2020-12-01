package ru.otus.spring.dao;

import ru.otus.spring.domain.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonDaoSimple implements PersonDao {

    private List<Person> personStore = new ArrayList<>();

    @Override
    public void save(Person student) {
        if (personStore.contains(student)) {
            return;
        }

        personStore.add(student);
    }

    public Person findByName(String name) throws PersonNotFoundException {
        Optional<Person> foundStudent = personStore.stream()
                .filter(person -> person.getName().equals(name))
                .findFirst();

        if (foundStudent.isEmpty()) {
            throw new PersonNotFoundException(" - " + name);
        }
        return foundStudent.get();
    }

    @Override
    public List<Person> findAll() {
        return personStore;
    }
}
