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
| Миграции | Flyway | V01–V18 |
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

#### Семантика глаголов чтения (юзкейсы)

| Глагол | Семантика | Примеры |
|---|---|---|
| `Find` | найти сущности из БД (прямые данные) | `FindExerciseUseCase`, `FindWorkoutUseCase` |
| `Get` | получить производные данные (агрегаты, проекции id, композиты) | `GetDailyHydrationUseCase`, `GetFavoriteExerciseIdsUseCase`, `GetSessionDetailUseCase` |
| `Calculate` | рассчитать на основе входных параметров | `CalculateFoodUseCase` |

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
- **Все поля обязательные**
- Строки: `@NotBlank` (сам отвергает null) + `@Size(max = …)` — всегда задан лимит
- Коллекции: `@NotEmpty` (сам отвергает null), элементы `List<@NotNull Long>`
- Числа, даты, enum: `@NotNull` (соседние `@Min`/`@Max`/`@Positive`/`@PastOrPresent` null пропускают)
- Поле `id` **отсутствует** (авто-генерация)
- Сообщения валидации — **на русском**, по шаблонам из раздела «Шаблоны сообщений валидации»
- Пакет: `web/dto/request/create/`

```java
public record CreateExerciseRequest(
        @NotBlank(message = "Название не может быть пустым")
        @Size(max = 255, message = "Название слишком длинное")
        String name,
        @NotBlank(message = "Описание не может быть пустым")
        @Size(max = 1000, message = "Описание слишком длинное")
        String description,
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
- **Не содержит любые user ID** — даже `sharedWithUserId` заменяется на `sharedWithUserEmail`
- **Нет isOwner в API** — фронт сам знает контекст через URL-параметр `access` (см. Безопасность)
- Вложенные records для композитных данных
- Пакет: `web/dto/response/`

```java
public record ExerciseResponse(Long id, String name, String description, List<ExerciseMuscleResponse> muscles) {
    public record ExerciseMuscleResponse(Long id, String name) {}
}
```

```java
// WorkoutAccessResponse — вместо sharedWithUserId идёт sharedWithUserEmail
public record WorkoutAccessResponse(
        Long id,
        String sharedWithUserEmail,  // не sharedWithUserId!
        Long workoutId,
        String workoutName,
        AccessLevel accessLevel
) {}
```

#### Шаблоны сообщений валидации

Сообщения строятся по единым шаблонам, имя поля — конкретное для данного DTO:

| Аннотация | Шаблон | Пример |
|---|---|---|
| `@NotBlank` / `@NotNull` (объекты) | «{Поле} не может быть пустым» | «Название продукта не может быть пустым» |
| `@NotNull` (даты/время) | «{Поле} должна быть указана» / «{Поле} должно быть указано» | «Дата приема должна быть указана» |
| `@NotEmpty` (коллекции) | «Список {чего} не может быть пустым» | «Список мышц не может быть пустым» |
| `@Size(max)` (строки) | «{Поле} слишком длинное» | «Имя слишком длинное» |
| `@Size` (пароль) | «Пароль должен содержать от 6 до 255 символов» | — |
| `@Email` | «Некорректный email» / «Email слишком длинный» | — |
| `@PastOrPresent` | «{Поле} не может быть в будущем» | «Дата взвешивания не может быть в будущем» |
| `@Past` | «{Поле} не может быть в будущем» | «Дата рождения не может быть в будущем» |
| `@Min` | «{Поле} должна быть не менее {N} {ед}» | «Вес должен быть не менее 5 кг» |
| `@Max` | «{Поле} не может быть больше {N} {ед}» | «Вес не может быть больше 500 кг» |
| `@Positive` | «{Поле} должна быть положительным» / «должны быть положительными» | «Повторения должны быть положительными» |
| `@PositiveOrZero` | «{Поле} не могут быть отрицательными» | «Калории не могут быть отрицательными» |
| Элементы `List<@NotNull>` | «ID {сущности} не может быть пустым» | «ID упражнения не может быть пустым» |

**Границы значений:**

| Поле | Ограничение |
|---|---|
| Вес тела / профиля | 5–500 кг |
| Рост | 30–300 см |
| Вес подхода | 0–10000 кг |
| Повторения | 1–500 |
| Вес продукта в приеме пищи | 0–5000 г |
| Вода за раз | 0–10 л |
| Калории (на 100 г) | 0–10000 |
| БЖУ (на 100 г) | 0–1000 |

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

#### Порядок аргументов методов репозитория

Репозитории используют **обратный** порядок относительно use case'ов — userId не первый, а после других ID:

```
1. name | id (ids)          — поисковые данные, первичный ID сущности
2. ___id | ____ids          — второстепенные фильтры (muscleId, exerciseId, workoutId...)
3. Long userId               — всегда после других ID
4. from / to                — временные параметры
5. Pageable                  — всегда последним
```

**Примеры:**

```java
// name (поиск) → muscleId (фильтр) → userId → Pageable
Page<Exercise> searchFavoriteExercise(String name, Long muscleId, Long userId, Pageable pageable);

