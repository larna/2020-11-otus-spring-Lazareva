package ru.otus.spring.service.i18n;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.props.LocalizationProps;

import java.util.List;
import java.util.Locale;

/**
 * Сервис локализации выводимых сообщений
 */
@Service
public class LocalizationServiceImpl implements LocalizationService {
    private final MessageSource messageSource;
    private final LocalizationProps localizationProps;

    public LocalizationServiceImpl(MessageSource messageSource, LocalizationProps localizationProps) {
        this.messageSource = messageSource;
        this.localizationProps = localizationProps;
    }

    /**
     * Получение сообщения на нужном языке по идентификатору
     *
     * @param name название (идентификатор) сообщения из файла messages в bundle (например: test.error)
     * @param args параметры для сообщений типа: Message content with arguments {0}, {1} ...
     * @return локализованное сообщение
     */
    @Override
    public String getMessage(String name, String... args) {
        String[] arguments = (args == null) ? new String[]{} : args;
        return messageSource.getMessage(name, arguments, localizationProps.getLocale());
    }

    /**
     * Список доступных локалей
     *
     * @return
     */
    @Override
    public List<String> getAccessibleLocales() {
        return localizationProps.getAccessibleLocales();
    }

    /**
     * Получить текущую локаль
     *
     * @return
     */
    @Override
    public Locale getCurrentLocale() {
        return localizationProps.getLocale();
    }

    /**
     * Установить локаль
     *
     * @param localeName language tag: ru, en ....
     */
    @Override
    public void selectLocale(String localeName) {
        if (localeName == null)
            return;
        localizationProps.setLocale(Locale.forLanguageTag(localeName));
    }
}
