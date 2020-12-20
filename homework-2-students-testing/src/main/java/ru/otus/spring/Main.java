package ru.otus.spring;

import org.springframework.context.annotation.*;
import ru.otus.spring.config.DaoConfig;
import ru.otus.spring.config.ServiceConfig;
import ru.otus.spring.controller.TestController;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        TestController testController = context.getBean(TestController.class);
        testController.start();
    }
}
