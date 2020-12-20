package ru.otus.spring.service.ui.login;

import ru.otus.spring.domain.Person;

public interface LoginProcessService {
    Person login() throws AccessDeniedException;
}
