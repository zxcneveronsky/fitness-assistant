# PATTERNS — Fitness Assistant

## Backend

### Stack

| Назначение | Технология | Версия |
|---|---|---|
| Язык | Java | 21 (JDK) |
| Фреймворк | Spring Boot | 3.4.3 |
| Сборка | Maven | 3.9+ |
| База данных | PostgreSQL | 16+ |
| ORM | Hibernate / JPA | через Spring Data JPA |
| Миграции | Flyway | V01–V19 |
| Аутентификация | JWT (jjwt) | 0.12.6 |
| Валидация | Jakarta Validation | — |
| Мониторинг | Spring Actuator | — |
| Кэш | Caffeine | exercise, food, muscle |
| CSV | OpenCSV | 5.9 (DataInitializer) |
| Security | Spring Security + JWT filter | — |

---

### Архитектура

**Clean Architecture** — 4 слоя, зависимости направлены внутрь:

```
web (presentation) → application (use cases) → core (domain)
                                                    ↑
                                          infrastructure (adapters)
```

- **`core/`** — доменные модели (POJO), интерфейсы репозиториев, security-интерфейсы, исключения. Чистая Java, никаких зависимостей от фреймворков.
- **`application/`** — юзкейсы (`@Service`). Содержат бизнес-логику. Зависят только от `core/`.
- **`infrastructure/`** — адаптеры репозиториев, JPA-сущности, реализации security, конфиги. Зависят от `core/`.
- **`web/`** — контроллеры, DTO, веб-мапперы, GlobalExceptionHandler. Зависят от `application/` и `infrastructure/`.

---

### Нейминг (по слоям)

#### Все слои

| Сущность | Шаблон | Пример |
|---|---|---|
| DTO Create | `Create{Entity}Request` | `CreateExerciseRequest` |
| DTO Update | `Update{Entity}Request` | `UpdateExerciseRequest` |
| DTO Response | `{Entity}Response` | `ExerciseResponse` |
| Юзкейс | `{Verb}{Entity}UseCase` | `CreateExerciseUseCase` |
| Метод юзкейса | `{verb}{Entity}(params)` | `createExercise()` |
| Контроллер | `{Entity}Controller` | `ExerciseController` |
| WebMapper | `{Entity}WebMapper` | `ExerciseWebMapper` |
| Domain Repository (interface) | `{Entity}Repository` | `ExerciseRepository` |
| JPA Repository | `Jpa{Entity}Repository` | `JpaExerciseRepository` |
| Repository Adapter | `{Entity}RepositoryAdapter` | `ExerciseRepositoryAdapter` |
| Entity↔Domain Mapper | `{Entity}Mapper` | `ExerciseMapper` |
| JPA Entity | `{Entity}Entity` | `ExerciseEntity` |
| Исключение | `{Entity}NotFoundException` | `ExerciseNotFoundException` |
| | `{Entity}AlreadyExistsException` | `WorkoutAccessAlreadyExistsException` |

#### Переменные в юзкейсах

| Контекст | Шаблон | Пример |
|---|---|---|
| Параметр домена в Create | `{entity}` (camelCase от класса) | `Exercise exercise`, `BodyWeight bodyWeight` |
| Результат `save()` | `saved{Entity}` | `savedExercise`, `savedUser`, `savedBodyWeight` |
| Входящий объект апдейта | `{entity}Update` | `exerciseUpdate`, `bodyWeightUpdate`, `mealUpdate` |
| Существующий объект в `.map()` | `existing{Entity}` | `existingExercise`, `existingBodyWeight` |
| Результат после апдейта | `updated{Entity}` | `updatedExercise`, `updatedBodyWeight` |

#### Переменные в мапперах

| Контекст | Шаблон | Пример |
|---|---|---|
| Параметр DTO | `request` | `CreateExerciseRequest request` |
| Параметр в `toEntity` | `domain` | `Exercise domain` |
| Параметр в `toDomain` (из entity) | `entity` | `ExerciseEntity entity` |

---

### DTO (records)

#### CreateRequest

- Java `record`
- **Все поля обязательные** — `@NotNull` + `@NotBlank` / `@NotEmpty`
- Строки: `@Size(max = …)` — всегда задан лимит
- Числа: `@Min` / `@Max` или `@Positive` / `@PositiveOrZero`
- Даты: `@PastOrPresent`
- Поле `id` **отсутствует** (авто-генерация)
- Сообщения валидации — **на русском**
- Пакет: `web/dto/request/create/`

