package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.spring.config.props.ApplicationProps;
import ru.otus.spring.config.props.QuestionsResourceProps;
import ru.otus.spring.config.props.TestProps;
import ru.otus.spring.controller.TestController;

@SpringBootApplication
@EnableConfigurationProperties({QuestionsResourceProps.class, TestProps.class, ApplicationProps.class})
public class Main {
    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class, args);
        TestController testController = context.getBean(TestController.class);
        testController.start();
    }
}
