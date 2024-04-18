# Подготовка базы данных.

В файлах `src/main/resources/application.properties` и `src/test/resources/application.properties` необходимо указать нужные данные базы данных, а именно имя пользователя, пароль к нему и правильный URL базы данных, с указанием именно баз `workers` и `workers-test`. Далее нужно создать тестовую и основную базы данных, используя готовые скрипты:

```bash
psql -f src/main/resources/sql/Database.sql -h localhost -U postgres
psql -f src/main/resources/sql/DatabaseTest.sql -h localhost -U postgres
```

# Сборка проекта и тестирование.

```bash
gradle build
```

# Запуск веб-сайта.

В первом терминале запускаем веб-сайт:

```bash
gradle bootRun
```

Если команда выше не работает, то можно попробовать следующую команду:

```bash
SPRING_PROFILES_ACTIVE=test gradle clean bootRun
```

По ссылке `http://localhost:8080` откроется веб-сайт.

Если нужно запустить Selenium-тесты, то нужно во втором терминале запустить:

```bash
gradle test
```