```java
public record CreateExerciseRequest(
        @NotNull(message = "Название не может быть пустым")
        @NotBlank(message = "Название не может быть пустым")
        @Size(max = 255, message = "Название слишком длинное")
        String name,
        @NotNull(message = "Описание не может быть пустым")
        @NotBlank(message = "Описание не может быть пустым")
        @Size(max = 1000, message = "Описание слишком длинное")
        String description,
        @NotNull(message = "Список мышц не может быть пустым")
        @NotEmpty(message = "Список мышц не может быть пустым")
        List<@NotNull Long> muscleIds
) {}
```

#### UpdateRequest

- Java `record`
- `Long id` — `@NotNull` (обязательное)
- **Все остальные поля — nullable** (без `@NotNull` / `@NotBlank`)
- Ограничения `@Size`, `@Min`/`@Max` сохраняются (если поле пришло — оно валидно)
- Сообщения — на русском
- Пакет: `web/dto/request/update/`

```java
public record UpdateExerciseRequest(
        @NotNull(message = "ID упражнения не может быть пустым")
        Long id,
        @Size(max = 255, message = "Название слишком длинное")
        String name,
        @Size(max = 1000, message = "Описание слишком длинное")
        String description,
        List<@NotNull Long> muscleIds
) {}
```

#### Response

- Java `record`
- **Нет аннотаций валидации**
- Всегда содержит `Long id` (кроме случаев, где сущность не имеет своего id — например `UserProfile`)
- **Не содержит userId** — это антипаттерн, пробрасывать userId наружу запрещено
- Вложенные records для композитных данных
- Пакет: `web/dto/response/`

```java
public record ExerciseResponse(Long id, String name, String description, List<ExerciseMuscleResponse> muscles) {
    public record ExerciseMuscleResponse(Long id, String name) {}
}
```

---

### WebMapper

- `@Component` (или `@RequiredArgsConstructor` если есть зависимости)
- Методы:
  - `toDomain(Create{Entity}Request)` — маппит DTO → домен. Поля, которых нет в запросе = `null` (с комментарием `// Этого поля нет в запросе`)
  - `toDomain(Update{Entity}Request)` — nullable-поля могут быть `null`
  - `toResponse(Domain)` — домен → Response

```java
public Exercise toDomain(CreateExerciseRequest request) {
    return new Exercise(
            null, // Этого поля нет в запросе
            request.name(),
            request.description(),
            request.muscleIds().stream().map(id -> new Muscle(id, null)).toList()
    );
}
```

---

### Контроллер

#### Структура класса

```java
@RestController
@RequestMapping("/api/v1/{entity}")
@RequiredArgsConstructor
@Validated
public class ExerciseController {

    private final FindExerciseUseCase findExerciseUseCase;
    private final CreateExerciseUseCase createExerciseUseCase;
    private final UpdateExerciseUseCase updateExerciseUseCase;
    private final DeleteExerciseUseCase deleteExerciseUseCase;
    private final ExerciseWebMapper exerciseWebMapper;
```

- Поля: по одному юзкейсу на CRUD-операцию + WebMapper
- Дополнительные юзкейсы (не CRUD) добавляются по необходимости
- `@Validated` на классе обязателен

#### HTTP-методы

| Операция | Аннотация | Статус |
|---|---|---|
| Чтение (одна) | `@GetMapping("/{id}")` | 200 OK |
| Чтение (список/поиск) | `@GetMapping` / `@GetMapping("/search")` | 200 OK |
| Создание | `@PostMapping` | `@ResponseStatus(CREATED)` |
| Обновление | `@PatchMapping` | `@ResponseStatus(OK)` |
| Удаление | `@DeleteMapping("/{id}")` | `@ResponseStatus(NO_CONTENT)` |

#### Порядок аргументов методов

```
1. @AuthenticationPrincipal UserDetailsAdapter adapter   ← если требуется аутентификация
2. @PathVariable("id") Long {entity}Id                    ← всегда с переименованием
3. @RequestParam                                          ← query-параметры
4. @Valid @RequestBody                                    ← тело запроса
5. Pageable / @PageableDefault(size = 12)                 ← всегда последним
```

Path variable **всегда** переименовывается в семантическое имя:

