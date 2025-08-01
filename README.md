# 📚 Библиотека - Spring/JPA веб-приложение


Учебный проект, разработанный в рамках курса по Spring и JPA. Реализует систему учета книг в библиотеке с возможностью:
- Назначения книг читателям
- Автоматического контроля сроков возврата
- Поиска и сортировки книг

## 🎯 Цель проекта

- Продемонстрировать навыки работы с Spring Framework
- Показать понимание принципов ORM (Hibernate)
- Реализовать комплексное веб-приложение "от и до"

## 🛠 Технический стек

| Категория       | Технологии                          |
|-----------------|-------------------------------------|
| **Backend**     | Spring MVC, Spring Data JPA, Hibernate |
| **База данных** | PostgreSQL (автогенерация схемы через `hbm2ddl`) |
| **Frontend**    | Thymeleaf, HTML5, CSS3              |
| **Инструменты** | Maven, Git, IntelliJ IDEA           |

## 🚀 Как запустить

### Предварительные требования
- Установите [Java 17+](https://adoptium.net/)
- Установите [PostgreSQL 14+](https://www.postgresql.org/download/)
- [Maven 3.8+](https://maven.apache.org/)

1.Клонируйте репозиторий:
git clone https://github.com/Nelobbick/SpringCousre

2.Настройте подключение к БД в файле 
src/main/resources/hibernate.properties

3.Создайте базу данных при помощи файла 
src/main/resources/sql/sql

4.Запустите проект

Приложение будет доступно по адресу: http://localhost:8080

📌 Что я освоил в этом проекте
Настройка Spring-приложения без Spring Boot

Работа с JPA/Hibernate и аннотациями

Реализация SQL-запросов через JPA

Оптимизация производительности запросов

Отладка проблем N+1 в Hibernate
