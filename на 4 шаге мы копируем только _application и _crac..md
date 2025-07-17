<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" class="logo" width="120"/>

# на 4 шаге мы копируем только /application и /crac. получается, что /dependencies, /spring-boot-loader и /snapshot-dependencies уже не нужны? приложение запустится без этих каталогов?

Отличный вопрос! Давайте подробно разберём, что происходит на 4 этапе и зачем нужны или не нужны перечисленные каталоги.

### Как работает Spring Boot layered JAR

Когда вы командой

```
java -Djarmode=layertools -jar app.jar extract
```

извлекаете свой Spring Boot fat jar, он разбивается на слои:

- **/dependencies/** — сторонние зависимости (библиотеки)
- **/spring-boot-loader/** — классы загрузчика Spring Boot (JarLauncher и др.)
- **/snapshot-dependencies/** — временные зависимости (чаще всего библиотеки из snapshot-репозиториев)
- **/application/** — ваши собственные классы приложения


### Какие папки нужны при запуске Spring Boot JarLauncher

Spring Boot "слоистый запуск" предполагает, что **JarLauncher** ищет всё необходимое на пути классов (classpath), и в обычном варианте используются ВСЕ эти слои, например:

```
java -cp . org.springframework.boot.loader.launch.JarLauncher
```

Classpath здесь — содержимое текущей директории (.), а внутри неё — всё, что необходимо (все слои).

**Если удалить папки /dependencies, /spring-boot-loader, /snapshot-dependencies, то приложение НЕ стартует!**

- JarLauncher сам находится внутри **/spring-boot-loader/**
- В /dependencies и /snapshot-dependencies лежат все внешние библиотеки, без них приложение не найдёт нужных классов
- В /application находятся только ваши классы, но они обычно не покрывают все требуемые классы для старта.


### Почему в финальном контейнере копируются все эти папки?

**Это обязательный шаг!**
В финальном образе должны быть все слои JAR:

- /dependencies/
- /spring-boot-loader/
- /snapshot-dependencies/
- /application/

**/crac/** — служебная папка с checkpoint'ом (снятым снимком состояния RSS/JVM).

### Итоговый вывод

- **Нужно копировать ВСЕ слои (/dependencies, /spring-boot-loader, /snapshot-dependencies, /application)** из предыдущего этапа в финальный контейнер.
- Если вы копируете только /application и /crac — приложение не запустится, т.к. не будет найдено большинство зависимостей и классов загрузчика.


#### Пример правильного копирования (как у вас было в предыдущем образце):

```dockerfile
COPY --from=crac-build /application/dependencies/ ./
COPY --from=crac-build /application/spring-boot-loader/ ./
COPY --from=crac-build /application/snapshot-dependencies/ ./
COPY --from=crac-build /application/application/ ./
COPY --from=crac-build /crac /crac
```

**/application** — это директория, куда были разложены все слои. Если вы делаете копирование

```
COPY --from=crac-build /application/ ./
```

тогда копируются всё содержимое — и это тоже рабочий вариант! Просто убедитесь, что внутри /application в crac-build есть все нужные папки.

**Резюме:**
/ dependencies, /spring-boot-loader и /snapshot-dependencies обязательно нужны в конечном образе!
Приложение не запустится только с /application и /crac.

