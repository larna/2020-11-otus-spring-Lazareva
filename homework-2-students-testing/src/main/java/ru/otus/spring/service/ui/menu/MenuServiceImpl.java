package ru.otus.spring.service.ui.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.spring.service.io.IOService;

import java.io.IOException;

@Service
public class MenuServiceImpl implements MenuService {
    private final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
    private final String EXIT_CONST = "exit";
    private final String IO_ERROR_MESSAGE = "IOError in menu! Application will be close. Contact to support";
    private final IOService ioService;

    public MenuServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void printMenu() {
        ioService.outMessage("***** Menu ******");
        ioService.outMessage("Press any key for test");
        ioService.outMessage(String.format("Press `%s` for exit", EXIT_CONST));
    }

    @Override
    public Command getCommand() {
        try {
            String commandString = ioService.readMessage();
            if (EXIT_CONST.equals(commandString))
                return Command.EXIT;
        } catch (IOException e) {
            logger.error(IO_ERROR_MESSAGE, e);
            ioService.outMessage(IO_ERROR_MESSAGE);
            return Command.EXIT;
        }
        return Command.CONTINUE;
    }
}
