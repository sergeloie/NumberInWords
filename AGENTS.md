# AGENTS.md — Инструкции для кодинг-агента

Этот файл — точка входа для любого агента, работающего в этом репозитории.
Прочитай его первым. Он отвечает на вопрос "где что лежит и по каким правилам работаем".

---

## 1. Карта каталогов

| Путь | Назначение |
|------|------------|
| `gradle/guardrails.gradle.kts` | script plugin: ВСЕ проверки кода (tier1-3, gitleaks, osv, languageCheck) |
| `config/checkstyle/`, `config/pmd/`, `config/spotbugs/` | конфиги линтеров |
| `config/githooks/` | pre-commit hook (Gitleaks), ставится `./gradlew installGitHooks` |
| `docs/agents/guardrails.md` | зоны инструментов, команды, формат summary |
| `.gitattributes`, `.gitleaksignore`, `osv-scanner.toml` | гигиена строк, baseline секретов, baseline уязвимостей |
| `src/main/java/ru/anseranser/` | приложение: `api/`, `service/`, `common/`, `config/`, `dto/`, `enums/` |
| `src/main/resources/cases/*.json` | словари склонений (по падежам) |

## 2. Архитектура (кратко)

Приложение "Числа прописью": Spring Boot 3.5, REST `POST /convert` (JSON), число → русские слова.

```
ru.anseranser
├── NumberInWordsApplication     # точка входа (Bootstrap)
├── api/                         # NumberController (@RestController)
├── dto/                         # NumberInputDTO / NumberOutputDTO (общие между api и service)
├── enums/                       # Cases / Genders (общие, без зависимостей)
├── service/                     # WordConverter (вся бизнес-логика; только сюда из api)
├── common/exception/            # RestErrorHandler (@RestControllerAdvice)
└── config/                      # CaseConfig (загрузка словарей)
```

Правила (проверяются ArchUnit в `src/test/java/ru/anseranser/ArchitectureTest.java`):
- `api` зависит от `service`, не наоборот (service не знает про `api`).
- `service` не зависит от Spring MVC/Servlet (только `api`).
- `common` не зависит ни от кого — только от него зависят.
- Логика — в `service`, контроллеры тонкие.

## 3. Общие правила для агента

1. **Читай `docs/agents/guardrails.md` перед коммитом** — там зоны инструментов и формат вывода.
2. **Не ломай сборку.** `./gradlew tiers --continue` — зелёный после каждого шага (PASSED/PARTIAL, без новых FAIL).
3. **Языки: английский и русский.** Русский — только в `.md/.txt/.json`; код и конфиги — ASCII. Проверка: `./gradlew languageCheck`.
4. **Не создавай файлы в корне** без необходимости.
5. **Спрашивай перед архитектурными решениями.** Трудно откатить + трейд-офф + удивит следующего → предложи ADR в `docs/adr/`.
6. **Маленькие вертикальные срезы.** Один эндпоинт end-to-end лучше 5 слоёв без интеграции.
7. **SonarQube у проекта — свой SonarCloud** (конфиг в `build.gradle.kts`); локальный SonarQube не разворачивать.

## 4. Команды (проверки кода)

```bash
./gradlew tier1 --continue   # быстро перед коммитом (Spotless, Checkstyle, ErrorProne, Gitleaks, Language)
./gradlew tier2 --continue   # CI (PMD, SpotBugs, ArchUnit, JaCoCo, OSV-Scanner)
./gradlew tier3 --continue   # тяжёлое/ночное (PIT, статус SonarQube)
./gradlew tiers --continue   # все сразу
./gradlew check              # стандартный check (ядро tier1+tier2)
./gradlew spotlessApply      # применить форматирование
./gradlew installGitHooks    # однократно после клона
```

Порт по умолчанию — `8080` (`./gradlew bootRun`).