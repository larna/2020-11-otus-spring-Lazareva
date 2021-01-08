package ru.otus.spring.service.ui.menu.commands;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.i18n.LocalizationService;
import ru.otus.spring.service.ui.menu.MenuService;
import ru.otus.spring.service.ui.menu.commands.CommandHandler;

@Component
@AllArgsConstructor
public class SelectLocaleCommandHandler implements CommandHandler {
    private final LocalizationService localizationService;

    @Override
    public void handle(String userChoice) {
        localizationService.selectLocale(userChoice);
    }

    @Override
    public Boolean isSuitableCommand(@NonNull String userChoice) {
        return isSelectedChangeLocaleMenuItem(userChoice);
    }

    @Override
    public Boolean isContinue() {
        return true;
    }

    private Boolean isSelectedChangeLocaleMenuItem(@NonNull String userChoice) {
        if (userChoice.isEmpty() || localizationService.getAccessibleLocales() == null)
            return false;
        return localizationService.getAccessibleLocales().contains(userChoice);
    }
}
