package ru.otus.spring.config.props;

import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Person;
import ru.otus.spring.service.i18n.LocalizationService;

/**
 * Настройки связанные с тестированием
 */
@Component
@ConfigurationProperties(prefix = "application.test.process")
public class TestProcessProps extends BaseMessagesProps {
    private static final String COMEBACK_DEFAULT = "Pardon, you already pass the test today. Come back tomorrow.";
    private static final String ERROR_DEFAULT = "Test process error";

    TestProcessProps(LocalizationService localizationService) {
        super(localizationService);
    }

    public String getComeBackMessage() {
        return getMessage("comeback", COMEBACK_DEFAULT);
    }

    public String getErrorMessage() {
        return getMessage("error", ERROR_DEFAULT);
    }

    public String getStartTestMessage(@NonNull Person student) {
        return getMessage("start", "Test started for student "+student.getFullName(), student.getFullName());
    }
}
