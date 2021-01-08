package ru.otus.spring.service.i18n;

import java.util.List;
import java.util.Locale;

/**
 * Сервис локализации выводимых сообщений
 */
public interface LocalizationService {
    /**
     * Получение сообщения на нужном языке по идентификатору
     *
     * @param name название (идентификатор) сообщения из файла messages в bundle (например: test.error)
     * @param args параметры для сообщений типа: Message content with arguments {0}, {1} ...
     * @return локализованное сообщение
     */
    String getMessage(String name, String... args);

    /**
     * Список доступных локалей
     *
     * @return
     */
    List<String> getAccessibleLocales();

    /**
     * Получить текущую локаль
     *
     * @return
     */
    Locale getCurrentLocale();

    /**
     * Установить локаль
     *
     * @param localeName language tag: ru, en ....
     */
    void selectLocale(String localeName);
}
