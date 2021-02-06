package ru.otus.spring.config.props;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.i18n.LocalizationService;

@Component
@ConfigurationProperties(prefix = "application.test.ask-questions.select")
public class AnswerSelectQuestionProps extends AnswerQuestionProps {
    @Setter
    private String answerRepeatSelect;

    AnswerSelectQuestionProps(LocalizationService localizationService) {
        super(localizationService);
    }

    public String getRepeatToAnswer() {
        return localizationService.getMessage(answerRepeatSelect);
    }
}
