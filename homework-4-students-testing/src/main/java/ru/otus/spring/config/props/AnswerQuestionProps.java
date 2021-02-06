package ru.otus.spring.config.props;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.i18n.LocalizationService;

@Component
@ConfigurationProperties(prefix = "application.test.ask-questions.input")
public class AnswerQuestionProps {
    protected final LocalizationService localizationService;
    @Setter
    private String inviteToAnswer;

    AnswerQuestionProps(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    public String getInviteToAnswer() {
        return localizationService.getMessage(inviteToAnswer);
    }
}
