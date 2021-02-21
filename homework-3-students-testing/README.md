# Домашнее задание 3

## Перенести приложение для тестирования студентов на Spring Boot

## Цель: 
Использовать возможности Spring Boot, чтобы разрабатывать современные приложения, так, как их сейчас и разрабатывают.

## Результат: 
Production-ready приложение на Spring Boot
Это домашнее задание выполняется на основе предыдущего.

#### Требования:

1. Создать проект, используя Spring Boot Initializr (https://start.spring.io)
2. Перенести приложение проведения опросов из прошлого домашнего задания.
3. Перенести все свойства в application.yml
4. Локализовать выводимые сообщения и вопросы (в CSV-файле). MesageSource должен быть из автоконфигурации Spring Boot.
5. Сделать собственный баннер для приложения.
6. Перенести тесты и использовать spring-boot-test-starter для тестирования

*Опционально:
- использовать ANSI-цвета для баннера.
- если Ваш язык отличается от русского и английского - локализовать в нём.

###Описание:

Разбила приложение на 4 модуля:
- domain-students-testing - модель предметной области
- question-parser - для тренировки написания стартеров, вынесла функционал разбора csv файлов в отдельный модуль
- question-parser-starter - стартер для QuestionParser
- students-testing - приложение тестирования. 