// id (первичный) → userId (фильтр)
boolean existsByExerciseIdAndUserId(Long exerciseId, Long userId);
void deleteByExerciseIdAndUserId(Long exerciseId, Long userId);

// workoutId (первичный) → ownerId → sharedWithUserId
boolean existsByWorkoutIdAndOwnerIdAndSharedWithUserId(Long workoutId, Long ownerId, Long sharedWithUserId);

// search: name → userId → Pageable
Page<Workout> searchWorkout(String name, Long userId, Pageable pageable);
Page<Food> searchFavoriteFood(String name, Long userId, Pageable pageable);

// entityId → userId → from → to → Pageable
Page<Set> findByExerciseIdAndUserIdAndStartTimeBetween(Long exerciseId, Long userId, LocalDateTime from, LocalDateTime to, Pageable pageable);
```

**Почему userId не первый:** репозиторий работает с данными — первым идёт то, по чему ищут (имя, первичный ID сущности). userId — лишь один из фильтров, он идёт после основных идентификаторов.

**В адаптере** порядок совпадает с domain-интерфейсом. **В use case** — userId первый (см. α pattern). Use case при вызове репозитория переставляет аргументы в репозиторный порядок.

---

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

#### N+1 prevention

- **`JOIN FETCH`** — во всех кастомных `@Query` в JPA-репозиториях, где загружается связанная сущность
- **`@EntityGraph(attributePaths = {...})`** — альтернатива JOIN FETCH для derived queries
- **`@BatchSize(size = 15)`** — на коллекциях (`@OneToMany`, `@ElementCollection`)
- **Ленивая загрузка** — `FetchType.LAZY` везде. `.getId()` на LAZY-прокси не делает запрос к БД
- Пример:
  ```java
  @Query("SELECT wa FROM WorkoutAccessEntity wa JOIN FETCH wa.workout JOIN FETCH wa.owner JOIN FETCH wa.sharedWithUser WHERE wa.workout.id = :workoutId AND wa.owner.id = :ownerId")
  List<WorkoutAccessEntity> findByWorkoutIdAndOwnerId(...);
  ```

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

#### @MapsId — persist/merge

Для entity с `@MapsId` (PK = FK: UserProfile, Targets, Streak) **`SimpleJpaRepository.save()` вызывает `merge()` при non-null ID** — это ломает INSERT для новых записей. Фикс: явный вызов `persist()` для новых, `merge()` для существующих:

```java
@Override
public UserProfile save(UserProfile userProfile) {
    UserProfileEntity entity = userProfileMapper.toEntity(userProfile);
    entity.setUser(jpaUserRepository.getReferenceById(userProfile.getUserId()));
    if (entity.getId() != null && jpaUserProfileRepository.existsById(entity.getId())) {
        return userProfileMapper.toDomain(entityManager.merge(entity));
    }
    entityManager.persist(entity);
    return userProfileMapper.toDomain(entity);
}
```

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

#### WorkoutAccess — проверка доступа

Стандартный паттерн проверки во всех use case'ах, где нужен доступ к тренировке:

```java
boolean isOwner = workoutRepository.existsById(workoutId, userId);
boolean hasAccess = workoutAccessRepository.existsBySharedWithUserIdAndWorkoutId(userId, workoutId);
if (!isOwner && !hasAccess) {
    throw new WorkoutNotFoundException(workoutId);  // или AccessDeniedException
}
```

**Таблица прав:**

| Операция | Владелец | READ | COPY |
|---|---|---|---|
| Просмотр `GET /workout/{id}` | ✅ | ✅ | ✅ |
| Старт сессии `POST /workout/session/start` | ✅ | ✅ | ✅ |
| Копирование `POST /workout/{id}/copy` | ✅ | ❌ | ✅ |
| Редактирование / удаление | ✅ | ❌ | ❌ |
| Управление доступами | ✅ | ❌ | ❌ |

- Фронт получает `access` через URL-параметр (`workout-detail.html?id=X&access=READ`), а не через `isOwner` в API
- `workoutAccessRepository.existsBySharedWithUserIdAndWorkoutId(userId, workoutId)` — проверяет любой доступ (READ или COPY)
- Для копирования используется `existsBySharedWithUserIdAndWorkoutIdAndAccessLevel(userId, workoutId, COPY)` — только COPY

---

### Кэширование

- Только на адаптерах репозиториев
- Кэшируются редко меняемые данные: **exercise**, **food**, **muscle**
- `@Cacheable("{entity}")` — на чтении
- `@CacheEvict(value="{entity}", allEntries=true)` — на записи

---

### Flyway Миграции

- `V{номер}__{описание}.sql`
- Нумерация: V01, V02, ..., V18
- Описание на английском (snake_case)
- Все изменения только через миграции (не через JPA ddl-auto)

---

## ДОП ПАТТЕРНЫ

### 1. CREATE patterns

Четыре паттерна создания сущностей в зависимости от наличия DTO и маппера:

| Паттерн | DTO | Маппер (web) | userId в DTO | Use case строит `new` | Примеры |
|---|---|---|---|---|---|
| **А** | Есть | Есть | Нет | Нет | Food, Exercise |
| **Б** | Есть | Есть | Да | Нет | Workout, Set, Hydration, BodyWeight, Meal/manual, UserProfile, WorkoutAccess/update |
| **В** | Нет | Нет | — | Да | Favorite, CopyWorkout |
| **Г** | Есть | Нет | — | Да | Session/start, Meal/auto, WorkoutAccess/create |

**Правила выбора:**
- **А** — если ресурс публичный (не привязан к пользователю). DTO без `userId`
- **Б** — если ресурс привязан к пользователю (user-scoped). DTO содержит `userId`, маппер маппит
- **В** — если операция тривиальная (1-2 поля), не стоит заводить DTO и маппер
- **Г** — если нужен DTO (валидация с фронта), но создание сущности достаточно простое, чтобы не писать маппер — use case сам строит `new Entity(...)`. Пример: `CreateWorkoutAccessRequest` валидирует workoutId, email, accessLevel, а `CreateWorkoutAccessUseCase` сам создаёт `new WorkoutAccess(null, userId, sharedWithUserId, email, workoutId, null, accessLevel)`

### 2. α pattern — userId всегда первым

Во **всех** методах use case'ов `userId` — строго первый аргумент:

```java
// user-scoped
BodyWeight createBodyWeight(Long userId, BodyWeight bodyWeight)

