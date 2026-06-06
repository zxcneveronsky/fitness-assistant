# AGENTS.md — План разработки Fitness Assistant

## Архитектура (Hexagonal)

```
web/controller/     → REST
application/service/→ Use cases
core/model/         → Domain POJOs
core/repository/    → Ports (интерфейсы)
infrastructure/adapter/ → Adapters (JPA реализации)
infrastructure/mapper/  → Entity ↔ Domain
infrastructure/security/ → JWT + Security
infrastructure/init/ → DataInitializer
web/dto/            → Request/Response
web/mapper/         → Domain ↔ DTO
```

### Правила добавления новой фичи
1. Миграция Flyway (V14__*.sql, V15__*.sql, ...)
2. JPA Entity в `infrastructure/persistence/entity/`
3. Spring Data JPA repo в `infrastructure/persistence/jpa/`
4. Domain model в `core/model/`
5. Port (interface) в `core/repository/`
6. Mapper Entity↔Domain в `infrastructure/mapper/`
7. Adapter в `infrastructure/adapter/`
8. Use case в `application/service/`
9. Controller в `web/controller/`
10. Web mapper Domain↔DTO в `web/mapper/`
11. DTO в `web/dto/request/` и `web/dto/response/`
12. Фронт в `src/main/resources/static/`

---

## 🔒 Консистенция кода (обязательно)

### URL naming
- Все URL в единственном числе: `/exercise`, `/muscle`, `/food`, `/meal`, `/workout`, `/workout/session`, `/workout/session/set`

### DTO поля
- Название: `name` (не `exerciseName`, `foodName`, `workoutName`)
- Списки ID: `exerciseIds`, `muscleIds`, `foodIds` (не `exercisesIds`, `musclesId`, `foodsIds`)
- `userId` в response DTO не возвращаем

### Use case naming
- `search{Entity}(...nullable params...)` — поиск с фильтрами, возвращает `Page<Entity>`
- `findById(...)` — точный поиск одной записи
- `findAll(...)` — все записи пользователя
- `get{Data}(...)` — агрегаты/расчёты (например `getDailyNutrition`, `getDailyHydration`)
- Класс: `Find{Entity}UseCase` — может содержать `findById` + `search{Entity}`

### @Transactional
- Только в use case'ах (`application/service/`)
- В адаптерах, мапперах, контроллерах — НЕТ

### Cache
- Кэшируем только reference data: Exercise, Food, Muscle
- UserProfile, Targets — НЕ кэшируем (часто меняются, per-user)
- Cache names в `application.yml`: `spring.cache.cache-names: exercise, food, muscle`
- `@EnableCaching` на `Application.java`. `CacheManager` бин не создаём

### Переменные (Java)
- `Page<Entity> result` → `Page<Entity> {entity}s` (множественное число)
- `Entity saved = repo.save(...)` → `Entity saved{Entity} = repo.save(...)`
- `Entity entity = mapper.toEntity(domain)` → `Entity {name}Entity = mapper.toEntity(domain)` (в адаптерах)
- `Entity merged = em.merge(entity)` → `Entity merged{Name}Entity = em.merge({name}Entity)`
- Без однобуквенных (`w`, `e`, `s`, `f`)
- `findById` → переменная `{entity}` (без `saved`/`updated`)

### Exception-конструкторы
- UserProfileNotFoundException, TargetsNotFoundException — без id (их id = userId)

### Исключения
- `CreateUserProfileUseCase: Targets targets = new Targets()` — новый объект, не из БД

---

## 📋 План

### ⭐ 1. Избранное

**Миграция** `V15__create_favorite_tables.sql`:
```sql
CREATE TABLE favorite_exercises (
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    exercise_id BIGINT REFERENCES exercises(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (user_id, exercise_id)
);

CREATE TABLE favorite_foods (
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    food_id BIGINT REFERENCES foods(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (user_id, food_id)
);
```

