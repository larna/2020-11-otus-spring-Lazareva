package ru.otus.spring.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.i18n.LocalizationService;

@Component
@ConfigurationProperties(prefix = "application.login")
public class LoginProps extends BaseMessagesProps {
    private static final String INVITE_MESSAGE_DEFAULT = "Enter your full name (for example: Petrov Peter):";
    private static final String WRONG_FORMAT_DEFAULT = "Full name has wrong format. It must be: surname firstname, for example: Petrov Peter";
    private static final String ACCESS_DENIED_DEFAULT = "Student not found. Contact to support.\n";
    private static final String ERROR_DEFAULT = "Login error.";

    public LoginProps(LocalizationService localizationService) {
        super(localizationService);
    }

    public String getErrorMessage() {
        return getMessage("error", ERROR_DEFAULT);
    }

    public String getInviteMessage() {
        return getMessage("invite", INVITE_MESSAGE_DEFAULT);
    }

    public String getAccessDeniedMessage() {
        return getMessage("accessDenied", ACCESS_DENIED_DEFAULT);
    }

    public String getWrongFormatMessage() {
        return getMessage("wrongFormat", WRONG_FORMAT_DEFAULT);
    }
}