```java
@PathVariable("id") Long exerciseId    // а не просто id
@PathVariable("id") Long bodyWeightId  // а не просто id
```

#### Нейминг переменных в контроллере

**API-имена** — как их назвал клиент:
- `request` (тело запроса)
- `from`, `to` (диапазоны дат)
- `localDateTime` (параметр времени от клиента)
- `name`, `muscleId` (параметры поиска)

---

### Юзкейс (Use Case / Service)

#### Структура класса

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateExerciseUseCase {

    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Exercise createExercise(Exercise exercise) { ... }
}
```

- `@Transactional` — на запись
- `@Transactional(readOnly = true)` — на чтение (можно на классе для read-only юзкейсов)

#### Возвращаемые типы

| Тип метода | Возврат |
|---|---|
| Create | Domain entity |
| Update | Domain entity |
| Find single | Domain entity (или `Optional`) |
| Find list/page | `Page<T>`, `List<T>` |
| Delete | `void` |

#### Порядок аргументов методов

```
1. Long userId (текущий аутентифицированный пользователь)   ← всегда первым
2. Другие entity ID (workoutId, sessionId, exerciseId...)   ← ID других сущностей
3. Бизнес-параметры (доменный объект, String, enum, Boolean)  ← от тяжёлых к лёгким
4. Временные параметры (LocalDate, LocalDateTime)            ← даты/время
5. Pageable                                                   ← всегда последним
```

**Правило тяжёлых сущностей**: если несколько бизнес-параметров, сначала идёт тот, что "тяжелее" как сущность. Например, `workoutId` перед `exerciseId`, `sessionId` перед `setId`.

**Примеры:**

```java
// user-scoped: userId первым
BodyWeight createBodyWeight(Long userId, BodyWeight bodyWeight)

// множество ID: userId → workoutId → email → accessLevel
WorkoutAccess createWorkoutAccess(Long userId, Long workoutId, String email, AccessLevel accessLevel)

// поиск с пагинацией: userId → entityId → from → to → Pageable
Page<ExerciseHistory> findExerciseHistory(Long userId, Long exerciseId, LocalDateTime from, LocalDateTime to, Pageable pageable)

// без userId (публичные ресурсы): бизнес-параметры → Pageable
Page<Exercise> searchExercise(String name, Long muscleId, Pageable pageable)

// Auth (нет userId — пользователь не аутентифицирован):
LoginResult loginUser(String email, String password)
LoginResult registerUser(User user)
```

#### Нейминг переменных в юзкейсе

**Бизнес-имена** — как они называются в предметной области:
- `consumedAt` (не `localDateTime`)
- `measuredAt` (не `date`)
- `startTime`, `endTime` (не `from`, `to` — это имена для API)

#### Паттерн Create

```java
@Transactional
public BodyWeight createBodyWeight(Long userId, BodyWeight bodyWeight) {
    BodyWeight savedBodyWeight = bodyWeightRepository.save(bodyWeight);
    log.info("Запись веса создана | id={} | weight={}", savedBodyWeight.getId(), savedBodyWeight.getWeight());
    return savedBodyWeight;
}
```

1. При необходимости `domain.setId(null)` — защита от переданного id
2. Бизнес-проверки (unique, exists)
3. `repository.save(domain)` → `saved{Entity}`
4. `log.info(...)` — на русском, pipe-разделители
5. return `saved{Entity}`

#### Паттерн Update

```java
@Transactional
public BodyWeight updateBodyWeight(Long userId, BodyWeight bodyWeightUpdate) {
    Long bodyWeightId = bodyWeightUpdate.getId();
    BodyWeight updatedBodyWeight = bodyWeightRepository.findById(bodyWeightId, userId)
            .map(existingBodyWeight -> {
                existingBodyWeight.setWeight(
                        bodyWeightUpdate.getWeight() != null
                                ? bodyWeightUpdate.getWeight()
                                : existingBodyWeight.getWeight()
                );
                existingBodyWeight.setMeasuredAt(
                        bodyWeightUpdate.getMeasuredAt() != null
                                ? bodyWeightUpdate.getMeasuredAt()
                                : existingBodyWeight.getMeasuredAt()
                );
                return bodyWeightRepository.save(existingBodyWeight);
            })
            .orElseThrow(() -> new BodyWeightNotFoundException(bodyWeightId));
    log.info("Запись веса обновлена | id={}", bodyWeightId);
    return updatedBodyWeight;
}
```

1. `Long entityId = entityUpdate.getId()` — извлечение id
2. `repository.findById(entityId, ...)` — поиск существующей
3. `.map(existing{Entity} -> {` — lambda с именем `existing{Entity}`
4. Для каждого поля: `update.getField() != null ? update.getField() : existing.getField()`
5. `repository.save(existing)` — save мутированной existing
6. `.orElseThrow(() → new NotFoundException(entityId))`
7. `log.info(...)`
8. return `updated{Entity}`

#### Паттерн Delete

```java
@Transactional
public void deleteExercise(Long exerciseId) {
    if (!exerciseRepository.existsById(exerciseId)) {
        throw new ExerciseNotFoundException(exerciseId);
    }
    exerciseRepository.deleteById(exerciseId);
    log.info("Упражнение удалено | id={}", exerciseId);
}
```

#### Паттерн Find

```java
@Transactional(readOnly = true)
public Exercise findById(Long exerciseId) {
    Exercise exercise = exerciseRepository.findById(exerciseId)
            .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));
    log.info("Упражнение найдено | id={}", exerciseId);
    return exercise;
}
```

#### Логирование

- `@Slf4j` на классе
- Формат: `"Действие | key=value | key=value"`
- Язык: русский
- Всегда логируется id созданной/изменённой/удалённой сущности

---

### Доменная модель

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BodyWeight {
    private Long id;
    private Long userId;
    private Double weight;
    private LocalDate measuredAt;
}
```

