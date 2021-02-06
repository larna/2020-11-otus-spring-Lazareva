package ru.otus.spring.service.ui.login;

import ru.otus.spring.domain.Person;

/**
 * Сервис логирования
 */
public interface LoginProcessService {
    /**
     * Метод авторизации
     * @return объект студент, если такой найден или выбрасывает исключение
     * @throws AccessDeniedException - выбрасывается, если студент не найден
     */
    Person login(String userName) throws AccessDeniedException, IllegalFormatStudentNameException;
}

