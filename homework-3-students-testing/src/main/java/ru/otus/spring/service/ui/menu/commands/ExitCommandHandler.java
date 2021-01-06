package ru.otus.spring.service.ui.menu.commands;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.i18n.LocalizationService;

@Component
@AllArgsConstructor
public class ExitCommandHandler implements CommandHandler {
    private static final String MENU_COMMAND_EXIT_TEXT = "menu_commandExit";
    private final LocalizationService localizationService;

    @Override
    public void handle(String userChoice) {
    }

    @Override
    public Boolean isSuitableCommand(@NonNull String userChoice) {
        if (localizationService.getMessage(MENU_COMMAND_EXIT_TEXT).equals(userChoice))
            return true;
        return false;
    }

    @Override
    public Boolean isContinue() {
        return false;
    }
}
