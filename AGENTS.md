# Fitness Assistant — проект Т-Класс, 130 лицей, Екатеринбург

## Стек
Spring Boot 3.4.3 / Java 21 / PostgreSQL 16 / Maven
Clean Architecture (4 слоя): Controller → UseCase → Repository(Adapter) → JPA

## Команда
- **Мейн-разработчик** — весь бэк + фронт
- **Первый участник** — логирование (SLF4J, log.info во всех 44 use case)
- **Тимофей** — данные (CSV: 512 продуктов, 109 упражнений, 33 мышцы)

---

# 1. АРХИТЕКТУРА БЭКЕНДА

## 1.1 Clean Architecture (4 слоя)

```
web/controller/         → REST контроллеры (10 шт)
  ↓ вызов
application/service/    → Use Case'ы (44 шт) — бизнес-логика
  ↓ вызов
core/repository/        → Интерфейсы репозиториев (абстракция)
  ↓ реализация
infrastructure/adapter/ → JPA-реализации репозиториев (10 шт)
  ↓
infrastructure/persistence/jpa/ → Spring Data JPA репозитории
infrastructure/persistence/entity/ → Entity-классы (JPA)
infrastructure/mapper/  → Entity ↔ Domain мапперы (12 шт)
```

### Полная структура пакетов:
```
src/main/java/com/example/fitness_assistant/
├── Application.java                              — @SpringBootApplication + @EnableCaching
├── core/
│   ├── model/                                    — Доменные модели (POJO, Lombok)
│   │   ├── User.java                             — id, email, password, role (USER/ADMIN)
│   │   ├── UserProfile.java                      — id, name, birthDate, weight, height, gender, target*, useAutopilot
│   │   ├── Targets.java                          — targetKcal, targetProteins, targetFats, targetCarbs, targetHydration
│   │   ├── Exercise.java                         — id, name, description, List<Muscle> muscles
│   │   ├── Muscle.java                           — id, name
│   │   ├── Food.java                             — id, name, brands, kcal, proteins, fats, carbs
│   │   ├── Set.java                              — id, sessionId, exerciseId, weight, reps, createdAt
│   │   ├── WorkoutSession.java                   — id, workoutId, userId, startTime, endTime
│   │   ├── workout/
│   │   │   ├── Workout.java                      — id, userId, name, List<Long> exercisesIds
│   │   │   └── WorkoutWithExercise.java          — id, userId, name, List<Exercise> exercises
│   │   ├── meal/
│   │   │   ├── Meal.java                         — id, userId, name, brands, kcal, proteins, fats, carbs, consumedAt
│   │   │   └── DailyNutrition.java               — kcal, proteins, fats, carbs (агрегат)
│   │   └── hydration/
│   │       ├── Hydration.java                    — id, userId, name, amount, consumedAt
│   │       └── DailyHydration.java               — totalAmount (агрегат)
│   ├── repository/                               — Интерфейсы (не Spring Data, а кастомные!)
│   │   ├── UserRepository.java
│   │   ├── UserProfileRepository.java
│   │   ├── ExerciseRepository.java
│   │   ├── FoodRepository.java
│   │   ├── MealRepository.java
│   │   ├── HydrationRepository.java
│   │   ├── WorkoutRepository.java
│   │   ├── WorkoutSessionRepository.java
│   │   ├── SetRepository.java
│   │   └── MuscleRepository.java                 — только getReferenceById
│   └── exception/                                — 11 кастомных исключений
│       ├── UserNotFoundException.java
│       ├── UserAlreadyExistsException.java
│       ├── InvalidPasswordException.java
│       ├── UserProfileNotFoundException.java
│       ├── ExerciseNotFoundException.java
│       ├── FoodNotFoundException.java
│       ├── MealNotFoundException.java
│       ├── HydrationNotFoundException.java
│       ├── WorkoutNotFoundException.java
│       ├── WorkoutSessionNotFoundException.java
│       └── SetNotFoundException.java
├── application/
│   └── service/
│       ├── exercise/        — 5 use case
│       ├── food/            — 6 use case (+CalculateFoodUseCase)
│       ├── meal/            — 5 use case
│       ├── hydration/       — 5 use case
│       ├── set/             — 5 use case
│       ├── workout/         — 5 use case
│       ├── workoutsession/  — 5 use case
│       ├── user/            — 3 use case (+LoginResult.java)
│       ├── targets/         — 2 use case
│       └── profile/         — 4 use case
├── infrastructure/
│   ├── adapter/             — Repository адаптеры (10 шт)
│   ├── mapper/              — Entity ↔ Domain (12 шт)
│   ├── persistence/
│   │   ├── entity/          — JPA Entity (10 шт, @Entity, @Table)
│   │   ├── jpa/             — Spring Data JPA репозитории
│   │   └── projection/      — Интерфейсы для агрегирующих запросов
│   ├── security/
│   │   ├── JwtService.java  — генерация/валидация JWT
│   │   ├── JwtFilter.java   — OncePerRequestFilter, try-catch JwtException
│   │   └── UserDetailsAdapter.java — record implements UserDetails
│   ├── config/
│   │   ├── SecurityConfig.java — permitAll + authenticated + jwtFilter
│   │   └── ApplicationConfig.java — UserDetailsService, PasswordEncoder, AuthenticationManager
│   └── init/
│       └── DataInitializer.java — CommandLineRunner, загрузка CSV + demo user
└── web/
    ├── controller/          — 10 REST контроллеров
    ├── dto/
    │   ├── request/create/  — 11 DTO
    │   ├── request/update/  — 9 DTO
    │   └── response/        — 14 DTO
    ├── mapper/              — Domain ↔ DTO (13 шт)
    └── handler/
        └── GlobalExceptionHandler.java — @RestControllerAdvice
```

