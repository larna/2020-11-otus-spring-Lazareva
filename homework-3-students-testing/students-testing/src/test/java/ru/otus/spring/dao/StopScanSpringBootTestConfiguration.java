package ru.otus.spring.dao;

import org.springframework.boot.SpringBootConfiguration;

/**
 * Останавливаю сканирование @SpringBootTest для того
 * чтобы он не создавал мне весь контекст
 */
@SpringBootConfiguration
public class StopScanSpringBootTestConfiguration {
}