**Бэк:**
- `FavoriteExerciseEntity`, `FavoriteFoodEntity` + jpa repos
- `FavoriteExercise`, `FavoriteFood` domain models
- Ports: `FavoriteExerciseRepository`, `FavoriteFoodRepository`
- Adapters + Mappers
- Toggle: `POST /favorite/exercise/{id}` / `POST /favorite/food/{id}` (add if not exists, remove if exists)
- List: `GET /favorite/exercise`, `GET /favorite/food`
- Эндпоинты без body, только path variable (toggle)

**Фронт:**
- Heart icon ♡/♥ на карточках в `explore/exercises.html` и `explore/food.html`
- Фильтр «⭐ Избранное» (галочка/кнопка) — показывает только избранное
- При загрузке страницы: `GET /favorite/exercise` → маппинг `Set<Long>`, подсветка сердечек

---

### ⚖️ 2. История веса

**Миграция** `V16__create_body_weights_table.sql`:
```sql
CREATE TABLE body_weights (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    weight_kg NUMERIC(5,1) NOT NULL,
    measured_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_body_weights_user_date ON body_weights(user_id, measured_at DESC);
```

**Бэк:**
- `BodyWeightEntity`, `BodyWeight` domain + port + adapter
- `POST /profile/weight` — `{ weightKg }`, sets measured_at = now
- `GET /profile/weight?from=&to=` — history sorted DESC
- `GET /profile/weight/latest` — last entry
- `DELETE /profile/weight/{id}` — удалить запись

**Фронт:**
- Dashboard: блок «⚖️ Вес» — последнее значение + trend arrow (вверх/вниз/равно)
- Маленький график Chart.js line за 30 дней
- Кнопка «Записать» → input с number keyboard
- На `profile.html`: полный график, кнопка удаления точки

---

### 📈 3. Прогресс в упражнениях

**Бэк:**
- `GET /exercise/{id}/progress?from=&to=` — агрегация по `sets`
- Возвращает: `[{ date, maxWeight, totalReps, totalVolume, sets: [{weight, reps}] }]`
- Группировка по `session_id`, берётся `startTime` из `workout_sessions`

**Фронт:**
- В `explore/exercises.html`: кнопка «📊 Прогресс» у каждого упражнения при клике
- Модалка с двумя графиками:
  1. Рабочий вес (max weight за сессию) — line chart
  2. Объём (weight × reps) — line chart
- Если данных нет — «Нет данных для этого упражнения»

---

### 🔥 4. Streak

**Миграция** `V17__add_streak_to_profiles.sql`:
```sql
ALTER TABLE users_profiles
    ADD COLUMN current_streak INT DEFAULT 0,
    ADD COLUMN last_workout_date DATE;
```

**Бэк:**
- В `endSession`: проверить `today` vs `last_workout_date`
  - `last_workout_date = today` → ничего не менять (уже считали)
  - `last_workout_date = yesterday` → `streak++`
  - иначе → `streak = 1`
  - `last_workout_date = today`
- `GET /profile/streak` → `{ workoutStreak: N }`

**Фронт:**
- Dashboard: 🔥 `N дней` (крупно, рядом с приветствием)
- Если streak = 0 — не показывать

---

### 🔗 5. Шаринг тренировок

**Миграция** `V18__create_workout_shares.sql`:
```sql
CREATE TABLE workout_shares (
    workout_id BIGINT REFERENCES workouts(id) ON DELETE CASCADE,
    shared_with_user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (workout_id, shared_with_user_id)
);

ALTER TABLE workouts ADD COLUMN is_public BOOLEAN DEFAULT false;
```

