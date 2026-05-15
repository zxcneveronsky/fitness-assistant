# Fitness Assistant

REST API для фитнес-трекинга: учёт тренировок, питания, гидратации, целей.

Spring Boot 3.4 / Java 21 / PostgreSQL / JWT

---

## Требования

- **Java 21+**
- **Maven 3.9+**
- **PostgreSQL 16+** (локально или в Docker)
- **Docker** (опционально, для контейнеризации)

---

## Запуск

### 1. Переменные окружения

Создайте файл `.env` в корне проекта:

```env
DB_PASSWORD=your_db_password
JWT_SECRET=base64-encoded-secret-at-least-256-bits
```

> `.env` добавлен в `.gitignore` — секреты не попадут в репозиторий.
> Для JWT_SECRET можно сгенерировать: `openssl rand -base64 64`

### 2. Подготовка БД

**Через Docker:**
```bash
docker run -d \
  --name fitness-db \
  -e POSTGRES_DB=fitness_assistant \
  -e POSTGRES_PASSWORD=${DB_PASSWORD} \
  -p 5432:5432 \
  postgres:16
```

**Локально:** создайте БД `fitness_assistant` в вашем PostgreSQL.

### 3. Сборка и запуск

```bash
# Сборка
./mvnw clean package

# Запуск
java -jar target/fitness-assistant-0.0.1-SNAPSHOT.jar
```

Или через Maven:
```bash
./mvnw spring-boot:run
```

При первом запуске:
- Flyway накатит 18 миграций
- DataInitializer загрузит стартовые данные (512 продуктов, 33 мышцы, 109 упражнений)

---

## Порты

| Сервис | Порт |
|---|---|
| API | `8080` |
| PostgreSQL | `5432` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| Health | `http://localhost:8080/actuator/health` |

---

## Аутентификация

Публичные эндпоинты (без токена):
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `/api/v1/food/**`
- `/api/v1/exercises/**`

Для всех остальных эндпоинтов требуется Bearer JWT-токен.

---

## API Endpoints

### Auth (`/api/v1/auth`)
| Метод | Путь | Описание |
|---|---|---|
| POST | `/register` | Регистрация нового пользователя |
| POST | `/login` | Вход, получение JWT |
| DELETE | `/{id}` | Удаление пользователя |

### Food (`/api/v1/food`)
| Метод | Путь | Описание |
|---|---|---|
| GET | `/` | Все продукты (пагинация) |
| GET | `/{id}` | Продукт по ID |
| GET | `/search?name=` | Поиск продуктов |
| GET | `/calc/{id}?weight=` | Расчёт КБЖУ на вес |
| POST | `/` | Создать продукт |
| PATCH | `/` | Обновить продукт |
| DELETE | `/{id}` | Удалить продукт |

### Exercises (`/api/v1/exercises`)
| Метод | Путь | Описание |
|---|---|---|
| GET | `/` | Все упражнения |
| GET | `/{id}` | Упражнение по ID |
| GET | `/search?name=` | Поиск по названию/мышце |
| POST | `/` | Создать упражнение |
| PATCH | `/` | Обновить упражнение |
| DELETE | `/{id}` | Удалить упражнение |

### Meals (`/api/v1/meal`)
| Метод | Путь | Описание |
|---|---|---|
| GET | `/search` | Поиск приёмов пищи |
| GET | `/{id}` | Приём пищи по ID |
| GET | `/daily` | Дневная норма питания |
| POST | `/manual` | Добавить приём вручную |
| POST | `/auto` | Добавить по продукту + вес |
| PATCH | `/` | Обновить |
| DELETE | `/{id}` | Удалить |

### Hydration (`/api/v1/hydration`)
| Метод | Путь | Описание |
|---|---|---|
| GET | `/search` | История гидратации |
| GET | `/{id}` | Запись по ID |
| GET | `/daily` | Дневная гидратация |
| POST | `/` | Добавить запись |
| PATCH | `/` | Обновить |
| DELETE | `/{id}` | Удалить |

### Workouts (`/api/v1/workout`)
| Метод | Путь | Описание |
|---|---|---|
| GET | `/` | Все тренировки |
| GET | `/{id}` | Тренировка + упражнения |
| GET | `/search?name=` | Поиск |
| POST | `/` | Создать |
| PATCH | `/` | Обновить |
| DELETE | `/{id}` | Удалить |

### Workout Sessions (`/api/v1/workout/session`)
| Метод | Путь | Описание |
|---|---|---|
| GET | `/history` | История сессий |
| GET | `/{id}` | Сессия по ID |
| POST | `/start` | Начать тренировку |
| PATCH | `/end` | Завершить тренировку |
| DELETE | `/{id}` | Удалить сессию |

### Sets (`/api/v1/workout/session/set`)
| Метод | Путь | Описание |
|---|---|---|
| GET | `/?sessionId=&exerciseId=` | Подходы сессии |
| GET | `/{id}?sessionId=` | Подход по ID |
| POST | `/` | Добавить подход |
| PATCH | `/` | Обновить подход |
| DELETE | `/{id}?sessionId=` | Удалить подход |

### Profile (`/api/v1/profile`)
| Метод | Путь | Описание |
|---|---|---|
| GET | `/` | Профиль пользователя |
| POST | `/` | Создать профиль |
| PATCH | `/` | Обновить профиль |
| DELETE | `/` | Удалить профиль |
| PATCH | `/targets` | Обновить цели |
| GET | `/targets` | Получить цели |
| PATCH | `/targets/status?enabled=` | Автопилот целей |

---

## Тестирование

Проект содержит заглушку теста:

```bash
./mvnw test
```

---

## Архитектура

```
application/service/   → Use cases (бизнес-логика)
core/model/            → Domain модели
core/repository/       → Интерфейсы репозиториев
infrastructure/adapter/→ Реализации репозиториев (JPA)
infrastructure/security/→ JWT + Spring Security
infrastructure/mapper/ → Entity ↔ Domain мапперы
infrastructure/init/   → DataInitializer (CSV → БД)
web/controller/        → REST контроллеры
web/dto/               → Request/Response DTO
web/mapper/            → Domain ↔ DTO мапперы
```

---

## Технологии

| Назначение | Библиотека |
|---|---|
| Фреймворк | Spring Boot 3.4.3 |
| Язык | Java 21 |
| БД | PostgreSQL 16 |
| Миграции | Flyway (18 миграций) |
| Кэш | Caffeine (exercises, food, muscles, userProfiles) |
| Аутентификация | JWT (jjwt 0.12.6) |
| Документация | SpringDoc OpenAPI 2.8.6 |
| Валидация | Jakarta Validation |
| Мониторинг | Spring Actuator |
| CSV | OpenCSV 5.9 |
| Сборка | Maven |
