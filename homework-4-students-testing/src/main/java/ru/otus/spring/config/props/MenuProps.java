package ru.otus.spring.config.props;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.i18n.LocalizationService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Настройки приложения меню
 */
@Component
@ConfigurationProperties(prefix = "application.menu")
@Setter
public class MenuProps extends BaseMessagesProps {
    private static final String TITLE_DEFAULT = "*********** Menu *************";
    private static final String ERROR_DEFAULT = "Menu error";
    private static final String UNKNOWN_COMMAND_DEFAULT = "Unknown command...";

    private List<String> items;
    private Map<String, String> commands;

    public MenuProps(LocalizationService localizationService) {
        super(localizationService);
    }

    public List<String> getItems() {
        return items.stream()
                .map(item -> {
                    String commandName = item.substring(item.lastIndexOf(".") + 1);
                    String arg = commands.get(commandName);
                    return localizationService.getMessage(item, arg);
                }).collect(Collectors.toList());
    }

    public String getErrorMessage() {
        return getMessage("error", ERROR_DEFAULT);
    }

    public String getUnknownCommandMessage() {
        return getMessage("unknownCommand", UNKNOWN_COMMAND_DEFAULT);
    }

    public String getTitle() {
        return getMessage("title", TITLE_DEFAULT);
    }
}
