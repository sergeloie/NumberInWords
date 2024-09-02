[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sergeloie_NumberInWords&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sergeloie_NumberInWords)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=sergeloie_NumberInWords&metric=bugs)](https://sonarcloud.io/summary/new_code?id=sergeloie_NumberInWords)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=sergeloie_NumberInWords&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=sergeloie_NumberInWords)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sergeloie_NumberInWords&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sergeloie_NumberInWords)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=sergeloie_NumberInWords&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=sergeloie_NumberInWords)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sergeloie_NumberInWords&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=sergeloie_NumberInWords)

Приложение, склоняющее числительные от 0 до 999_999_999_999 по падежам и родам.  
Стек: Java 21, Spring Boot, Docker, Amplicode, SonarCloud  

Демо доступно по адресу https://number-in-words-rus-v1.onrender.com  
(Бесплатный хостинг, поэтому может загружаться около минуты)

Приложение доступно как Docker образ, для скачивания образа выполните
```shell
docker pull anseranser/number-in-words-rus:v1
```
и для запуска
```shell
docker run -d -p 13923:13923 --name=niw-rus-v1 anseranser/number-in-words-rus:v1
```
и для последующих запусков
```shell
docker start niw-rus-v1
```

Фронтенд приложения будет доступен по адресу http://127.0.0.1:13923  
Swagger UI - по адресу http://127.0.0.1:13923/swagger-ui/index.html

Веб-приложение склоняет числительные от 0 до 999_999_999_999 включительно, на русском языке по падежам и родам.
Для конвертирования числа в строку нужно отправить на эндпойнт /convert POST запрос с json такого вида:  

```json
{
  "number": 111987654321,
  "gender": "FEMININE",
  "case": "INSTRUMENTAL"
}
```
Поле number обязательное. Если поля gender и case отсутствуют, то вместо них будут подставлены Мужской - MASCULINE и Именительный - NOMINATIVE, соответственно.  

Падежи могут принимать следующие значения:  
```
    Именительный - NOMINATIVE
    Родительный - GENITIVE
    Дательный - DATIVE
    Винительный - ACCUSATIVE
    Творительный - INSTRUMENTAL
    Предложный - PREPOSITIONAL
```
А роды:  
```
    Мужской - MASCULINE
    Женский - FEMININE
    Средний - NEUTER
```
В ответ приходит аналогичный json дополнительно содержащий строку numberInWords с числом прописью
```json
{
  "number": 111987654321,
  "gender": "FEMININE",
  "numberInWords": "ста одиннадцатью миллиардами девятьюстами восьмьюдесятью семью миллионами шестьюстами пятьюдесятью четырьмя тысячами тремястами двадцатью одной",
  "case": "INSTRUMENTAL"
}
```
