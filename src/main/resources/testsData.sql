CREATE TABLE IF NOT EXISTS genre(
    genre_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     varchar(50)
);

CREATE TABLE IF NOT EXISTS mpa_values(
    mpa_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name   varchar(50)
);

CREATE TABLE IF NOT EXISTS users (
    user_id  int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    varchar(70) NOT NULL,
    login    varchar(50) NOT NULL,
    name     varchar(50),
    birthday date
);

CREATE TABLE IF NOT EXISTS film (
    film_id         int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name            varchar(50) NOT NULL,
    description     varchar(500),
    release_date    date,
    duration        int,
    rate            int,
    mpa_rate_id     int REFERENCES mpa_values (mpa_id)
);

CREATE TABLE IF NOT EXISTS friendship(
    user_id   int NOT NULL,
    friend_id int NOT NULL,
    status    varchar(15),
    CONSTRAINT fk_friendship_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
    CONSTRAINT fk_friendship_friend_id
        FOREIGN KEY (friend_id)
            REFERENCES users (user_id),
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS likes(
    film_id int NOT NULL,
    user_id int NOT NULL,
    CONSTRAINT fk_likes_film_id
        FOREIGN KEY (film_id)
            REFERENCES film (film_id),
    CONSTRAINT fk_likes_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (user_id),
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS film_genre(
    film_id int NOT NULL,
    genre_id int NOT NULL,
    CONSTRAINT fk_film_genre_film_id
        FOREIGN KEY (film_id)
            REFERENCES film (film_id),
    CONSTRAINT fk_film_genre_genre_id
        FOREIGN KEY (genre_id)
            REFERENCES genre (genre_id),
    PRIMARY KEY (film_id, genre_id)
    );

DELETE FROM film_genre;
DELETE FROM likes;
DELETE FROM friendship;
DELETE FROM users;
DELETE FROM film;

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;
ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1;

MERGE INTO mpa_values KEY(mpa_id)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

MERGE INTO genre KEY(genre_id)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

INSERT INTO users (email, login, name, birthday) VALUES ('user1@mail.ru', 'user1_login', 'user1', '2002-12-05');

INSERT INTO film (name, description, release_date, duration, rate, mpa_rate_id)
VALUES ('Я устала', 'Фильм поведает историю о девушке, сдавшей спринт', '2001-01-01', '100', '5', '1');