# guardrails.md — Проверки кода (зоны, команды, формат вывода)

> Установлено скиллом `setup-guardrails`. Все таски и конфиги живут в `gradle/guardrails.gradle.kts` (script plugin, подключен одной строкой в `build.gradle.kts`). Синк версий — см. reference в самом скилле.

## Зоны и инструменты

| Zone | Tool | Report |
|------|------|--------|
| Форматирование | Spotless (Palantir 2.76.0 + ktlint) | console |
| Стиль | Checkstyle (config/checkstyle/checkstyle.xml — конфиг проекта) | `build/reports/checkstyle/*.xml` |
| Компилятор | Error Prone (ошибки: ReferenceEquality, EqualsGetClass, MissingOverride) | console (compileJava) |
| Запахи/баги | PMD (config/pmd/ruleset.xml) | `build/reports/pmd/*.xml` |
| Запахи/баги | SpotBugs + findsecbugs (config/spotbugs/exclude.xml) | `build/reports/spotbugs/*.xml` |
| Архитектура | ArchUnit 8 правил (`src/test/java/ru/anseranser/ArchitectureTest.java`) | JUnit XML |
| Покрытие | JaCoCo (gate 0.5) | `build/reports/jacoco/test/` |
| Секреты | Gitleaks (hook pre-commit + таск `gitleaksDetect`) | console |
| Уязвимости зависимостей | OSV-Scanner (таск `osvScan`, baseline `osv-scanner.toml`) | console |
| Мутации (Tier 3) | PIT | `build/reports/pitest/` |
| Качество (Tier 3) | SonarQube — у проекта свой SonarCloud (`./gradlew sonar` с конфигом из `build.gradle.kts`); guardrails его не дублирует | SonarCloud |
| Языки (EN/RU) | `languageCheck` | console |

## Команды

```bash
./gradlew tier1 --continue   # быстро перед коммитом: Spotless, Checkstyle, ErrorProne, Gitleaks, Language
./gradlew tier2 --continue   # CI: PMD, SpotBugs, ArchUnit, JaCoCo, OSV-Scanner
./gradlew tier3 --continue   # тяжёлое/ночное: PIT (+ статус Sonar)
./gradlew tiers --continue   # все сразу
./gradlew check              # стандартный check (ядро tier1+tier2)
./gradlew spotlessApply      # применить форматирование
./gradlew installGitHooks    # однократно после клона: git config core.hooksPath config/githooks
./gradlew dependencies --write-locks   # опционально: gradle.lockfile для полноценного osvScan
```

## Формат summary и семантика статусов

Каждый tier печатает `=== Tier N Summary ===` с PASS/FAIL/SKIPPED по каждому инструменту и вердикт:

- `PASSED` — все инструменты зелёные.
- `FAILED` — хотя бы один FAIL (читать отчёт соответствующего инструмента).
- `PARTIAL` — нет FAIL, но что-то SKIPPED (например, gitleaks/osv-scanner не установлены, SonarQube не запущен).

SKIPPED честно отражает причину (бинарь не найден / таск не в графе / сервер недоступен) — «молчаливого провала» нет.

## Бинарные инструменты (gitleaks, osv-scanner)

Резолв: PATH → winget fallback (Windows). Установка:

- Windows: `winget install Gitleaks.Gitleaks`, `winget install Google.OSVScanner`
- macOS: `brew install gitleaks osv-scanner`
- Linux: `apt install gitleaks` (или release c GitHub), osv-scanner: `curl -1sLf 'https://dl.cloudsmith.io/public/osv/osv-scanner/gpg...'` — см. https://google.github.io/osv-scanner/installation/

Если бинаря нет — таски SKIPPED, коммит не блокируется (но hook pre-commit при отсутствии gitleaks отказывает — это осознанно).

## OSV baseline (osv-scanner.toml)

Файл стартует ПУСТЫМ. Перед занесением игнора: `./gradlew dependencies --write-locks` → `./gradlew osvScan` → для каждого подтверждённого false-positive/accepted risk: `[[IgnoredVulns]] id = "GHSA-..." reason = "..."`.

## Языки (languageCheck)

Допустимы только английский и русский. Русский — в `.md/.txt/.json/.java/.html` (решение владельца: `.java` и `.html` добавлены в `russianAllowedExtensions` в `gradle/guardrails.gradle.kts` — тесты со словами и страница-пример на русском) плюс типографика (`— → « » – • …`, box-drawing для деревьев каталогов). Всё остальное (конфиги, скрипты, hooks) — строго ASCII. Генерируемые wrapper-скрипты `gradlew`/`gradlew.bat` исключены из проверки (`languageCheckSkipPaths` — список репо-относительных путей, при необходимости расширяется). Правило применяется автоматически в `tier1`.

## Предсуществующие проблемы

На уже существующем проекте первый прогон tiers может быть красным из-за старого кода (форматирование, стиль, уязвимости). Это ожидаемо: guardrails показывают состояние, а не чинят код. Форматирование безопасно применить: `./gradlew spotlessApply`. Остальное — отчёты и точечные фиксы по одному.