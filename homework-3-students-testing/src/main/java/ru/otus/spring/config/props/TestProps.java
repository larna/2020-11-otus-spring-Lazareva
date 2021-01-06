package ru.otus.spring.config.props;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Настройки связанные с тестированием
 */
@Component
@ConfigurationProperties(prefix = "test.pass")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestProps {
    private Integer percent;
}
