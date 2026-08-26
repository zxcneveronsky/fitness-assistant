# ERRORS.md — Аудит репозиториев, адаптеров и SQL (ревизия 2)

Обновлено: 25.08.2026 после перепроверки. Первичный аудит + верификация фиксов.
Область: `core/repository/*`, `infrastructure/adapter/*`, `infrastructure/persistence/jpa/*`, entity/мапперы, `db/migration/*.sql`.
Контекст: Spring Boot 3.4.3 (Hibernate 6.6), PostgreSQL + Flyway, Caffeine-кэш, `open-in-view: false`, `default_batch_fetch_size: 30`.

> Ревизия 2: исправленные пункты из ревизии 1 убраны из плана работ (список внизу для истории).
> Открытых CRITICAL нет.

---

## 🟠 HIGH

### H1. NULL в `consumed_at` через update-эндпоинты → строки выпадают из дневных агрегатов

**Файлы:**
- `web/dto/request/update/UpdateMealRequest.java:32-33`, `web/dto/request/update/UpdateHydrationRequest.java:17-18`
- `db/migration/V07__create_meals_table.sql`, `V08__create_hydration_table.sql` (`consumed_at TIMESTAMP` без NOT NULL)
- `JpaMealRepository.getDailyNutrition`, `JpaHydrationRepository.getDailyHydration`

**Проблема:** Create-DTO защищены (`@NotNull @PastOrPresent`), но Update-DTO имеют **только `@PastOrPresent`** — `consumedAt=null` проходит валидацию и записывается в БД. Схема допускает NULL. Агрегаты фильтруют `consumedAt >= :startOfDay AND < :endOfDay` → записи с NULL **молча исчезают** из дневной суммы калорий/воды.

**Фикс (оба уровня):**
1. В оба Update-DTO добавить `@NotNull(message = "Дата приема должна быть указана")`.
2. Миграция V20 (defense in depth): бэкфилл при необходимости + `ALTER TABLE meals ALTER COLUMN consumed_at SET NOT NULL; ALTER TABLE hydrations ALTER COLUMN consumed_at SET NOT NULL;`
3. Проверить сервисные UpdateUseCase: что при null в домене они не затирают значение (частичное обновление).

---

## 🟡 MEDIUM

### M1. Дублирование кода между параллельными адаптерами

1. **`fetchExercisesPage(Page<Long>, Pageable)`** — идентичные приватные методы ~25 строк в `ExerciseRepositoryAdapter` и `FavoriteExerciseRepositoryAdapter`. Фикс: общий support-компонент (`infrastructure/adapter/support/ExercisePageFetcher`).
2. **Оконность дня** (`atStartOfDay()/atTime(LocalTime.MAX)` + fallback на «сегодня») скопирована в `MealRepositoryAdapter` (searchMeal, getDailyNutrition) и `HydrationRepositoryAdapter` (searchHydration, getDailyHydration) — 4 места, плюс скрытая таймзонная зависимость (см. L3). Фикс: хелпер `DateRange.ofNullable(LocalDateTime)`.
3. **Ручной merge/persist с EntityManager** в трёх адаптерах: `StreakRepositoryAdapter`, `TargetsRepositoryAdapter`, `UserProfileRepositoryAdapter`. Остальные 13 используют `jpaRepo.save()`. Паттерн `if (id != null && existsById(id)) merge else persist` делает тот же лишний SELECT, от которого пытались уйти; persist без flush возвращает домен до INSERT. Фикс: `implements Persistable` на `StreakEntity`/`TargetsEntity`/`UserProfileEntity` (`isNew()` = false после загрузки), адаптеры свести к `jpaRepo.save()`, EntityManager выпилить.

### M2. Лишние JOIN FETCH / EntityGraph

1. `JpaSetRepository.findByExerciseIdAndUserIdAndStartTimeBetween` (~строка 33): `JOIN FETCH sess.workout w` — маппер читает только `session.id`; JOIN workouts выполняется впустую на каждый запрос истории упражнения. Фикс: убрать `, w` (оставить fetch session).
2. `@EntityGraph(attributePaths = "user")` на 5 методах — мапперы читают только `user.getId()` (прокси отдаёт id без SQL): `JpaMealRepository.findByIdAndUserId`, `JpaHydrationRepository.findByIdAndUserId`, `JpaWorkoutRepository.findByIdAndUserId`, `JpaWorkoutSessionRepository.findByIdAndUserId` и `findAllByUserId`. Фикс: удалить; если позже мапперы начнут читать email — вернуть точечно.

### M2bis. Игнор `Pageable.getSort()` в поиске упражнений

8 ID-пагинационных запросов захардкожены `ORDER BY e.name ASC`; sort клиента молча отбрасывается, при этом Spring Data добавляет sort из Pageable в count query → возможный рассинхрон content/count. Фикс: задокументировать контракт «сортировка всегда name» и передавать `Sort.unsorted()` из адаптеров, либо поддержать сортировку.

### M3. Хрупкий контракт `existsAllByIdIn`

**Файл:** `ExerciseRepositoryAdapter.existsAllByIdIn:77-80`: `ids == null || isEmpty → true` (пустая тренировка проходит валидацию!), `countAllByIdIn(ids) == ids.size()` ломается на дублях — dedup делегирован сервисам (`CreateWorkoutUseCase`, `UpdateWorkoutUseCase`). Фикс внутри адаптера:
```java
List<Long> distinct = ids == null ? List.of() : ids.stream().distinct().toList();
return !distinct.isEmpty() && jpaExerciseRepository.countAllByIdIn(distinct) == distinct.size();
```
и решить на уровне use case, допустима ли пустая тренировка (сейчас — да).

