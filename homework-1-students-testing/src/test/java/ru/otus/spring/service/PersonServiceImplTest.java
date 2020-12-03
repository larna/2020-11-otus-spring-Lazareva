package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.PersonDao;
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

        given(personDao.findByName(any()))
                .willReturn(new Person("Ivanov", 18));

        Person foundPerson = service.getByName("Ivanov");
        assertAll(() -> assertNotNull(foundPerson),
                () -> assertEquals("Ivanov", foundPerson.getName()));
        verify(personDao, times(1)).findByName("Ivanov");
    }

    @DisplayName("Вызывает dao для сохранения человека")
    @Test
    void shouldHaveCorrectSave() {
        Person person = new Person("Ivanov", 19);
        service.save(person);
        verify(personDao, times(1)).save(any());
    }
}