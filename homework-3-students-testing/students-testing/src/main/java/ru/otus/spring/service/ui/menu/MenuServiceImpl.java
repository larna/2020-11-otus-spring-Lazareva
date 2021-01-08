package ru.otus.spring.service.ui.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.props.MenuProps;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.io.IOService;
import ru.otus.spring.service.ui.menu.commands.CommandHandler;

import java.io.IOException;
import java.util.List;

/**
 * Сервис обрабатыващий логику Меню
 */
@Service
public class MenuServiceImpl implements MenuService {
    private final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
    private final IOService ioService;
    /**
     * Настройки меню
     */
    private final MenuProps menuProps;
    /**
     * Обработчики команд пунктов меню.
     */
    private final List<CommandHandler> menuItemHandlers;

    public MenuServiceImpl(IOService ioService,
                           MenuProps menuProps,
                           List<CommandHandler> menuItemHandlers) {
        this.ioService = ioService;
        this.menuProps = menuProps;
        this.menuItemHandlers = menuItemHandlers;
    }

    /**
     * Показывает меню пользователю
     */
    @Override
    public void showMenu() {
        ioService.outMessage(menuProps.getTitle());
        menuProps.getItems().forEach((menuitem) -> ioService.outMessage(menuitem));
    }

    /**
     * Метод считывает команду от пользователя и находит нужный обработчиков и выполняет его
     *
     * @return возвращает флаг требуется ли продолжение
     * true - если можно продолжить
     * false - работу нужно завершить
     */
    @Override
    public Boolean handleUserChoice() {
        try {
            final String commandString = ioService.readMessage();
            CommandHandler commandHandler = menuItemHandlers.stream()
                    .filter(handler -> handler.isSuitableCommand(commandString))
                    .findFirst()
                    .orElseThrow(CommandHandlerNotFoundException::new);
            commandHandler.handle(commandString);
            return commandHandler.isContinue();
        } catch (CommandHandlerNotFoundException e) {
            ioService.outMessage(menuProps.getUnknownCommandMessage());
            return true;
        } catch (IOException e) {
            logger.error("Application error", e);
            ioService.outMessage(menuProps.getErrorMessage());
            return false;
        }
    }
}
