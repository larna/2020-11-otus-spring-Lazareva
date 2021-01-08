package ru.otus.spring.service.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.person.PersonDao;
import ru.otus.spring.domain.Person;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Класс PersonService")
@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {
    private PersonService service;
    @Mock
    private PersonDao personDao;

    @BeforeEach
    void setUp() {
        service = new PersonServiceImpl(personDao);
    }

    @DisplayName("Корректно находит человека по имени")
    @Test
    void shouldHaveCorrectGetByName() {

        given(personDao.findByNameAndSurname(any(), any()))
                .willReturn(new Person("Ivanov", "Ivan", 18));

        Person foundPerson = service.getByName("Ivan", "Ivanov");
        assertAll(() -> assertNotNull(foundPerson),
                () -> assertEquals("Ivan", foundPerson.getName()),
                () -> assertEquals("Ivanov", foundPerson.getSurname()));
        verify(personDao, times(1)).findByNameAndSurname("Ivan", "Ivanov");
    }

    @DisplayName("Вызывает dao для сохранения человека")
    @Test
    void shouldHaveCorrectSave() {
        Person person = new Person("Ivanov", "Ivan", 19);
        service.save(person);
        verify(personDao, times(1)).save(any());
    }
}