## 1.2 Как работает связка слоёв

```
Client → Controller (@RequestBody DTO)
  → WebMapper.toDomain(request)        → Domain model
  → UseCase.method(domainModel)         → бизнес-логика
  → Repository.find/save(domainModel)
  → RepositoryAdapter
  → Mapper.toEntity(domainModel)        → JPA Entity
  → JpaRepository.save(entity)
  → Mapper.toDomain(entity)             → Domain model
  → WebMapper.toResponse(domain)        → Response DTO
  → Client ← JSON
```

## 1.3 Детали реализации Use Case

Каждый Use Case — `@Service @RequiredArgsConstructor @Slf4j`:
- Одна транзакция на один метод (`@Transactional`)
- Логирование: `log.info("Действие | id={} | поле='{}'", ...)`
- Исключения: `throw new XxxNotFoundException(id)`
- Валидация входных данных — через `@Valid` на DTO в контроллере

### Пример: UpdateFoodUseCase
```java
@Transactional
public Food updateFood(Food foodUpdate) {
    Long id = foodUpdate.getId();
    Food updatedFood = foodRepository.findById(id)
            .map(existingFood -> {
                existingFood.setName(foodUpdate.getName() != null ? foodUpdate.getName() : existingFood.getName());
                // ... все поля с тернарниками
                return foodRepository.save(existingFood);
            })
            .orElseThrow(() -> new FoodNotFoundException(id));
    log.info("Продукт обновлен | id={}", id);
    return updatedFood;
}
```

### Пример: UpdateTargetsUseCase (targets)
```java
if (request.getTargetKcal() != null || hasMacros(request) || request.getTargetHydration() != null) {
    profile.setUseAutopilot(false);  // отключаем автопилот при ручном вводе
    targetCalculationService.applyManualTargets(profile, request);
}
```

### TargetCalculationService.applyManualTargets
```java
if (hasMacros(request)) {
    // есть белки/жиры/углеводы → стабилизация ПО МАКРОСАМ
    setMacros();
    balanceCaloriesByMacros(profile);  // ккал = сумма макросов
}
else if (request.getTargetKcal() != null) {
    // есть только ккал → стабилизация ПО КАЛОРИЯМ
    setKcal();
    balanceMacrosByCalories(profile);  // БЖУ = 30%/30%/40% от ккал
}
// Вода — всегда независимо
```

### Пример: CreateWorkoutUseCase
```java
Workout savedWorkout = workoutRepository.save(workout);
log.info("Тренировка создана | id={}", savedWorkout.getId());
```

