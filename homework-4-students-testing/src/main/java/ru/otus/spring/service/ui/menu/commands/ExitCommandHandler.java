package ru.otus.spring.service.ui.menu.commands;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.i18n.LocalizationService;

@Component
public class ExitCommandHandler implements CommandHandler {
    private final @NonNull String exitCommand;

    public ExitCommandHandler(@Value("${application.menu.commands.exit}") String exitCommand) {
        this.exitCommand = exitCommand;
    }

    @Override
    public void handle(String userChoice) {
    }

    @Override
    public Boolean isSuitableCommand(@NonNull String userChoice) {
        if (exitCommand.equals(userChoice))
            return true;
        return false;
    }

    @Override
    public Boolean isContinue() {
        return false;
    }
}
