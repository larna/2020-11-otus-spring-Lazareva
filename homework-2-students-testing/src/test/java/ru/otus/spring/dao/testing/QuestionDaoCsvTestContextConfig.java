package ru.otus.spring.dao.testing;

import org.mockito.Mockito;
import org.springframework.context.annotation.*;
import ru.otus.spring.service.testing.QuestionService;
import ru.otus.spring.service.testing.TestService;
import ru.otus.spring.util.testing.QuestionParser;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackageClasses = { QuestionDao.class, QuestionParser.class})
public class QuestionDaoCsvTestContextConfig {
}