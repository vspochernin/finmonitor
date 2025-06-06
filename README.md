# finmonitor
Проект «Финансовый мониторинг и отчетность» по дисциплине "Проектная практика" (хакатон). 2-й семестр 1-го курса МИФИ ИИКС РПО (2024-2025 уч. г).

Подробные требования к проекту описаны в файле задания [задания](/documentation/task.md).

## Документация

Подробная документация по проекту находится в файле [документации](/documentation/documentation%20mephi%20fin_project.md).

Также возможно ознакомиться с [презентацией](/documentation/Хакатон%20презентация%20команды%20№1%20(GlobusIT).pdf).

## Запуск проекта

### Предварительные требования

1. Установленные Docker и Docker Compose.
2. Java 21 или выше.
3. Maven.

## Алгоритм запуска

Для запуска проекта нужно:

1. Находясь в корне проекта, запустить базу данных с помощью Docker:

```shell
docker compose -f src/main/resources/db/docker-compose.yml up -d  
```

2. Собрать и запустить приложение с помощью Maven:

```shell
mvn spring-boot:run
```

## Участники хакатона и зоны ответственности

- [Владислав Почернин](https://github.com/vspochernin) - тимлид.
  - Инициализация проекта.
  - Реализация регистрации и аутентификации средствами Spring Security и JWT.
  - Реализация механизма исключений и валидации.
  - Реализация логики удаления транзакций.
- [Александр Рыжиков](https://github.com/striff2001).
  - Разработка и документация пользовательских и технических требований.
  - Написание документации.
  - Создание диаграмм.
- [Даниил Ткаченко](https://github.com/Daniil1380).
  - Создание схемы базы данных.
  - Создание модели данных в коде (скелета приложения).
  - Реализация редактирования данных транзакции.
  - Выгрузка аудита БД.
- [Борис Заграй](https://github.com/zagbor).
  - Обеспечение работы на стороне клиента (Swagger).
  - Внедрение работы Spring Security.
  - Тестирование работы приложения.
- [Владимир Трубарев](https://github.com/JavaBruse).
  - Реализация фильтрации транзакций по всем представленным видам.
  - Реализация дашбордов (отчетов) по всем представленным видам.
  - Наполнение базы данных тестовыми данными.

## Соответствие функциональным требованиям

Последовательно пройдемся по всем требованиям из задания:

- В системе имеется возможность ввода данных по транзакции.
- Пользователь имеет возможность скорректировать транзакцию.
- Пользователь имеет возможность получить дашборды (отчеты) по транзакциям.
- При получении отчета имеется возможность настроить фильтрацию по всем требуемым видам фильтров.
- Имеется возможность получить каждый из указанных видов дашбордов (отчетов).
- Реализовано редактирование транзакций с ограничением возможных статусов для редактирования.
- Реализовано удаление транзакций путем изменения статуса с ограничением возможных статусов для удаления.
- Реализован механизм регистрации и аутентификации пользователей.
- Реализована валидация данных (Дата, ИНН, Телефон).
- При ведении проекта использовался Scrum (планирование, распределение задач, дейли-встречи, ретроспективы).
- Использованы базовые принципы проектирования БД.
- Сформирована и предоставлена архитектура приложения (подробно описана в файле документации).
- Сформирована логическая модель данных (пакет model).

## Итоговые артефакты демонстрации реализованного решения

1. Реализованное веб-приложение: [GitHub репозиторий приложения](https://github.com/vspochernin/finmonitor).
2. Демонстрация работы с входными данными: [видео-питч](https://drive.google.com/file/d/1RPB29l0nT5EWAWb5TTSYw3uF4k2-Agnu/view?usp=sharing).
3. Отчеты о прохождении тестирования: часть ["Сценарии тестирования"](/documentation/documentation%20mephi%20fin_project.md#сценарии-тестирования) документации.
4. Архитектура: часть ["Архитектура приложения"](/documentation/documentation%20mephi%20fin_project.md#архитектура-приложения) документации.
5. Диаграммы (БД, пользовательские): диаграммы, находящиеся в [документации](/documentation/documentation%20mephi%20fin_project.md).
6. Техническая документация: сама [документация](/documentation/documentation%20mephi%20fin_project.md).
7. Unit-тесты: [код тестов](/src/test/java/ru/hackathon/finmonitor).
8. Выгрузка из БД логов integration log: интеграции отсутствовали.
9. Выгрузка аудита БД: [аудит БД](/documentation/Аудит%20БД.pdf).