package ru.otus.spring.service.ui.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.props.LoginProps;
import ru.otus.spring.dao.person.PersonNotFoundException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.service.person.PersonService;

@Service
public class LoginProcessServiceImpl implements LoginProcessService {
    private static final Logger logger = LoggerFactory.getLogger(LoginProcessServiceImpl.class);
    private final PersonService personService;
    private final LoginProps loginProps;

    @Autowired
    public LoginProcessServiceImpl(PersonService personService, LoginProps loginProps) {
        this.personService = personService;
        this.loginProps = loginProps;
    }

    /**
     * Вход для студента в систему тестирования
     *
     * @return объект студент
     * @throws AccessDeniedException выбрасывается если студент не найден
     */
    @Override
    public Person login(final String userName) throws AccessDeniedException, IllegalFormatStudentNameException {
        try {
            return getPerson(userName);
        } catch (PersonNotFoundException e) {
            logger.warn(loginProps.getAccessDeniedMessage(), e);
            throw new AccessDeniedException(e);

        } catch (IllegalFormatStudentNameException e) {
            //если ввели имя в неправильном формате - только фамилию, например, просим повторить ввод
            String errorMessage = loginProps.getWrongFormatMessage();
            logger.info(errorMessage, e);
            throw e;
        }
    }

    private Person getPerson(final String fullName) throws IllegalFormatStudentNameException {
        final String[] names = fullName.split(" ");
        if (names.length < 2) {
            String errorMessage = loginProps.getWrongFormatMessage();
            throw new IllegalFormatStudentNameException(errorMessage);
        }

        final String lastName = names[0];
        final String firstName = names[1];
        return personService.getByName(firstName, lastName);
    }
}
