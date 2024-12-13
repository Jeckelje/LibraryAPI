-- Вставка данных в таблицу books
INSERT INTO book_schema.books (book_isbn, book_title, book_genre, book_description, book_author, is_deleted)
VALUES
    ('978-3-16-148410-0', 'The Great Gatsby', 'Classic', 'A story about the American dream.', 'F. Scott Fitzgerald', false),
    ('978-0-14-118280-3', '1984', 'Dystopian', 'A novel about a totalitarian regime.', 'George Orwell', false),
    ('978-0-452-28423-4', 'To Kill a Mockingbird', 'Drama', 'A novel about racial injustice.', 'Harper Lee', false);

-- Вставка данных в таблицу users
INSERT INTO book_schema.users (username, password, enabled)
VALUES
    ('admin', '$2a$10$jViUxQCf7LDv66fChi1tNeuWeiJxxqseRhH3w7Qx5uLwJocfIA4Qe', true), -- Пароль: admin (захэширован через BCrypt)
    ('user', '$2a$10$3SnbLBSJQgyW87Hua1BjpO7JaMG1O9F3u04aQnJS3kxdr34M62mNS', true);

-- Вставка данных в таблицу users_roles
INSERT INTO book_schema.users_roles (user_id, role)
VALUES
    (1, 'ROLE_ADMIN'),
    (1, 'ROLE_USER'),
    (2, 'ROLE_USER');

-- Вставка данных в таблицу book_tracker
INSERT INTO book_schema.book_tracker (book_id, status, taken, returned, is_deleted)
VALUES
    (1, 'taken', '2024-01-01', NULL, false),
    (2, 'free', NULL, NULL, false),
    (3, 'free', '2024-02-01', '2024-02-15', false);

-- Проверка вставленных данных
SELECT * FROM book_schema.books;
SELECT * FROM book_schema.users;
SELECT * FROM book_schema.users_roles;
SELECT * FROM book_schema.book_tracker;
