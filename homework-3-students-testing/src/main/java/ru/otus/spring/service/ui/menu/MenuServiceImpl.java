package ru.otus.spring.service.ui.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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

    private static final String MENU_UNKNOWN_COMMAND = "menu_unknownCommand";
    private static final String MENU_ERROR = "menu_error";
    private static final String MENU_TITLE = "menu_title";

    private final IOService ioService;
    private final LocalizationService localizationService;
    /**
     * Пункты меню
     */
    private final List<String> menuItems;
    /**
     * Обработчики команд пунктов меню.
     */
    private final List<CommandHandler> menuItemHandlers;

    public MenuServiceImpl(IOService ioService,
                           LocalizationService localizationService,
                           List<CommandHandler> menuItemHandlers) {
        this.ioService = ioService;
        this.localizationService = localizationService;
        this.menuItemHandlers = menuItemHandlers;
        this.menuItems = List.of("menuitem_test", "menuitem_locale", "menuitem_exit");
    }

    /**
     * Показывает меню пользователю
     */
    @Override
    public void showMenu() {
        ioService.outMessage(localizationService.getMessage(MENU_TITLE));
        menuItems.forEach((menuitem) -> ioService.outMessage(localizationService.getMessage(menuitem)));
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
            ioService.outMessage(localizationService.getMessage(MENU_UNKNOWN_COMMAND));
            return true;
        } catch (IOException e) {
            logger.error("Application error", e);
            ioService.outMessage(localizationService.getMessage(MENU_ERROR));
            return false;
        }
    }
}
