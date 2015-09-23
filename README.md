# Веб-приложение webfractal

Данное веб-приложение позволяет создавать фракталы на основе множества Жюлиа и сохранять их в базе данных.
Приложение реализованно на языке Java с использованием фреймворка Spring, БД Oracle, Hibernate ORM и фреймворка PrimeFaces.

Видео-запись работы приложения - https://drive.google.com/file/d/0ByQ6ese71ITjNGl6ZEZjRjc3eW8/view?usp=sharing

Для авторизации пользователей используется Springframework Security
Для работы с БД созданы 2 ДАО - UserDAO и FractalDAO, которым в БД соответсвуют
2 таблицы appuser и appfractal, связанные по id пользователя(см. database_tables.sql).
