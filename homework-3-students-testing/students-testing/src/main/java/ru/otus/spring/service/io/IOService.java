package ru.otus.spring.service.io;

import java.io.IOException;

/**
 * Сервис ввода/вывода
 */
public interface IOService {
    /**
     * Метод чтения данных
     * @return прочитанную строку
     * @throws IOException в случае ошибки ввод/вывода выбрасывает исключение
     */
    String readMessage() throws IOException;

    /**
     * Метод вывода данных
     * @param message строка для вывода
     */
    void outMessage(String message);

    void close();
}
