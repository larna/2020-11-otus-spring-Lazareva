package ru.otus.spring.config.props;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import ru.otus.spring.service.i18n.LocalizationService;

import java.util.Map;


class BaseMessagesProps {
    private static final Logger logger = LoggerFactory.getLogger(BaseMessagesProps.class);
    protected final LocalizationService localizationService;
    @Setter
    private Map<String, String> messages;

    BaseMessagesProps(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }
    /**
     * Получение сообщений
     *
     * @param name           название соответствующее идентификатору в messages
     * @param defaultMessage сообщение по-умолчанию
     * @return
     */
    String getMessage(String name, String defaultMessage) {
        String messageName = messages.getOrDefault(name, "");
        if (messageName.isEmpty())
            return defaultMessage;
        try {
            return localizationService.getMessage(messageName);
        } catch (NoSuchMessageException e) {
            logger.warn("no message for {}", name);
            return defaultMessage;
        }
    }

    /**
     * Получение сообщений c параметрами, типа: Сообщение для {0}
     *
     * @param name           название соответствующее идентификатору в messages
     * @param defaultMessage сообщение по-умолчанию
     * @param args           аргументы
     * @return
     */
    String getMessage(String name, String defaultMessage, String... args) {
        String messageName = messages.getOrDefault(name, "");
        if (messageName.isEmpty())
            return defaultMessage;
        try {
            return localizationService.getMessage(messageName, args);
        } catch (NoSuchMessageException e) {
            logger.warn("no message for {}", name);
            return defaultMessage;
        }
    }
}
