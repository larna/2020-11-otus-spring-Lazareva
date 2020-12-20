package ru.otus.spring.service.ui.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.person.PersonNotFoundException;
import ru.otus.spring.domain.Person;
import ru.otus.spring.service.person.PersonService;
import ru.otus.spring.service.io.IOService;

import java.io.IOException;

@Service
public class LoginProcessServiceImpl implements LoginProcessService {
    private static final Logger logger = LoggerFactory.getLogger(LoginProcessServiceImpl.class);
    private static final String LOGIN_MESSAGE = "Enter your full name (for example: Petrov Peter):";
    private static final String WRONG_FORMAT_MESSAGE = "Full name has wrong format. It must be: surname firstname, for example: Petrov Peter";
    private static final String IO_ERROR_MESSAGE = "Error of input/output. Try to reset application or pass the test later.";
    private static final String ACCESS_DENIED_MESSAGE = "Student not found. Contact to support.\n\n";

    private final PersonService personService;
    private final IOService ioService;

    @Autowired
    public LoginProcessServiceImpl(PersonService personService, IOService ioService) {
        this.personService = personService;
        this.ioService = ioService;
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
            ioService.outMessage(LOGIN_MESSAGE);
            return getPerson();
        } catch (IOException | PersonNotFoundException e) {
            logger.warn(e instanceof IOException ? IO_ERROR_MESSAGE : ACCESS_DENIED_MESSAGE, e);
            ioService.outMessage(e instanceof IOException ? IO_ERROR_MESSAGE : ACCESS_DENIED_MESSAGE);
            throw new AccessDeniedException(e);

        } catch (IllegalFormatStudentNameException e) {
            //если ввели имя в неправильном формате - только фамилию, например, просим повторить ввод
            logger.info(WRONG_FORMAT_MESSAGE, e);
            ioService.outMessage(WRONG_FORMAT_MESSAGE);
            return login();
        }
    }

    private Person getPerson() throws IOException, IllegalFormatStudentNameException {
        final String fullName = ioService.readMessage();
        final String[] names = fullName.split(" ");
        if (names.length < 2)
            throw new IllegalFormatStudentNameException(WRONG_FORMAT_MESSAGE);

        final String lastName = names[0];
        final String firstName = names[1];
        return personService.getByName(firstName, lastName);
    }
}
