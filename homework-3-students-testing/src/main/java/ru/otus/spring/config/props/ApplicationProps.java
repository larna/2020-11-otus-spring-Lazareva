package ru.otus.spring.config.props;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Настройки приложения связанные с локализацией
 * Наверное получилось запутано, сделала соответствие сообщения в сервисе сообщению из bundle
 * Это позволило использовать общие сообщения в bundle типа ошибки для многих сервисов, если вдруг логика
 * оповещение пользователя измениться, то мы поменяем только настройки конфигурации.
 * Но получилось, что для того чтобы понять какому сообщению из сервиса соотеветствует сообщение в messages,
 * нужно сходить в application.yaml
 */
@Component
@ConfigurationProperties(prefix = "application")
@Setter
public class ApplicationProps {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationProps.class);
    private static final String DEFAULT_MESSAGE_NAME = "default";
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private Locale locale;
    @Getter
    private List<String> accessibleLocales;
    private Map<String, Map<String, String>> messages;

    public Locale getLocale() {
        return locale == null ? DEFAULT_LOCALE : locale;
    }

    public String getMessageName(@NonNull String name) {
        if (messages == null) {
            logger.error("application.messages was not found");
            return DEFAULT_MESSAGE_NAME;
        }

        int delimiterIndex = name.indexOf('_');
        if (delimiterIndex == -1) {
            logger.error("wrong name of message {}. For example: testProcess_comeback", name);
            return DEFAULT_MESSAGE_NAME;
        }

        String usageFor = name.substring(0, delimiterIndex);
        String messageName = name.substring(delimiterIndex + 1);

        Map<String, String> concreteMessagesMap = messages.getOrDefault(usageFor, Map.of());
        return concreteMessagesMap.getOrDefault(messageName, DEFAULT_MESSAGE_NAME);
    }
}