// множество параметров
WorkoutAccess createWorkoutAccess(Long userId, Long workoutId, String email, AccessLevel accessLevel)

// поиск с пагинацией
Page<ExerciseHistory> findExerciseHistory(Long userId, Long exerciseId, LocalDateTime from, LocalDateTime to, Pageable pageable)
```

Исключения:
- Публичные ресурсы без привязки к пользователю (search food/exercise/muscle)
- Auth (register/login — пользователь ещё не аутентифицирован)

---

### 3. DataInitializer — CommandLineRunner + CSV seeding

**Файл:** `infrastructure/init/DataInitializer.java`

- `implements CommandLineRunner` — запускается после старта приложения
- **Guard clause**: `if (foodRepository.count() > 0) return;` — защита от повторного импорта при каждом рестарте
- **OpenCSV** (`CSVReader`) для чтения CSV из `ClassPathResource("data/{file}.csv")`
- Пропуск заголовка: `reader.readNext()` вхолостую
- **Helper-методы** `parseDouble()` / `parseLong()` с null-safety и логированием
- **Демо-пользователь**: хардкод `user@example.com` / `password123` с BCrypt
- Загрузка **напрямую в JPA Entity** (food, muscles, exercises), минуя слой адаптеров
- `saveAll()` для batch-вставки

---

### 4. JPA Interface Projections — агрегатные запросы

Для запросов с агрегацией (`SUM`, `COALESCE`) используются **Spring Data JPA interface-based closed projections**:

- `DailyHydrationProjection` — `Double getTotalAmount()`
- `DailyNutritionProjection` — `getKcal()`, `getProteins()`, `getFats()`, `getCarbs()`

**Цепочка маппинга:**

```
JpaRepository.@Query → Projection interface → InfrastructureMapper → Domain → WebMapper → Response
```

- Проекции маппятся в доменные модели через отдельные **инфраструктурные мапперы**:
  - `DailyHydrationMapper` — projection → `DailyHydration`
  - `DailyNutritionMapper` — projection → `DailyNutrition`
- `COALESCE(SUM(...), 0)` — если нет записей, проекция не null, маппер отдаёт `DailyHydration(0.0)` / `DailyNutrition(0.0, ...)`

---

### 5. Composite Domain Models — in-memory агрегации

Собираются в **use case** через координацию нескольких репозиториев. Не persist, а read-only value objects.

| Модель | Состав | Строится в | Особенность |
|---|---|---|---|
| `WorkoutWithExercise` | `Workout` + `List<Exercise>` | `FindWorkoutUseCase.findById()` | `@Getter @AllArgsConstructor`, без `@Setter` |
| `SessionDetail` → `ExerciseDetail` → `SetDetail` | 3 уровня вложенности | `GetSessionDetailUseCase.getSessionDetail()` | `@Getter @AllArgsConstructor`, immutable |
| `ExerciseHistory` | метаданные сессии + `List<Set>` | `FindExerciseHistoryUseCase.findExerciseHistory()` | собран через `groupingBy`, обёрнут в `PageImpl` |

```java
@Getter
@AllArgsConstructor
public class SessionDetail {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<ExerciseDetail> exercises;
}
```

**Принцип:** use case загружает данные из нескольких репозиториев, группирует/собирает и возвращает плоскую или вложенную read-only структуру.

---

### 6. TargetCalculationService — Helper Service

Статус: stateless `@Service` с чистой бизнес-логикой, **без репозиториев**.

```java
@Service
public class TargetCalculationService {
    private static final double PROTEIN_RATIO = 0.30;
    // ...

