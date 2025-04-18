# finmonitor
Проект «Финансовый мониторинг и отчетность» по дисциплине "Проектная практика" (хакатон). 2-й семестр 1-го курса МИФИ ИИКС РПО (2024-2025 уч. г).

### Создание базы данных в докер контейнере:
```shell
docker run --name FinmonitorDB   -e POSTGRES_USER=postgres   -e POSTGRES_PASSWORD=postgres   -e POSTGRES_DB=finmonitor   -p 5432:5432   -d postgres:13
```