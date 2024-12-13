-- Удаление схемы, если она существует (со всеми таблицами внутри)
DROP SCHEMA IF EXISTS book_schema CASCADE;

-- Создание новой схемы для таблицы books
CREATE SCHEMA IF NOT EXISTS book_schema;

-- Удаление таблицы, если она существует (на случай, если схема осталась)
DROP TABLE IF EXISTS book_schema.books;

DROP TABLE if exists book_schema.users;

DROP TABLE IF EXISTS book_schema.users_roles;

-- Удаляем таблицу, если она существует
DROP TABLE IF EXISTS book_schema.book_tracker;

-- Создание таблицы books в схеме book_schema
CREATE TABLE book_schema.books
(
    id               BIGSERIAL PRIMARY KEY,  -- Уникальный идентификатор
    book_isbn        VARCHAR(50)   NOT NULL, -- ISBN книги
    book_title       VARCHAR(256)  NOT NULL, -- Название книги
    book_genre       VARCHAR(256)  NOT NULL, -- Жанр книги
    book_description VARCHAR(1024) NOT NULL, -- Описание книги
    book_author      VARCHAR(100)  NOT NULL,  -- Автор книги
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
);

-- Добавление уникального ограничения на ISBN
ALTER TABLE book_schema.books
    ADD CONSTRAINT unique_book_isbn UNIQUE (book_isbn);


-- Создание таблицы users
CREATE TABLE book_schema.users
(
    id       SERIAL PRIMARY KEY,                -- Уникальный идентификатор
    username    VARCHAR(255) NOT NULL UNIQUE,      -- Уникальный логин пользователя
    password VARCHAR(255) NOT NULL,             -- Хэшированный пароль пользователя
    enabled  BOOLEAN      NOT NULL DEFAULT TRUE -- Состояние активности пользователя
);


-- Создание таблицы users_roles для хранения ролей пользователей
CREATE TABLE book_schema.users_roles
(
    user_id INT         NOT NULL,                                   -- Идентификатор пользователя
    role    VARCHAR(10) NOT NULL,                                   -- Роль пользователя, ограниченная ENUM
    PRIMARY KEY (user_id, role),                                    -- Составной ключ для предотвращения дублирования
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES book_schema.users (id) ON DELETE CASCADE,
    CONSTRAINT chk_role CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN')) -- Ограничение на допустимые значения
);


-- Создание таблицы book_tracker
CREATE TABLE IF NOT EXISTS book_schema.book_tracker
(
    id
               BIGSERIAL
        PRIMARY
            KEY,
    book_id
               BIGINT
                            NOT
                                NULL,
    status
               VARCHAR(255) NOT NULL,
    taken      DATE,
    returned   DATE,
    is_deleted BOOLEAN      NOT NULL DEFAULT FALSE
);

-- Создание индекса на поле book_id
CREATE INDEX IF NOT EXISTS idx_book_id ON book_schema.book_tracker (book_id);

-- Создание индекса на поле status
CREATE INDEX IF NOT EXISTS idx_status ON book_schema.book_tracker (status);
