[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=sergeloie_NumberInWords&metric=bugs)](https://sonarcloud.io/summary/new_code?id=sergeloie_NumberInWords)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=sergeloie_NumberInWords&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=sergeloie_NumberInWords)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sergeloie_NumberInWords&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sergeloie_NumberInWords)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=sergeloie_NumberInWords&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=sergeloie_NumberInWords)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sergeloie_NumberInWords&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=sergeloie_NumberInWords)

Приложение, склоняющее числительные от 0 до 999_999_999_999 по падежам и родам.  
Стек: Java 21, Spring Boot, Docker, Amplicode, SonarCloud  
Фронтенд приложения доступен по адресу http://127.0.0.1:13923
Swagger UI - по адресу http://127.0.0.1:13923/swagger-ui/index.htm

Веб-приложение склоняет числительные от 0 до 999_999_999_999 включительно, на русском языке по падежам и родам.
Для конвертирования числа в строку нужно отправить на эндпойнт /convert json такого вида  
Поле number обязательное. Если поля gender и case отсутствуют, то вместо них будут подставлены Мужской - MASCULINE и Именительный - NOMINATIVE, соответственно.
```json
{
  "number": 111987654321,
  "gender": "FEMININE",
  "case": "INSTRUMENTAL"
}
```
Падежи могут принимать следущие значения  
```
    Именительный - NOMINATIVE
    Родительный - GENITIVE
    Дательный - DATIVE
    Винительный - ACCUSATIVE
    Творительный - INSTRUMENTAL
    Предложный - PREPOSITIONAL
```
А роды
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