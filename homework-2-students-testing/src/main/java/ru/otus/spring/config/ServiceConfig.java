package ru.otus.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.dao.person.PersonDao;
import ru.otus.spring.dao.testing.QuestionDao;
import ru.otus.spring.service.person.PersonService;
import ru.otus.spring.service.person.PersonServiceImpl;
import ru.otus.spring.service.io.IOServiceConsole;
import ru.otus.spring.service.io.IOService;
import ru.otus.spring.service.testing.QuestionService;
import ru.otus.spring.service.testing.QuestionServiceImpl;

@Configuration
public class ServiceConfig {
    @Bean(destroyMethod = "close")
    public IOService ioService() {
        return new IOServiceConsole(System.in, System.out);
    }

    @Bean
    public QuestionService questionService(QuestionDao questionDao) {
        return new QuestionServiceImpl(questionDao);
    }

    @Bean
    public PersonService personService(PersonDao personDao) {
        return new PersonServiceImpl(personDao);
    }

}