## 1.4 Исключения (GlobalExceptionHandler)

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Все NotFound → 404
    @ExceptionHandler({MealNotFoundException.class, ExerciseNotFoundException.class, ...})
    public ResponseEntity<Object> handleNotFound(RuntimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    // Дубликат email → 409
    @ExceptionHandler(UserAlreadyExistsException.class) → HttpStatus.CONFLICT
    // Неверный пароль → 401
    @ExceptionHandler(InvalidPasswordException.class) → HttpStatus.UNAUTHORIZED
}
```

Ответ всегда в формате:
```json
{
  "timestamp": "2026-05-16T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Упражнение с id 999 не найдено"
}
```

---

# 2. БЕЗОПАСНОСТЬ

## 2.1 JWT (jjwt 0.12.6)

### JwtService
- Секретный ключ: `${JWT_SECRET}` (Base64, минимум 256 бит)
- Срок жизни: 24 часа
- Claims: `userId`, `sub` (email), `iat`, `exp`
- Подпись: HMAC-SHA256

### JwtFilter (OncePerRequestFilter)
```java
try {
    // парсинг и валидация токена
    String userEmail = jwtService.extractUsername(jwt);
    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        if (jwtService.isTokenValid(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
} catch (JwtException | IllegalArgumentException e) {
    log.warn("Невалидный JWT: {}", e.getMessage());
    // не ставим authentication → Spring Security вернёт 401
}
filterChain.doFilter(request, response);
```

**Важно:** без try-catch при expired/malformed токене → 500.

### UserDetailsAdapter
```java
public record UserDetailsAdapter(User user) implements UserDetails {
    public Long getUserId() { return user.getId(); }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }
}
```

## 2.2 SecurityConfig

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers(
        "/", "/index.html", "/login.html", "/register.html",
        "/dashboard.html", "/meal.html", "/hydration.html",
        "/workout.html", "/workout-detail.html", "/session.html",
        "/session-detail.html", "/history.html", "/profile.html",
        "/js/**", "/explore/**",
        "/api/v1/auth/**", "/api/v1/food/**", "/api/v1/exercises/**",
        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml"
    ).permitAll()
    .anyRequest().authenticated()
)
.csrf(csrf -> csrf.disable())
.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
```

**Важно:** HTML-страницы все в permitAll! Auth проверяется на JS (isAuth() → редирект на login). Так надо, потому что браузер не передаёт Bearer-токен при загрузке HTML.

---

# 3. БАЗА ДАННЫХ

## 3.1 Миграции Flyway (18 шт)

| Миграция | Таблица | Описание |
|---|---|---|
| V1 | users | id, email (unique), password, role |
| V2 | users_profiles | id=FK(user), name, birthDate, weight, height, gender, target*, useAutopilot |
| V3 | foods | id, name, brands, kcal, proteins, fats, carbs |
| V4 | muscles | id, name (unique) |
| V5 | exercises | id, name, description |
| V6 | exercise_muscles | exercise_id FK, muscle_id FK, PK(exercise_id, muscle_id) |
| V7 | meals | id, user_id FK, name, brands, kcal, proteins, fats, carbs, consumed_at |
| V8 | search indexes | — |
| V9 | profiles: target macros | ALTER TABLE add target_proteins/fats/carbs |
| V10 | hydrations | id, user_id FK, name, amount, consumed_at |
| V11 | profiles: target hydration | ALTER TABLE add target_hydration |
| V12 | workouts | id, user_id FK, name |
| V13 | workout_exercises | workout_id FK, exercise_id FK |
| V14 | indexes | — |
| V15 | workout_sessions | id, workout_id FK, user_id FK, start_time, end_time |
| V16 | indexes | — |
| V17 | sets | id, session_id FK, exercise_id FK, weight, reps, created_at |
| V18 | indexes | — |

## 3.2 application.yml (ключевые параметры)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fitness_assistant
    username: postgres
    password: ${DB_PASSWORD}
  jpa:
    open-in-view: false        # OSIV выключен! Важно для производительности
    hibernate:
      ddl-auto: none           # Только Flyway управляет схемой
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
  cache:
    type: caffeine
    cache-names: exercises, food, muscles, userProfiles
    caffeine:
      spec: maximumSize=500,expireAfterAccess=1h
jwt:
  secret: ${JWT_SECRET}
logging:
  level:
    com.example.fitness_assistant: DEBUG
management:
  endpoints:
    web:
      exposure:
        include: health, info, metrics
  endpoint:
    health:
      show-details: always
```

---

# 4. КЭШ (Caffeine)

## 4.1 Где применяется

| Кэш | Класс | @Cacheable на | @CacheEvict на |
|---|---|---|---|
| exercises | ExerciseRepositoryAdapter | findById, findAllByIdIn | save, deleteById |
| food | FoodRepositoryAdapter | findById | save, deleteById |
| muscles | MuscleRepositoryAdapter | getReferenceById | — (справочник) |
| userProfiles | UserProfileRepositoryAdapter | findById | save, deleteById |

## 4.2 Что НЕ кэшируем
- `findAll(pageable)` — пагинация, комбинаторика ключей
- `searchExercise/` `searchFood` — поиск + пагинация
- User-specific данные (Meal, Hydration, Set, WorkoutSession)

---

# 5. DATANITIALIZER (подробно)

## 5.1 Логика работы

```java
@Component
public class DataInitializer implements CommandLineRunner {
    public void run(String... args) {
        if (foodRepository.count() > 0) {
            log.info("Данные уже загружены, инициализация пропущена");
            return;  // защита от повторной загрузки
        }
        loadFood();       // 1. food.csv → saveAll
        loadMuscles();     // 2. muscles.csv → saveAll
        loadExercises();   // 3. exercises.csv → muscleMap → saveAll
        createDemoUser();  // 4. user@example.com / password123
    }
}
```

## 5.2 Формат CSV

**food.csv:**
```
name,brands,kcal,proteins,fats,carbs
Грудка куриная отварная,Мираторг,137,29.8,1.8,0.0
```
- `ххх` в brands → null (бренд неизвестен)

**muscles.csv:**
```
name
Бицепс
Бицепс бедра
...
```

**exercises.csv:**
```
name,description,muscle_ids
"Алмазные отжимания","Поставь ладони ромбом...","7,28"
```
- `muscle_ids` — ID из muscles.csv (порядок загрузки = порядок ID)

## 5.3 Порядок загрузки
1. Muscles (saveAll → получают ID)
2. Exercises (readAllMuscles → muscleMap → для каждой строки маппим ID → MuscleEntity)

---

# 6. БЭКЕНД — ВАЖНЫЕ НЮАНСЫ

## 6.1 Имена полей в DTO (критично!)
| DTO | Поле | Где используется |
|---|---|---|
| `ExerciseResponse` | `exerciseName` | **НЕ name!** GET /exercises |
| `CreateWorkoutRequest` | `exercisesIds` | **С s!** POST /workout |
| `UpdateWorkoutRequest` | `exerciseIds` | **Без s!** PATCH /workout |
| `WorkoutResponse` | `exerciseIds` | **Без s!** GET /workout |
| `WorkoutWithExerciseResponse` | `exercises` | Список объектов ExerciseResponse |
| `CreateMealAutoRequest` | `id` (productId) + `weight` | POST /meal/auto |
| `CreateSetRequest` | `sessionId`, `exerciseId`, `weight`, `reps`, `createdAt` | **createdAt обязателен!** |
| `UpdateWorkoutSessionRequest` | `id`, `endTime` | PATCH /workout/session/end |

## 6.2 targets — только ИЛИ/ИЛИ
- Есть **только ккал** → `balanceMacrosByCalories` (30/30/40)
- Есть **только макросы** → `balanceCaloriesByMacros`
- Есть **и то, и другое** → `balanceCaloriesByMacros` (ккал пересчитается из макросов)
- **Вода** — всегда отдельно, не зависит от condition

## 6.3 Автопилот
- `useAutopilot = true` → при обновлении профиля цели пересчитываются автоматом
- При PATCH /profile/targets → `useAutopilot = false`
- Включить обратно: `PATCH /profile/targets/status?enabled=true`

## 6.4 WorkoutMapper
```java
// null-safe на exercisesIds — защита от NPE при PATCH без этого поля
public WorkoutEntity toEntity(Workout domain) {
    return new WorkoutEntity(domain.getId(), null, domain.getName(),
        domain.getExercisesIds() != null ? new ArrayList<>(domain.getExercisesIds()) : new ArrayList<>());
}
```

## 6.5 CalculateFoodUseCase — НЕ мутировать оригинал
```java
// ПЛОХО: food.setKcal(food.getKcal() * k); return food;
// ХОРОШО:
Food calculated = new Food(food.getId(), food.getName(), food.getBrands(),
    food.getKcal() * k, food.getProteins() * k, food.getFats() * k, food.getCarbs() * k);
return calculated;
```

## 6.6 LoginResult
```java
// Отдельный record в application/service/user/
public record LoginResult(String token, String email, String role) {}

// LoginUseCase.login() возвращает LoginResult
// RegisterUseCase.register() тоже возвращает LoginResult (генерирует токен сам)
```

## 6.7 UserWebMapper
```java
// Два метода:
public AuthResponse toAuthResponse(String token, User user) { ... }  // для register
public AuthResponse toAuthResponse(LoginResult result) { ... }        // для login
```

## 6.8 Error handling в API

| Ситуация | HTTP статус | Пример body |
|---|---|---|
| Неверные данные | 400 | `{"message":"Вес должен быть положительным"}` |
| Не найден ресурс | 404 | `{"message":"Продукт с id 999 не найден"}` |
| Дубликат email | 409 | `{"message":"Email already@exists.com уже существует"}` |
| Неверный пароль | 401 | `{"message":"Неверный пароль"}` |
| Нет/протух токен | 401 | Spring Security default |
| Внутренняя ошибка | 500 | (не должно быть на предсказуемых ошибках) |

---

# 7. ФРОНТЕНД (ПОДРОБНО)

## 7.1 Общая архитектура

### Технологии
- **Tailwind CSS** (CDN) — утилитарные классы, кастомизация через стили
- **Chart.js** (CDN) — пончиковые диаграммы на дашборде
- **Vanilla JS** — без фреймворков, все запросы через `fetch()`
- **api.js** — общий API-клиент (путь: `/js/api.js`)

### api.js — общий API-клиент

**Функции:**
```javascript
const API = 'http://localhost:8080/api/v1';  // базовый URL

getToken()              // достать JWT из localStorage
setToken(token)         // сохранить JWT в localStorage
clearToken()            // удалить JWT из localStorage
isAuth()                // проверить, есть ли токен

request(path, options)  // универсальный запрос:
                        // - добавляет Authorization: Bearer <token>
                        // - 401/403 → clearToken + redirect /login.html
                        // - 204 → return null
                        // - !ok → throw Error с message из body
                        // - ok → return JSON
```

**Работа с JWT:**
```javascript
// Логин → сохраняем токен
const data = await request('/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) });
setToken(data.token);

// Дальше все request() сами добавляют Authorization: Bearer <token>
```

**Активная тренировка (баннер):**
```javascript
// localStorage API:
getActiveSession()      // { id, name } или null
saveActiveSession(id, name)  // сохранить
removeActiveSession()   // удалить

renderActiveSessionBanner()  // показать баннер из localStorage (fast path)
syncActiveSessions()         // проверить API, обновить баннер
```

## 7.2 Страницы (подробно)

### 7.2.1 Публичные страницы

#### index.html — Лендинг
- Мотивационные фразы (ротация каждые 5 сек)
- Кнопки «Регистрация» / «Вход»
- Карточки возможностей (с замочками для auth-функций)
- **Nav:** лого → /index.html

#### login.html — Вход
- Форма: email + password
- `POST /auth/login` → сохраняет token в localStorage → redirect /dashboard.html
- Показывает ошибку если неверные данные
- **Демо-подсказка:** `user@example.com / password123`

#### register.html — Регистрация
- Форма: email + password (min 4 символа)
- `POST /auth/register` → сохраняет token → redirect /dashboard.html

#### explore/exercises.html — Поиск упражнений
- Публичный (без регистрации)
- Поиск по названию или мышце: `GET /exercises/search?name=X&page=Y&size=9`
- Карточки: название (`ex.exerciseName`), описание, список мышц
- Пагинация: ← Назад | 1 2 3 ... | Вперёд →
- **Nav:** Вход | Регистрация (если не авторизован)
- **Если авторизован** — лого ведёт на /dashboard.html, есть баннер активной сессии

#### explore/food.html — Поиск продуктов
- Публичный (без регистрации)
- Поиск: `GET /food/search?name=X&page=Y&size=9`
- Карточки: название, бренд, КБЖУ (4 колонки)
- Пагинация с ← Назад | ... | Вперёд →

### 7.2.2 Защищённые страницы (Auth)

**Общие элементы:**
- Nav: Главная | Еда | Вода | Тренировки | История | Профиль | Выйти
- Лого → /dashboard.html
- Баннер активной тренировки (sticky, top:64px, z-index:40)
- `if (!isAuth()) window.location.href = '/login.html';`
- Кнопка «Выйти» → `clearToken()` → /index.html

#### dashboard.html — Дашборд (самая сложная страница)

**Chart.js диаграммы:**
```javascript
// Пончиковые диаграммы (doughnut):
// Вода — голубая (#0071e3)
// Калории — зелёная (#34c759)
// cutout: '75%', animation: 600ms
// В центре: проценты + "из N"
```

**Блок 1 — Вода:**
- Заголовок: "Вода сегодня" + текущее значение мл
- Кнопка "+" (зелёная, ведёт на /hydration.html)
- Диаграмма-пончик
- Поле ввода + кнопка "+" (кастомное значение)
- Быстрые кнопки: +150 мл | +250 мл | +500 мл
- `quickAddWater(ml)` → POST /hydration + updateCharts()

**Блок 2 — Калории:**
- Заголовок: "Калории сегодня" + текущее значение ккал
- Кнопка "+" (зелёная, ведёт на /meal.html)
- Диаграмма-пончик
- БЖУ: белки (синие) | жиры (красные) | углеводы (зелёные)

**Блок 3 — Параметры тела:**
- Силуэт человека (SVG)
- Рост / Вес
- Кнопка «Изменить» → модалка с слайдерами

**Блок 4 — Feature cards:**
- Персональные параметры / История тренировок
- Поиск упражнений / Поиск продуктов
- Тренировки / Трекер питания / 💧 Трекер воды

**Загрузка данных:**
```javascript
async function loadData() {
    // 1. GET /profile/targets → waterNorm, kcalNorm
    // 2. GET /profile → weight, height, name
    // 3. GET /meal/daily?localDateTime=... → kcalToday, proteinsToday, fatsToday, carbsToday
    // 4. GET /hydration/daily?localDateTime=... → waterToday
    // 5. updateCharts()
}
// Автообновление: window.addEventListener('pageshow', loadData)
//                 document.addEventListener('visibilitychange', ...)
```

#### meal.html — Трекер питания

**Дата-пикер:**
```html
<input type="date" id="mealDate">
```
- По умолчанию — сегодня
- При смене даты — перезагружает список приёмов пищи
- `selectedDate()` → возвращает ISO строку выбранной даты

**Добавление приёма пищи:**
1. Поиск: `GET /food/search?name=X&size=20`
2. Результаты: название + ккал/100г + поле "г" + кнопка "+"
3. `addMeal(foodId)` → `POST /meal/auto` с `{ id, weight, name: 'auto', consumedAt }`

**Список съеденного:**
- `GET /meal/search?localDateTime=&size=50`
- Каждая запись: название, ккал, БЖУ, кнопка ✕ (удалить)

#### hydration.html — Трекер воды
- Дата-пикер (аналогично meal)
- Добавление: название + объём (л) + кнопка
- Список записей: название, объём, ✕ удалить
- `GET /hydration/search?localDateTime=&size=50`

#### workout.html — Список тренировок

**Создание тренировки (inline):**
1. Поле «Название»
2. Поиск упражнений: `GET /exercises/search?name=X&size=20`
3. Результаты: нажатие → добавляется в список выбранных
4. Выбранные: отображаются с ✕ (убрать)
5. Кнопка «Создать тренировку» → `POST /workout` с `{ name, exercisesIds: [...] }`
6. **Валидация:** `@NotEmpty` на exercisesIds — нужно выбрать хотя бы одно

**Список тренировок:**
- `GET /workout?page=0&size=50`
- Каждая: название, количество упражнений (`w.exerciseIds` — без s!)
- Кнопки: Открыть (→ workout-detail) | Старт (→ session?workoutId=X) | ✕ (удалить)

#### workout-detail.html — Детали тренировки
- Название тренировки
- Кнопка «Старт» → /session.html?workoutId=X
- **Поиск и добавление упражнений:** `GET /exercises/search` → нажатие → `PATCH /workout` с `{ id, exerciseIds }`
- Список упражнений с кнопкой «Убрать» (PATCH с удалённым ID)

#### session.html — Активная тренировка

**Два режима:**
1. `?workoutId=X` — **новая сессия**: старт → POST /session/start
2. `?sessionId=X` — **продолжить**: загрузить существующую + старые подходы

**Поток работы:**
```javascript
async function startNewSession(workoutId) {
    // 1. GET /workout/{id} → загружаем упражнения
    // 2. POST /workout/session/start → создаём сессию
    // 3. saveActiveSession(id, name) → в localStorage
    // 4. renderExercises() → отображаем карточки
}

async function resumeSession(sessionId) {
    // 1. GET /workout/session/{id} → проверяем что активна
    // 2. GET /workout/{workoutId} → загружаем упражнения
    // 3. renderExercises()
    // 4. Для каждого упражнения → loadSets(exerciseId)
}

// Добавление подхода:
// POST /workout/session/set с { sessionId, exerciseId, weight, reps, createdAt }
// createdAt = new Date().toISOString() — обязательно!
```

**Завершение сессии:**
```javascript
// PATCH /workout/session/end с { id, endTime }
// removeActiveSession() → redirect /history.html
```

#### session-detail.html — Детали сессии (просмотр)
- `GET /workout/session/{id}` → время, длительность
- `GET /workout/{workoutId}` → список упражнений
- Для каждого: `GET /workout/session/set?sessionId=&exerciseId=&size=50`
- Отображение: "Подход 1: 80 кг × 12 повторов"

#### history.html — История тренировок

**Пагинация:** `GET /workout/session/history?page=Y&size=10`
- Стрелки ← → + номера страниц

**Каждая сессия:**
- Дата + время начала (локализовано по-русски)
- **Название тренировки** (загружается через `/workout/{workoutId}` с кэшированием)
- Статус: ⏳ Активна / ✅ Завершена
- **Активная:** кнопка «Завершить» → `PATCH /workout/session/end`
- **Все:** кнопка «Удалить» → `DELETE /workout/session/{id}` с подтверждением
- Вся карточка — ссылка на session-detail

**Названия тренировок:** кэшируются в `workoutNameCache {}`. Первая загрузка — Promise.all.

#### profile.html — Профиль + Цели

**Параметры (левая колонка):**
- Имя, Вес (кг), Рост (см), Пол (MALE/FEMALE), Дата рождения

**Цели (правая колонка):**
- Калории (ккал), Белки (г), Жиры (г), Углеводы (г), Вода (л)

**Сохранение (критическая логика):**
```javascript
// Шаг 1: PATCH /profile (параметры без useAutopilot)
// Шаг 2: PATCH /profile/targets (только изменённые поля!)
```

**Отправка таргетов (важно!):**
```javascript
const t = {};
if (kcal) {
    t.targetKcal = kcal;           // если есть ккал → шлём ТОЛЬКО ккал
} else {
    // если ккал нет, но есть макросы → шлём макросы
    if (prot) t.targetProteins = prot;
    if (fat) t.targetFats = fat;
    if (carb) t.targetCarbs = carb;
}
if (hydr) t.targetHydration = hydr;  // вода — всегда отдельно
```

**Почему так:** API TargetCalculationService.applyManualTargets имеет две ветки:
- `hasMacros(request)` → рассчитать ккал из макросов
- `else if (targetKcal != null)` → рассчитать макросы из ккал
Если слать и ккал, и макросы — сработает `hasMacros` (ккал пересчитается, игнорируя введённый).

**Автопилот:**
- Кнопка «Авторасчёт» → `PATCH /profile/targets/status?enabled=true`
- После авторасчёта → `loadProfile()` → форма обновляется

**После сохранения:**
```javascript
await loadProfile();  // важно: await, чтобы форма обновилась ДО сообщения
showError('✅ Сохранено!');
```

### 7.2.3 Баннер активной тренировки

**Где находится:** на всех 11 защищённых страницах (добавлен после `</nav>`)
**Позиция:** `position:sticky;top:64px;z-index:40`
**Цвет:** жёлтый (`bg-yellow-50 border-yellow-200`)

**Как работает:**
```javascript
// 1. Быстрый путь: renderActiveSessionBanner() — из localStorage
// 2. Синхронизация: syncActiveSessions() — из API
//    - GET /workout/session/history?page=0&size=20
//    - фильтр: !s.endTime (активные сессии)
//    - для каждой: GET /workout/{id} → название тренировки
//    - кэш: workoutNameCache
```

**Отображение:**
- 1 активная: `⏳ Активная тренировка — [название] → Продолжить`
- 2+ активных: `⏳ N активные тренировки → [назв1] [назв2] [назв3]`

**При старте сессии:**
```javascript
saveActiveSession(sessionId, workoutName)  // → localStorage
```

**При завершении/удалении:**
```javascript
removeActiveSession()  // → localStorage
// syncActiveSessions() обновит баннер при следующей загрузке страницы
```

## 7.3 Навигация и переходы

```
/index.html (лендинг)
 ├── /login.html → POST /auth/login → /dashboard.html
 ├── /register.html → POST /auth/register → /dashboard.html
 ├── /explore/exercises.html (публичный)
 └── /explore/food.html (публичный)

/dashboard.html (после логина)
 ├── Nav: Еда (/meal.html)
 ├── Nav: Вода (/hydration.html)
 ├── Nav: Тренировки (/workout.html)
 ├── Nav: История (/history.html)
 ├── Nav: Профиль (/profile.html)
 ├── Card: Персональные параметры → /profile.html
 ├── Card: История тренировок → /history.html
 ├── Card: Поиск упражнений → /explore/exercises.html
 ├── Card: Поиск продуктов → /explore/food.html
 ├── Card: Тренировки → /workout.html
 ├── Card: Трекер питания → /meal.html
 └── Card: Трекер воды → /hydration.html

/workout.html
 ├── Каждая тренировка → Открыть → /workout-detail.html?id=X
 ├── Старт → /session.html?workoutId=X
 └── ✕ Удалить

/workout-detail.html
 ├── Старт → /session.html?workoutId=X
 └── Добавить/убрать упражнения

/session.html
 └── Завершить → /history.html

/history.html
 ├── Активная → Завершить → PATCH /session/end
 ├── Любая → Удалить → DELETE /session/{id}
 └── Клик → /session-detail.html?id=X
```

---

# 8. ВАЖНЫЕ НЮАНСЫ (ЧЕКЛИСТ)

## 8.1 Backend

| № | Нюанс | Подробнее |
|---|---|---|
| 1 | `ExerciseResponse.exerciseName` | Не `name`! Фронт читает `ex.exerciseName` |
| 2 | `CreateWorkoutRequest.exercisesIds` | С s! POST |
| 3 | `UpdateWorkoutRequest.exerciseIds` | Без s! PATCH |
| 4 | `WorkoutResponse.exerciseIds` | Без s! GET |
| 5 | `CreateSetRequest.createdAt` | Обязателен! @NotNull. Фронт шлёт `new Date().toISOString()` |
| 6 | `UpdateWorkoutSessionRequest.id + endTime` | PATCH /session/end |
| 7 | `CalculateFoodUseCase` | **Не** мутировать оригинал. Возвращать `new Food(...)` |
| 8 | `TargetCalculationService.applyManualTargets` | ИЛИ ккал, ИЛИ макросы. Не вместе |
| 9 | `UpdateTargetsUseCase` | Проверять `getTargetHydration() != null` |
| 10 | `WorkoutMapper.toEntity` | null-safe на `exercisesIds` |
| 11 | `JwtFilter` | try-catch `JwtException`, иначе 500 |
| 12 | `SecurityConfig` | HTML-страницы все в permitAll |
| 13 | `application.yml` | `open-in-view: false` |
| 14 | `DataInitializer` | Проверка `foodRepository.count() > 0` |

## 8.2 Frontend

| № | Нюанс | Подробнее |
|---|---|---|
| 1 | `request()` | При 401 → clear + redirect /login.html |
| 2 | `renderActiveSessionBanner()` | Проверять через `getActiveSession()` + `getElementById` |
| 3 | `syncActiveSessions()` | API может вернуть null |
| 4 | Profile: targets | Отправлять ИЛИ ккал, ИЛИ макросы. Не всё вместе |
| 5 | Profile: `await loadProfile()` | После сохранения — дождаться обновления формы |
| 6 | Meal: `selectedDate()` | Если дата не выбрана — `new Date().toISOString()` |
| 7 | Session: `createdAt` | Обязательно! В body POST /session/set |
| 8 | Workout: `exerciseIds` vs `exercisesIds` | Create = s, Update = без s, Response = без s |
| 9 | History: `w.exerciseIds` | Читать `w.exerciseIds` (без s!), не `w.exercisesIds` |
| 10 | Dashboard: `waterNorm` | Только из `/profile/targets`, не `weight * 30` |

---

# 9. МЕТРИКИ ПРОЕКТА

| Метрика | Значение |
|---|---|
| Java файлов | 190 |
| Строк кода (без пустых) | ~5000 |
| Use Case | 44 |
| Эндпоинты | 52 |
| Контроллеры | 10 |
| DTO | 34 |
| Мапперы (infra + web) | 25 |
| JPA сущности | 10 |
| Repository адаптеры | 10 |
| Кастомные исключения | 11 |
| Flyway миграции | 18 |
| Продукты | 512 |
| Упражнения | 109 |
| Мышцы | 33 |
| Страницы фронта | 14 |
| CSS/Tailwind | CDN |
| API документация | Swagger UI |

---

# 10. ЗАПУСК

```bash
# 1. PostgreSQL в Docker
docker run -d --name fitness-db \
  -e POSTGRES_DB=fitness_assistant \
  -e POSTGRES_PASSWORD=your_pass \
  -p 5432:5432 postgres:16

# 2. Создать .env в корне проекта
DB_PASSWORD=your_pass
JWT_SECRET=base64-encoded-secret-at-least-256-bits

# 3. Запуск
mvn spring-boot:run

# 4. Открыть
# http://localhost:8080/ — лендинг
# http://localhost:8080/swagger-ui.html — API документация
# http://localhost:8080/actuator/health — healthcheck

# 5. Демо-пользователь
# POST /api/v1/auth/login
# {"email":"user@example.com","password":"password123"}
```

## .env (добавлен в .gitignore)
```env
DB_PASSWORD=your_db_password
JWT_SECRET=base64-encoded-secret-at-least-256-bits
```
