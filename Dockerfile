# FROM bellsoft/liberica-openjdk-alpine:21 AS builder
# WORKDIR /application
# COPY . .
# RUN --mount=type=cache,target=/root/.gradle  chmod +x gradlew && ./gradlew clean build -x test

# FROM bellsoft/liberica-openjre-alpine:21 AS layers
# WORKDIR /application
# COPY --from=builder /application/build/libs/*.jar app.jar
# RUN java -Djarmode=layertools -jar app.jar extract

# FROM bellsoft/liberica-openjre-alpine:21
# VOLUME /tmp
# RUN adduser -S spring-user
# USER spring-user
# COPY --from=layers /application/dependencies/ ./
# COPY --from=layers /application/spring-boot-loader/ ./
# COPY --from=layers /application/snapshot-dependencies/ ./
# COPY --from=layers /application/application/ ./

# ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

# https://www.perplexity.ai/search/rasskazhi-podrobno-pro-doker-k-YWIFBHlBRxKJCSiSrvJWqA

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

