DROP TABLE IF EXISTS book_authors;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

-- 著者テーブル
CREATE TABLE authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL
);

-- 書籍テーブル
-- publish_status 0:未出版, 1:出版済
CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price INT CHECK (price >= 0),
    publish_status VARCHAR(3) NOT NULL CHECK (publish_status IN ('0', '1'))
);

-- 書籍・著者テーブル
CREATE TABLE book_authors (
    book_id INT,
    author_id INT,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE,
    PRIMARY KEY (book_id, author_id)
);

-- 著者
INSERT INTO authors (name, birth_date) VALUES ('相田太郎', '1975-05-20');
INSERT INTO authors (name, birth_date) VALUES ('川野一郎', '1980-11-12');
INSERT INTO authors (name, birth_date) VALUES ('佐藤花子', '1965-07-30');
INSERT INTO authors (name, birth_date) VALUES ('田中サトシ', '1990-04-15');
INSERT INTO authors (name, birth_date) VALUES ('中島亜美', '1983-09-05');

-- 書籍
INSERT INTO books (title, price, publish_status) VALUES ('世界の歩き方', 1500, '1');
INSERT INTO books (title, price, publish_status) VALUES ('世界の動物', 2000, '0');
INSERT INTO books (title, price, publish_status) VALUES ('世界の乗り物', 2500, '1');
INSERT INTO books (title, price, publish_status) VALUES ('世界不思議大全', 1800, '0');

-- 書籍・著者
-- 世界の歩き方 (共著)
INSERT INTO book_authors (book_id, author_id) VALUES (1, 1); -- 相田太郎
INSERT INTO book_authors (book_id, author_id) VALUES (1, 2); -- 川野一郎

-- 世界の動物
INSERT INTO book_authors (book_id, author_id) VALUES (2, 3); -- 佐藤花子

-- 世界の乗り物
INSERT INTO book_authors (book_id, author_id) VALUES (3, 4); -- 田中サトシ

-- 世界不思議大全
INSERT INTO book_authors (book_id, author_id) VALUES (4, 1); -- 相田太郎
