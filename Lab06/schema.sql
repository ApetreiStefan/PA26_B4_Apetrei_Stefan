DROP TABLE IF EXISTS movie_actor CASCADE;
DROP TABLE IF EXISTS movies      CASCADE;
DROP TABLE IF EXISTS genres      CASCADE;
DROP TABLE IF EXISTS actors      CASCADE;
DROP VIEW  IF EXISTS movies_report;

CREATE TABLE genres (
                        id   SERIAL PRIMARY KEY,
                        name TEXT   NOT NULL UNIQUE
);

CREATE TABLE actors (
                        id         SERIAL PRIMARY KEY,
                        first_name TEXT NOT NULL,
                        last_name  TEXT NOT NULL
);

CREATE TABLE movies (
                        id           SERIAL PRIMARY KEY,
                        title        TEXT    NOT NULL,
                        release_date DATE,
                        duration     INTEGER,
                        score        REAL,
                        genre_id     INTEGER REFERENCES genres(id)
);

CREATE TABLE movie_actor (
                             movie_id INTEGER REFERENCES movies(id)  ON DELETE CASCADE,
                             actor_id INTEGER REFERENCES actors(id)  ON DELETE CASCADE,
                             PRIMARY KEY (movie_id, actor_id)
);

CREATE VIEW movies_report AS
SELECT
    m.id,
    m.title,
    m.release_date,
    m.duration,
    m.score,
    g.name                                              AS genre,
    STRING_AGG(a.first_name || ' ' || a.last_name, ', '
        ORDER BY a.last_name)                    AS actors
FROM movies m
         LEFT JOIN genres     g  ON g.id = m.genre_id
         LEFT JOIN movie_actor ma ON ma.movie_id = m.id
         LEFT JOIN actors     a  ON a.id  = ma.actor_id
GROUP BY m.id, m.title, m.release_date, m.duration, m.score, g.name
ORDER BY m.title;

INSERT INTO genres (name) VALUES ('Action'), ('Drama'), ('Comedy'), ('Sci-Fi');

INSERT INTO actors (first_name, last_name) VALUES
                                               ('Keanu',    'Reeves'),
                                               ('Carrie-Anne', 'Moss'),
                                               ('Tom',      'Hanks'),
                                               ('Meryl',    'Streep');

INSERT INTO movies (title, release_date, duration, score, genre_id) VALUES
                                                                        ('The Matrix',            '1999-03-31', 136, 8.7, (SELECT id FROM genres WHERE name = 'Sci-Fi')),
                                                                        ('Cast Away',             '2000-12-22', 143, 7.8, (SELECT id FROM genres WHERE name = 'Drama')),
                                                                        ('The Devil Wears Prada', '2006-06-30', 109, 6.9, (SELECT id FROM genres WHERE name = 'Comedy'));

INSERT INTO movie_actor (movie_id, actor_id) VALUES
                                                 ((SELECT id FROM movies WHERE title = 'The Matrix'),            (SELECT id FROM actors WHERE last_name = 'Reeves')),
                                                 ((SELECT id FROM movies WHERE title = 'The Matrix'),            (SELECT id FROM actors WHERE last_name = 'Moss')),
                                                 ((SELECT id FROM movies WHERE title = 'Cast Away'),             (SELECT id FROM actors WHERE last_name = 'Hanks')),
                                                 ((SELECT id FROM movies WHERE title = 'The Devil Wears Prada'), (SELECT id FROM actors WHERE last_name = 'Streep'));