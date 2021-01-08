package ru.otus.spring.service.ui.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.props.LoginProps;
import ru.otus.spring.dao.person.PersonNotFoundException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.person.PersonService;
import ru.otus.spring.service.io.IOService;

import java.io.IOException;

@Service
public class LoginProcessServiceImpl implements LoginProcessService {
    private static final Logger logger = LoggerFactory.getLogger(LoginProcessServiceImpl.class);
    private final PersonService personService;
    private final IOService ioService;
    private final LoginProps loginProps;

    @Autowired
    public LoginProcessServiceImpl(PersonService personService, IOService ioService, LoginProps loginProps) {
        this.personService = personService;
        this.ioService = ioService;
        this.loginProps = loginProps;
    }

    /**
     * Вход для студента в систему тестирования
     *
     * @return объект студент
     * @throws AccessDeniedException выбрасывается если студент не найден
     */
    @Override
    public Person login() throws AccessDeniedException {
        try {
            ioService.outMessage(loginProps.getInviteMessage());
            return getPerson();
        } catch (IOException | PersonNotFoundException e) {
            String errorMessage = e instanceof IOException ? loginProps.getErrorMessage() : loginProps.getAccessDeniedMessage();
            logger.warn(errorMessage, e);
            ioService.outMessage(errorMessage);
            throw new AccessDeniedException(e);

        } catch (IllegalFormatStudentNameException e) {
            //если ввели имя в неправильном формате - только фамилию, например, просим повторить ввод
            String errorMessage = loginProps.getWrongFormatMessage();
            logger.info(errorMessage, e);
            ioService.outMessage(errorMessage);
            return login();
        }
    }

    private Person getPerson() throws IOException, IllegalFormatStudentNameException {
        final String fullName = ioService.readMessage();
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