### M4. Нет optimistic locking

Ни одна entity не имеет `@Version` — конкурентное редактирование workout/workout_access теряет изменения. Фикс: минимум `WorkoutEntity` (+ колонка version в V20), обработка `OptimisticLockingFailureException` в web-слое.

---

## 🔵 LOW

### L1. Индексы — миграция V20

- Удалить избыточный `idx_workout_exercises_workout_id` (V18) — покрыт PK `(workout_id, exercise_id)`.
- Добавить `CREATE INDEX ... ON workout_access(workout_id)` — нужен для `findByWorkoutIdAndOwnerId` и EXISTS в `existsAccessible`/`findAllAccessibleByIdIn`; UNIQUE(owner_id,...) покрывает только префикс owner_id.
- `ORDER BY f.name/e.name` не использует LOWER-trgm индексы (V19), plain btree отсутствует → in-memory sort на каждую страницу. Фикс: btree на `foods(name)`, `exercises(name)` или сортировать по `LOWER(name)` консистентно с индексами.
- Btree-индексы из V18 (`*_name_lower`) бесполезны для `LIKE '%...%'` — перекрыты trgm из V19; кандидаты на удаление.

### L2. Bulk DELETE без очистки persistence context

`JpaWorkoutAccessRepository.deleteByIdAndOwnerId`: `@Modifying @Query(DELETE ...)` без `clearAutomatically = true` — в той же транзакции L1-кэш может держать уже удалённую entity. Текущий поток безопасен (сразу exception/return), но задел на баг. Фикс: добавить `clearAutomatically = true`.

### L3. `LocalDate.now()` внутри адаптеров + таймзона

`MealRepositoryAdapter`, `HydrationRepositoryAdapter`: при null дате берётся «сегодня» серверной таймзоны. Скрытая зависимость, ломает тесты и пользователей других зон. Фикс: бин `Clock` или обязательная дата от вызывающего.

### L4. Enum-дубли домен ↔ entity

`Role`, `Gender`, `AccessLevel` продублированы, конвертация `valueOf(name())` размазана по 10+ местам. Консистентно, но шумно. Опционально: MapStruct/конвертеры.

### L5. Update WorkoutAccess через полный merge

`UpdateWorkoutAccessUseCase` → `findByIdAndOwnerId` (JOIN FETCH ×3) → домен → `save()` = merge с 3×`getReferenceById` ≈ 5 SQL на смену access_level. Замена на `@Modifying UPDATE WorkoutAccessEntity wa SET wa.accessLevel = :level WHERE ...` — 1 запрос.

### L6. Логирование SQL

`application.yml`: `org.hibernate.SQL` / `orm.jdbc.bind` на INFO — шумно для прода; понизить до WARN или вынести в dev-профиль.

---

## ✅ RESOLVED (ревизия 1 → проверено 25.08, не возвращать)

| Было | Решение в коде |
|---|---|
| C1 DISTINCT+ORDER BY (8 запросов) | Переписаны на `GROUP BY e.id, e.name` + явные countQuery (`JpaExerciseRepository`, `JpaFavoriteExerciseRepository`) |
| C2 N+1 ElementCollection | `@BatchSize` удалены; глобальный `default_batch_fetch_size: 30` |
| C3 Кросс-инвалидация кэша | `@CacheEvict({"muscle","exercise"})` в MuscleRepositoryAdapter |
| H1 Кэш «muscle» ×4 метода | Остался только `findById` |
| H2 Мёртвый `UserRepository.getReferenceById` | Удалён из порта и адаптера |
| H3 Голый delete/find в WorkoutAccess | Атомарный `deleteByIdAndOwnerId` (@Modifying long); DeleteUseCase на count-паттерне |
| H4 Ленивые SELECT в WorkoutAccessMapper | `findByIdAndOwnerId` с полным JOIN FETCH |
| L4 Кэш `findAllByIdIn` по списку | Аннотация снята, кэшится только findById |

Проверено и подтверждено корректным (не трогать): INNER JOIN в `findPageIdsByMuscleId*` консистентен со своими countQuery; маскировка отказа как 404 в `FindWorkoutUseCase.findById`; консистентность копии workout благодаря FK CASCADE; `open-in-view: false`; LAZY по умолчанию; EXISTS вместо JOIN для favorite; агрегаты SUM для дневных итогов; JOIN FETCH в `findAllSetDetailBySessionId`.

## 📋 Чеклист верификации после фиксов

1. **H1:** тест update meal/hydration с null consumedAt → 400; миграция V20 накатывается на БД со старыми данными.
2. **M1.3:** после перевода на Persistable — создание И обновление profile/targets/streak работают, SQL без лишних SELECT (Hibernate Statistics).
3. **M3:** пустой список id → false; список с дублями `[1,1,2]` → true при существующих 1,2.
4. **M4:** конкурентный PUT workout → второй запрос получает 409.
5. Регресс: поиск упражнений (name/muscle/без фильтров), избранное, история упражнения, детали сессии, шаринг доступа — `mvn verify` зелёный.