    public void applyManualTargets(Targets targets, Targets request) { ... }
    public void applyAutoTargets(Targets targets, UserProfile profile) { ... }
}
```

- **Не `@Transactional`** — только мутация переданного доменного объекта
- Инжектится в use case, который вызывает его методы
- Содержит: Mifflin-St Jeor equation, балансировку макросов, активность multiplier
- **Паттерн:** изоляция сложных вычислений из use case в отдельный stateless-сервис

---

### 7. Enum Convention

| Где определён | Когда использовать | Примеры |
|---|---|---|
| **Внутри доменной модели** | Всегда. Enum tightly coupled к одной сущности | `User.Role`, `UserProfile.Gender`, `WorkoutAccess.AccessLevel` |
| **Дублирован внутри JPA Entity** | Всегда. Те же значения, отдельный enum в entity | `UserEntity.Role`, `UserProfileEntity.Gender`, `WorkoutAccessEntity.AccessLevel` |
| **Отдельный файл** | Только если enum переиспользуется между **разными** сущностями (таких пока нет) | — |

**Конвертация в Infrastructure Mapper:**

```java
// toDomain: entity → domain
User.Role.valueOf(entity.getRole().name())

// toEntity: domain → entity
UserEntity.Role.valueOf(domain.getRole().name())
```

Значения enum всегда идентичны, поэтому конвертация через `valueOf(name())`.

---

### 8. @DateTimeFormat — формат дат в контроллерах

| Тип Java | Аннотация | Пример |
|---|---|---|
| `LocalDate` | `@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)` | `2026-06-28` |
| `LocalDateTime` | `@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)` | `2026-06-28T15:30:00` |

- Всегда на `@RequestParam` (не на `@RequestBody`)
- `required = false` + дефолт в use case (если null — подставляется `now()`)

---

### 9. @EntityGraph — борьба с N+1

Используется на derived queries с `AndUserId` в JPA-репозиториях для подгрузки `user`:

```java
@EntityGraph(attributePaths = "user")
Optional<HydrationEntity> findByIdAndUserId(Long id, Long userId);
```

Применяется в: `JpaHydrationRepository`, `JpaMealRepository`, `JpaWorkoutSessionRepository`.

---

### 10. ResponseEntity в проекте

**Контроллеры** — никогда не используют `ResponseEntity`. Статус через `@ResponseStatus`, возврат — напрямую DTO.

**GlobalExceptionHandler** — единственное место, где используется `ResponseEntity` (для формирования ошибок).

---

### 11. @Transactional — method-level vs class-level

- **Method-level** — доминирующий паттерн (~60 use cases). Каждый метод аннотирован отдельно.
- **Class-level** — только если **все** методы read-only (исключение: `FindWorkoutAccessUseCase`).

Правило: предпочтение method-level, class-level только для read-only классов без write-методов.