- POJO с Lombok
- Нет аннотаций фреймворков (чистая Java)
- `Long id` — всегда первое поле
- `Long userId` — если привязано к пользователю

---

### Исключения

```java
public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(Long id) {
        super("Упражнение с id " + id + " не найдено.");
    }
}
```

- Наследуют `RuntimeException`
- Конструктор принимает `Long id` (или `String email` для User)
- Сообщение на русском: `"Сущность с id " + id + " не найдена."`
- Один файл = одно исключение = одна сущность
- Пакет: `core/exception/`

---

### GlobalExceptionHandler

```java
@Slf4j
@RestControllerAdvice
```

**Группировка по HTTP-статусам:**

| Статус | Исключения |
|---|---|
| `404 NOT_FOUND` | Все `*NotFoundException` + `EntityNotFoundException` |
| `409 CONFLICT` | Все `*AlreadyExistsException` |
| `401 UNAUTHORIZED` | `InvalidPasswordException` |
| `403 FORBIDDEN` | `AccessDeniedException` |
| `400 BAD_REQUEST` | `IllegalArgumentException`, `ConstraintViolationException`, `MethodArgumentNotValidException`, `HttpMessageNotReadableException` |
| `500 INTERNAL_SERVER_ERROR` | Все остальные (логгируется stacktrace) |

**Формат ответа:**

```json
{"timestamp": "...", "status": 404, "error": "Not Found", "message": "..."}
```

- `LinkedHashMap` для сохранения порядка полей

---

### Валидация (3 слоя)

| Слой | Где | Механизм |
|---|---|---|
| 1. DTO-level | `web/dto/request/` | Jakarta Validation аннотации |
| 2. Controller-level | Контроллер | `@Validated` + `@Valid @RequestBody` |
| 3. Use Case level | Юзкейсы | Ручные проверки (exists, unique, ownership) |

---

### JPA Entity

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "body_weights")
public class BodyWeightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "measured_at", nullable = false)
    private LocalDate measuredAt;
}
```

- `@EqualsAndHashCode(of = "id")` — только по id
- `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`
- `@ManyToOne(fetch = FetchType.LAZY)` — все связи LAZY
- `@Column(name = "snake_case", nullable = false)`
- `@Enumerated(EnumType.STRING)` — для enum
- `@Table(name = "snake_case_plural")`
- `@UniqueConstraint` на таблицу для композитных уникальностей

---

### Infrastructure Mapper (Entity ↔ Domain)

```java
@Component
public class BodyWeightMapper {

    public BodyWeight toDomain(BodyWeightEntity entity) {
        if (entity == null) return null;
        return new BodyWeight(
                entity.getId(),
                entity.getUser().getId(),
                entity.getWeight(),
                entity.getMeasuredAt()
        );
    }

