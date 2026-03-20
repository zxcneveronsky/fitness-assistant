# Fitness Assistant

REST API для поиска упражнений по группам мышц и получения информации о продуктах питания по штрихкоду.

## Требования

- Java 21
- Maven 3.9+
- PostgreSQL 17+

## Запуск локально

1. Установи PostgreSQL и создай базу данных:
```sql
CREATE DATABASE fitness_assistant;
```

2. Настрой переменную окружения:
```
DB_PASSWORD=твой_пароль
CSV_DATA_PATH: абсолютный путь к папке с CSV-файлами (например, /home/user/project/src/main/resources/data/).
```
Важно: Flyway использует команду COPY, поэтому PostgreSQL должен иметь доступ на чтение к этой директории.

3. Запусти приложение:
```bash
./mvnw spring-boot:run
```

Приложение запустится на `http://localhost:8080`

При старте Flyway автоматически создаст таблицы и заполнит их данными из CSV файлов в `resources/data/` (Расположение таблиц по умолчанию).

## Где хранить секреты

Пароль от БД хранится в переменных окружения — **никогда не коммить пароль в git**.

В IntelliJ: Edit Configurations → Environment variables → `DB_PASSWORD=твой_пароль`

## Эндпоинты

### Здоровье сервиса
```
GET /api/health
```

### Упражнения
```
GET    /api/v1/exercises                          — все упражнения
GET    /api/v1/exercises/paged?page=0&size=10     — с пагинацией
GET    /api/v1/exercises/muscle/{muscle}          — поиск по мышце
GET    /api/v1/exercises/exercise/{exerciseName}  — поиск мышц по упражнению
POST   /api/v1/exercises                          — добавить упражнение
DELETE /api/v1/exercises/{exerciseName}           — удалить упражнение
```

### Еда
```
GET /api/v1/food/{barcode} — получить данные по штрихкоду
PUT /api/v1/food/{barcode} — обновить данные продукта
```

## Коды ответов

- `200` — успех
- `201` — создано
- `204` — удалено
- `400` — неверные данные
- `404` — не найдено

## Порты

- Приложение: `8080`
- PostgreSQL: `5432`

## Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

## Команда

Стафеев Григорий, Синянский Вениамин, Онучин Тимофей.
Проект Т-Класс, 130 лицей, Екатеринбург.
