package ru.otus.spring.controller.ui.views;

import java.util.List;

/**
 * Представление списков для пользователя
 * @param <T>
 */
public interface View<T> {
    /**
     * Отображение списка
     * @param list список
     * @param message сообщение
     * @return отображение списка
     */
    String getListView(List<T> list, String message);

    /**
     * Отображение объекта в виде таблицы с заголовком и содержащим единственную строку с информацией из переданного объекта
     * @param object объект для отображения
     * @param message сообщение пользователю
     * @return отображение объекта
     */
    String getObjectView(T object, String message);
}
