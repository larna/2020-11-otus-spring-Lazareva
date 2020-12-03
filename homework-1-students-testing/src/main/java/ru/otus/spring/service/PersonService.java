package ru.otus.spring.service;

import ru.otus.spring.domain.Person;

/**
 * Интерфейс сервиса для работы с личными данными людей
 */
public interface PersonService {
    /**
     * Получить объект Person по имени
     * @param name - имя
     * @return объект Person
     */
    Person getByName(String name);

    /**
     * Сохранить данные человека
     * @param person - объект Person c  данными о человеке
     */
    void save(Person person);

}
