package ru.otus.spring.dao.person;

import org.assertj.core.data.Index;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.dao.person.PersonDao;
import ru.otus.spring.dao.person.PersonDaoSimple;
import ru.otus.spring.domain.Person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс PersonDaoSimple")
class PersonDaoSimpleTest {

    private PersonDao personDao;

    @BeforeEach
    void setUp() {
        personDao = new PersonDaoSimple();
    }

    @DisplayName("Корректно сохраняет человека")
    @Test
    void shouldHaveCorrectSave() {
        Person student = new Person("Ivanov", "Ivan", 18);
        personDao.save(student);
        assertThat(personDao.findAll())
                .hasSize(1)
                .contains(student);
    }

    @DisplayName("Находит человека по имени")
    @Test
    void shouldHaveCorrectFindByName() {
        Person student = new Person("Ivanov", "Ivan", 18);
        personDao.save(student);
        assertEquals(student, personDao.findByNameAndSurname("Ivan","Ivanov"));
    }

    @DisplayName("Показывает всех людей")
    @Test
    void shouldHaveCorrectFindAll() {
        Person student1 = new Person("Ivanov", "Ivan", 18);
        Person student2 = new Person("Petrov", "Peter", 19);
        personDao.save(student1);
        personDao.save(student2);
        assertThat(personDao.findAll())
                .hasSize(2)
                .contains(student1, Index.atIndex(0))
                .contains(student2, Index.atIndex(1));
    }
}