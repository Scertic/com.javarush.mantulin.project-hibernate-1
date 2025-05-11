# Проект по теме: Hibernate #1

## Описание

https://javarush.com/quests/lectures/jru.module4.lecture06

### Отход от задания: 
#### 1. PostgreSQL вместо MySQL
#### 2. Добавлен liquibase
#### 3. Бины для фабрики сессии (AppConfig)
#### 4. Докер


---

## Установка и запуск

### 1. Клонируем проект
https://github.com/Scertic/com.javarush.mantulin.project-hibernate-1.git

### 2. Сборка
```bash
mvn clean install
```
> Результат: `target/rpg-hibernate-1.0.war`

---

## Вариант A — запуск через **Docker Compose** (рекомендуется)

```bash
docker-compose up --build
```

- PostgreSQL поднимет базу `rpg_db`
- Tomcat развернёт `.war`
- Liquibase прогонит схему и `init.sql`

🔗 [http://localhost:8080](http://localhost:8080)

---

## Вариант B — запуск через **IntelliJ IDEA**

### 1. Создайте базу в PostgreSQL
```sql
CREATE DATABASE rpg_db;
```

### 🔢 2. Запуск через контекст "/"


---

## Автор
Mantulin Aleksei