**Бэк:**
- `WorkoutShareEntity`, `WorkoutShare` domain + port + adapter
- `POST /workout/{id}/share` — body `{ email }`, findByEmail → save, если email не найден → 404
- `DELETE /workout/{id}/share/{userId}` — revoke
- `GET /workout/shared-with-me` — list
- `POST /workout/{id}/copy` — копия к себе (новый workout с теми же exercise_id)
- `PATCH /workout/{id}/visibility` — body `{ isPublic: true/false }`
- В `GET /workout/user/{userId}` (нужен новый endpoint или доработка существующего):
  ```sql
  WHERE user_id = :me
     OR is_public = true
     OR id IN (SELECT workout_id FROM workout_shares WHERE shared_with_user_id = :me)
  ```

**Фронт (`workout.html`):**
- Три вкладки: «Мои» / «Доступны мне» / «Публичные»
- В деталях тренировки (`workout-detail.html`):
  - Переключатель публичности `🌍 Публичная` (toggle)
  - Кнопка «🔗 Поделиться» → модалка с input email + «Поделиться»
  - Список тех, с кем поделились (email + крестик для отзыва)
  - Если shared/public — кнопка «📋 Скопировать к себе»

**Права доступа:**
- Владелец: всё (edit, delete, share)
- Shared user: только просмотр + копия
- Public: просмотр без авторизации + копия

---

### 🐳 6. Docker Compose

**Файлы:**
- `Dockerfile` — multi-stage build:
  ```dockerfile
  FROM maven:3.9-eclipse-temurin-21 AS build
  WORKDIR /app
  COPY . .
  RUN mvn clean package -DskipTests

  FROM eclipse-temurin:21-jre
  WORKDIR /app
  COPY --from=build /app/target/*.jar app.jar
  EXPOSE 8080
  ENTRYPOINT ["java", "-jar", "app.jar"]
  ```
- `docker-compose.yml`:
  ```yaml
  version: '3.8'
  services:
    postgres:
      image: postgres:16
      environment:
        POSTGRES_DB: fitness_assistant
        POSTGRES_PASSWORD: ${DB_PASSWORD}
      ports:
        - "5432:5432"
      volumes:
        - pgdata:/var/lib/postgresql/data

    app:
      build: .
      ports:
        - "8080:8080"
      environment:
        DB_PASSWORD: ${DB_PASSWORD}
        JWT_SECRET: ${JWT_SECRET}
        SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/fitness_assistant
      depends_on:
        - postgres

  volumes:
    pgdata:
  ```
- `.env.example`:
  ```
  DB_PASSWORD=your_db_password
  JWT_SECRET=base64-encoded-secret-at-least-256-bits
  ```

---

### 🧪 7. Тесты

**Конфиг:** `src/test/resources/application-test.yml`:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: none
  flyway:
    enabled: true
    locations: classpath:db/migration
```

**Зависимость в `pom.xml`:** `com.h2database:h2:runtime`, `org.springframework.boot:spring-boot-starter-test`

**Тесты:**
- `AuthFlowTest` — register → login → valid JWT
- `WorkoutFlowTest` — create workout → start session → add set → end session
- `ProfileTest` — create profile → update targets → verify

**Шаблон:**
```java
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class WorkoutFlowTest {
    @Autowired private MockMvc mockMvc;

    @Test
    void fullWorkoutFlow() throws Exception {
        // 1. Register
        // 2. Login → get JWT
        // 3. Create workout
        // 4. Start session
        // 5. Add set
        // 6. End session
    }
}
```

---

### 🛠 8. Админка

**Бэк:**
- Роль ADMIN уже есть в `UserRole.java` (не используется)
- В `SecurityConfig` добавить: `.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")`
- `@PreAuthorize` или filter для admin endpoints
- `AdminController`:
  - `GET/POST/PUT/DELETE /admin/foods`
  - `GET/POST/PUT/DELETE /admin/exercises`
  - `GET /admin/users` — список пользователей
- При регистрации первого пользователя — сделать его ADMIN (опционально)

**Фронт `admin.html`:** (на усмотрение, можно через Swagger тестировать)

**Создать админа:** напрямую в БД или через эндпоинт (если реализовать).
