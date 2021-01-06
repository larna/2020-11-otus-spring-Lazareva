package ru.otus.spring.service.i18n;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.props.ApplicationProps;

import java.util.List;
import java.util.Locale;

/**
 * Сервис локализации выводимых сообщений
 */
@Service
public class LocalizationServiceImpl implements LocalizationService {
    private final MessageSource messageSource;
    private final ApplicationProps applicationProps;

    public LocalizationServiceImpl(MessageSource messageSource, ApplicationProps applicationProps) {
        this.messageSource = messageSource;
        this.applicationProps = applicationProps;
    }
    /**
     * Получение сообщения на нужном языке по идентификатору
     * @param name название (идентификатор) сообщения: testProcess_error
     * @param args параметры для сообщений типа: Message content with arguments {0}, {1} ...
     * @return локализованное сообщение
     */
    @Override
    public String getMessage(String name, String... args) {
        String messageName = applicationProps.getMessageName(name);
        String[] arguments = (args == null) ? new String[]{} : args;
        return messageSource.getMessage(messageName, arguments, applicationProps.getLocale());
    }
    /**
     * Список доступных локалей
     * @return
     */
    @Override
    public List<String> getAccessibleLocales() {
        return applicationProps.getAccessibleLocales();
    }
    /**
     * Получить текущую локаль
     * @return
     */
    @Override
    public Locale getCurrentLocale() {
        return applicationProps.getLocale();
    }
    /**
     * Установить локаль
     * @param localeName language tag: ru, en ....
     */
    @Override
    public void selectLocale(String localeName) {
        if (localeName == null)
            return;
        applicationProps.setLocale(Locale.forLanguageTag(localeName));
    }
}
