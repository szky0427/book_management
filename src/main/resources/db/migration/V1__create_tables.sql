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