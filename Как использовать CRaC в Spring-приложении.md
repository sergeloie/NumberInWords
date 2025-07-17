<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" class="logo" width="120"/>

## Как использовать CRaC в Spring-приложении

CRaC (Coordinated Restore at Checkpoint) — это технология, дающая возможность запускать прогретые Java-приложения за миллисекунды с помощью сериализации состояния JVM и последующего восстановления. Для Spring Boot с поддержкой CRaC потребуется выполнить несколько шагов для максимального эффекта.

### 1. Требования

- **JDK с поддержкой CRaC**: Например, BellSoft Liberica JDK или Azul Zulu JDK с CRaC[^1][^2].
- **Spring Boot 3.2+** (и Spring Framework 6.1+): Эти версии имеют нативную интеграцию с CRaC[^1][^2].
- **Linux с поддержкой CRIU** (обычно в контейнере).


### 2. Подготовка приложения

- **Добавить зависимость на CRaC**:
В `pom.xml` добавить:

```xml
<dependency>
  <groupId>org.crac</groupId>
  <artifactId>crac</artifactId>
  <version>1.4.0</version>
</dependency>
```

В Gradle аналогичная зависимость.
- **Контролировать жизненный цикл ресурсов**:
Spring Boot автоматически поддерживает корректное закрытие и повторное открытие большинства ресурсов. Однако если в вашем коде используются нестандартные соединения (сокеты, ручное открытие файлов, нестандартные thread pool и др.), используйте интерфейс `org.crac.Resource` и реализуйте методы `beforeCheckpoint()` и `afterRestore()` для корректного закрытия и восстановления состояния своих компонентов[^2][^3].


### 3. Dockerfile и контейнеризация

```dockerfile
FROM bellsoft/liberica-runtime-container:jre-21-crac-cds-slim-musl

COPY app.jar /app/app.jar

# Первый запуск — для прогрева и создания снимка
RUN java -XX:CRaCCheckpointTo=/crac -jar /app/app.jar &
RUN jcmd <pid> JDK.checkpoint

# Второй контейнер уже стартует с восстановлением
ENTRYPOINT ["java", "-XX:CRaCRestoreFrom=/crac", "-jar", "/app/app.jar"]
```

- Параметры:
    - `-XX:CRaCCheckpointTo=/crac` — директория для хранения checkpoint.
    - `-XX:CRaCRestoreFrom=/crac` — восстановление из checkpoint.
- Запускайте контейнер с расширенными привилегиями:
`--cap-add=CHECKPOINT_RESTORE --cap-add=NET_ADMIN --cap-add=SYS_PTRACE --cap-add=SYS_ADMIN`[^2][^4].
- Убедитесь, что используемый образ поддерживает CRaC (например, `bellsoft/liberica-runtime-container:jre-21-crac...`).


### 4. Сценарий работы

1. Собрать прогретое приложение (при необходимости — с демо нагрузкой или автотестами).
2. Во время работы приложения выполнить команду чекпоинта (`jcmd <pid> JDK.checkpoint`).
3. Остановить контейнер, сохранить данные чекпоинта.
4. Для последующих запусков запускать приложение в режиме восстановления из чекпоинта — стартап занимает миллисекунды.

### 5. Для каких приложений CRaC действительно полезен

**Оптимально подходит для:**

- Serverless, FaaS, event-driven сервисов, где критичен быстрый холодный старт[^5].
- Микросервисов и приложений, часто масштабируемых в облаке.
- Приложений, требующих “разогрева” перед работой: большие Spring Boot проекты, сервисы с большим количеством зависимостей, автоконфигураций, сторонних библиотек.
- Инстансов, запускаемых по требованию (динамическое масштабирование)[^5][^6].

**Менее актуально:**

- Для сервисов с редко перезапускаемыми “долгоиграющими” инстансами.
- Для простых приложений, почти мгновенно запускающихся без CRaC.

**Пример основных шагов настройки в таблице:**


| Шаг | Описание |
| :-- | :-- |
| 1 | Добавить зависимость `org.crac:crac` в проект |
| 2 | Контролировать нетиповые ресурсы через интерфейс `Resource` |
| 3 | Использовать JDK/образ с поддержкой CRaC |
| 4 | В Dockerfile добавить запуск с checkpoint (и restore) |
| 5 | Проверить — старт приложения после restore << 1 сек |

### Полезные ссылки

- Официальная документация Spring Checkpoint/Restore[^1]
- Гайд Bellsoft о CRaC + Spring Boot + Docker[^2]

CRaC позволяет получать практически “мгновенный” старт Java-приложения за счёт snapshot-восстановления, но требует тщательной подготовки ресурсов и тестирования реального сценария запуска[^2][^3][^5].

<div style="text-align: center">⁂</div>

[^1]: https://docs.spring.io/spring-boot/reference/packaging/checkpoint-restore.html

[^2]: https://bell-sw.com/blog/how-to-use-crac-with-java-applications/

[^3]: https://docs.spring.io/spring-framework/reference/integration/checkpoint-restore.html

[^4]: https://ik.am/entries/772/en

[^5]: https://bell-sw.com/blog/what-is-crac-a-guide-to-cutting-java-startup-and-warmup-from-minutes-to-milliseconds/

[^6]: https://www.alibabacloud.com/help/doc-detail/2854327.html

[^7]: https://axiomjdk.ru/announcements/2024/09/16/crac/

[^8]: https://callistaenterprise.se/blogg/teknik/2024/07/01/SpringBoot-with-CRaC-part1-automatic-checkpoint/

[^9]: https://bell-sw.com/videos/how-to-use-crac-with-spring-boot-in-a-docker-container/

[^10]: https://docs.azul.com/core/release/21-ga/crac/crac-introduction

[^11]: https://openliberty.io/docs/latest/reference/feature/crac-1.4.html

[^12]: https://bell-sw.com/blog/how-to-use-crac-with-spring-boot-apps-in-a-docker-container/

[^13]: https://docs.azul.com/core/crac/crac-frameworks

[^14]: https://www.baeldung.com/openjdk-crac-hot-application-instances-scaling

[^15]: https://gist.github.com/making/35cfa52862e93793bad2b37b7c0e5135

[^16]: https://github.com/CRaC/docs

[^17]: https://docs.azul.com/core/crac/crac-guidelines

[^18]: https://foojay.io/today/how-to-run-a-java-application-with-crac-in-a-docker-container/

[^19]: https://docs.azul.com/core/crac/crac-introduction

[^20]: https://www.youtube.com/watch?v=MjvCBErBsaA

