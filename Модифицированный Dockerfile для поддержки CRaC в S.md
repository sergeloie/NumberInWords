<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" class="logo" width="120"/>

### Модифицированный Dockerfile для поддержки CRaC в Spring Boot приложении

Чтобы интегрировать поддержку CRaC в ваш Dockerfile, потребуется:

- Использовать базовый образ с поддержкой CRaC.
- Добавить стадию прогрева приложения и создания снимка (checkpoint).
- Изменить финальный запуск для восстановления из checkpoint.
- Убедиться, что выставлены необходимые Linux-капабильности при запуске контейнера.


#### Обновлённый Dockerfile

```dockerfile
# 1. Сборка приложения
FROM bellsoft/liberica-openjdk-alpine:21 AS builder
WORKDIR /application
COPY . .
RUN --mount=type=cache,target=/root/.gradle  chmod +x gradlew && ./gradlew clean build -x test

# 2. Извлечение слоёв Spring Boot
FROM bellsoft/liberica-openjre-alpine:21 AS layers
WORKDIR /application
COPY --from=builder /application/build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# 3. Сборка snapshot-образа с поддержкой CRaC и созданием checkpoint
FROM bellsoft/liberica-runtime-container:jre-21-crac-cds-slim-musl AS crac-build
WORKDIR /application
RUN adduser -S spring-user
USER spring-user
COPY --from=layers /application/dependencies/ ./
COPY --from=layers /application/spring-boot-loader/ ./
COPY --from=layers /application/snapshot-dependencies/ ./
COPY --from=layers /application/application/ ./

# Запуск для прогрева и создания checkpoint. Вместо main класса используем JarLauncher.
RUN java -XX:CRaCCheckpointTo=/crac -cp . org.springframework.boot.loader.launch.JarLauncher &
RUN sleep 20 && jcmd $(pgrep -f 'org.springframework.boot.loader.launch.JarLauncher') JDK.checkpoint

# 4. Финальный restore-образ
FROM bellsoft/liberica-runtime-container:jre-21-crac-cds-slim-musl
WORKDIR /application
RUN adduser -S spring-user
USER spring-user
COPY --from=crac-build /application/ ./
COPY --from=crac-build /crac /crac

VOLUME /tmp

ENTRYPOINT ["java", "-XX:CRaCRestoreFrom=/crac", "-cp", ".", "org.springframework.boot.loader.launch.JarLauncher"]
```


### Основные изменения и рекомендации

- Используйте образ с поддержкой CRaC:
`bellsoft/liberica-runtime-container:jre-21-crac-cds-slim-musl`
- Добавьте стадию прогрева приложения с использованием опции `-XX:CRaCCheckpointTo` и выполнением checkpoint через `jcmd`.
- В финальной стадии запуск производится с опцией `-XX:CRaCRestoreFrom=/crac` для быстрого старта из снимка[^1][^5][^2].


#### Дополнительные настройки для запуска контейнера

Для корректной работы CRaC контейнер требует дополнительных Linux-прав, поэтому контейнер рекомендуется запускать с ключами:

```sh
docker run --cap-add=CHECKPOINT_RESTORE --cap-add=NET_ADMIN --cap-add=SYS_PTRACE --cap-add=SYS_ADMIN ...
```


#### Краткое пояснение по стадиям

| Стадия | Задача |
| :-- | :-- |
| builder | Сборка .jar из исходных файлов проекта |
| layers | Извлечение слоёв Spring Boot JAR |
| crac-build | Прогрев приложения и создание checkpoint |
| финальный образ | Копирование файлов и запуск с восстановлением CRaC |

**Важно:**

- Перед использованием CRaC убедитесь, что ваши нестандартные ресурсы реализуют интерфейс `org.crac.Resource`.
- Spring Boot 3.2+ уже поддерживает CRaC из коробки, дополнительной настройки чаще всего не требуется[^1][^2][^5].

**Источники**:

- Документации и официальные гайды по CRaC для Spring Boot и Docker[^1][^5][^2].

<div style="text-align: center">⁂</div>

[^1]: https://bell-sw.com/blog/how-to-use-crac-with-spring-boot-apps-in-a-docker-container/

[^2]: https://github.com/CRaC/example-spring-boot

[^3]: https://spring.io/guides/gs/spring-boot-docker

[^4]: https://github.com/sdeleuze/spring-boot-crac-demo/blob/main/Dockerfile

[^5]: https://bell-sw.com/videos/how-to-use-crac-with-spring-boot-in-a-docker-container/

[^6]: https://callistaenterprise.se/blogg/teknik/2024/07/01/SpringBoot-with-CRaC-part1-automatic-checkpoint/

[^7]: https://www.youtube.com/watch?v=MjvCBErBsaA

[^8]: https://ik.am/entries/772/en

[^9]: https://axiomjdk.ru/announcements/2024/09/16/crac/

[^10]: https://foojay.io/today/how-to-run-a-java-application-with-crac-in-a-docker-container/

