package ru.otus.spring.service.ui.menu;

/**
 * Сервис обрабатыващий логику Меню
 */
public interface MenuService {
    /**
     * Отображение меню пользователю
     */
    void showMenu();

    /**
     * Обработка выбора пунктов меню пользователем
     *
     * @return возвращает флаг требуется ли продолжение
     *      true - если можно продолжить
     *      false - работу нужно завершить
     */
    Boolean handleUserChoice();
}