    public BodyWeightEntity toEntity(BodyWeight domain) {
        if (domain == null) return null;
        return new BodyWeightEntity(
                domain.getId(),
                null, // Это поле проставляется в адаптере через getReferenceById
                domain.getWeight(),
                domain.getMeasuredAt()
        );
    }
}
```

- `toDomain`: null-safe, извлекает id из связанных entities
- `toEntity`: null-safe, связи = `null` (проставляются в адаптере)

---

### Repository Adapter

```java
@Component
@RequiredArgsConstructor
public class BodyWeightRepositoryAdapter implements BodyWeightRepository {

    private final JpaBodyWeightRepository jpaBodyWeightRepository;
    private final JpaUserRepository jpaUserRepository;
    private final BodyWeightMapper bodyWeightMapper;

    @Override
    public BodyWeight save(BodyWeight bodyWeight) {
        BodyWeightEntity entity = bodyWeightMapper.toEntity(bodyWeight);
        entity.setUser(jpaUserRepository.getReferenceById(bodyWeight.getUserId()));
        return bodyWeightMapper.toDomain(jpaBodyWeightRepository.save(entity));
    }

    @Override
    public Optional<BodyWeight> findById(Long id, Long userId) {
        return jpaBodyWeightRepository.findByIdAndUserId(id, userId)
                .map(bodyWeightMapper::toDomain);
    }
}
```

- `implements` доменный интерфейс репозитория
- `save()`: domain → entity → простановка связей через `getReferenceById()` → save → entity → domain
- find/delete/exists: делегирование в JPA + маппинг

---

### Domain Repository Interface

```java
public interface BodyWeightRepository {
    Optional<BodyWeight> findById(Long id, Long userId);
    List<BodyWeight> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to);
    Optional<BodyWeight> findLatestByUserId(Long userId);
    BodyWeight save(BodyWeight bodyWeight);
    void deleteById(Long id, Long userId);
    boolean existsById(Long id, Long userId);
}
```

- Чистый Java-интерфейс в `core/repository/`
- Методы с `userId` — для user-scoped сущностей

---

### JPA Repository

```java
public interface JpaBodyWeightRepository extends JpaRepository<BodyWeightEntity, Long> {
    Optional<BodyWeightEntity> findByIdAndUserId(Long id, Long userId);
    List<BodyWeightEntity> findByUserIdAndMeasuredAtBetweenOrderByMeasuredAtDescIdDesc(...);
    Optional<BodyWeightEntity> findTopByUserIdOrderByMeasuredAtDescIdDesc(Long userId);
    void deleteByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}
```

- extends `JpaRepository<{Entity}, Long>`
- User-scoped методы с суффиксом `AndUserId`

---

### API URL Convention

```
/api/v1/{entity}                          — базовый URL
/api/v1/{entity}/{id}                     — получение по ID
/api/v1/{entity}/search                   — поиск
/api/v1/{entity}/{id}/sub-resource        — вложенные ресурсы (/workout/{id}/access)
/api/v1/{entity}/sub-resource             — вложенные без ID (/workout/access/shared-with-me)
```

---

### Пагинация

- `@PageableDefault(size = 12)` в контроллерах
- `Page<T>` — для списков с пагинацией
- `@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)` в `ApplicationConfig`
- `Pageable` — **всегда последний** параметр и в контроллере, и в юзкейсе

---

### Безопасность

- **Stateless JWT**: `SessionCreationPolicy.STATELESS`
- `JwtFilter extends OncePerRequestFilter` — перед `UsernamePasswordAuthenticationFilter`
- **Публичные эндпоинты**: auth (register/login), search (food, exercise, muscle), статика
- **Всё остальное** — authenticated
- `PasswordEncoder` и `TokenProvider` — интерфейсы в `core/security`, реализации в `infrastructure/security`
- `UserDetailsAdapter` — адаптер доменного `User` в Spring `UserDetails`

```java
public record UserDetailsAdapter(User user) implements UserDetails {
    public Long getUserId() { return user.getId(); }
}
```

---

### Кэширование

- Только на адаптерах репозиториев
- Кэшируются редко меняемые данные: **exercise**, **food**, **muscle**
- `@Cacheable("{entity}")` — на чтении
- `@CacheEvict(value="{entity}", allEntries=true)` — на записи

---

### Flyway Миграции

- `V{номер}__{описание}.sql`
- Нумерация: V01, V02, ..., V19
- Описание на английском (snake_case)
- Все изменения только через миграции (не через JPA ddl-auto)
