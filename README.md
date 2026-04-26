# KickSharing (Hibernate)

Консольное приложение для управления поездками и платежами сервиса шеринга с использованием **Hibernate ORM**.

## Требования

- Java 17+
- PostgreSQL 14+
- Hibernate 7.3+
- Gradle

## Установка и запуск

1. **Создайте базу данных PostgreSQL:**

   ```sql
   CREATE DATABASE KickSharing;
   ```
   
2. **Выполните скрипт создания таблиц:**
    - В pgAdmin: откройте файл `src/main/resources/db/schema.sql` и выполните (F5)

3. **Настройте подключение к БД:**
    - Скопируйте `hibernate.cfg.xml.sample` в `hibernate.cfg.xml`
    - Укажите свой пароль в `hibernate.cfg.xml`

4. **Запустите приложение:**
   **Через IDEA:**
    - Откройте проект в IntelliJ IDEA
    - Убедитесь, что зависимости Gradle подгрузились
    - Запустите `ru.shalaginov.appLogic.Main.java`
 
## Функции

- CRUD для каждой сущности (User, Ride, Payment)
- Просмотр статуса бонусной карты (бронзовая/серебряная/золотая)
- Каскадное удаление (при удалении пользователя удаляются все его поездки и платежи)

## Логи

Логи пишутся в папку `logs/app.log`