package ru.otus.spring.service.ui.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.person.PersonNotFoundException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.person.PersonService;
import ru.otus.spring.service.io.IOService;

import java.io.IOException;

@Service
public class LoginProcessServiceImpl implements LoginProcessService {
    private static final Logger logger = LoggerFactory.getLogger(LoginProcessServiceImpl.class);
    private static final String LOGIN_INVITE = "login_invite";
    private static final String LOGIN_WRONG_FORMAT = "login_wrongFormat";
    private static final String LOGIN_ERROR = "login_error";
    private static final String LOGIN_ACCESS_DENIED = "login_accessDenied";

    private final PersonService personService;
    private final IOService ioService;
    private final LocalizationService localizationService;

    @Autowired
    public LoginProcessServiceImpl(PersonService personService,
                                   IOService ioService,
                                   LocalizationService localizationService) {
        this.personService = personService;
        this.ioService = ioService;
        this.localizationService = localizationService;
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
            ioService.outMessage(localizationService.getMessage(LOGIN_INVITE));
            return getPerson();
        } catch (IOException | PersonNotFoundException e) {
            String messageName = e instanceof IOException ? LOGIN_ERROR : LOGIN_ACCESS_DENIED;
            String errorMessage = localizationService.getMessage(messageName);
            logger.warn(errorMessage, e);
            ioService.outMessage(errorMessage);
            throw new AccessDeniedException(e);

        } catch (IllegalFormatStudentNameException e) {
            //если ввели имя в неправильном формате - только фамилию, например, просим повторить ввод
            String errorMessage = localizationService.getMessage(LOGIN_WRONG_FORMAT);
            logger.info(errorMessage, e);
            ioService.outMessage(errorMessage);
            return login();
        }
    }

    private Person getPerson() throws IOException, IllegalFormatStudentNameException {
        final String fullName = ioService.readMessage();
        final String[] names = fullName.split(" ");
        if (names.length < 2) {
            String errorMessage = localizationService.getMessage(LOGIN_WRONG_FORMAT);
            throw new IllegalFormatStudentNameException(errorMessage);
        }

        final String lastName = names[0];
        final String firstName = names[1];
        return personService.getByName(firstName, lastName);
    }
}
