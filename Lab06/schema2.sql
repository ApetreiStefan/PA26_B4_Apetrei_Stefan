CREATE TABLE movie_list (
                            id         SERIAL PRIMARY KEY,
                            name       TEXT        NOT NULL,
                            created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE movie_list_entry (
                                  list_id  INTEGER REFERENCES movie_list(id) ON DELETE CASCADE,
                                  movie_id INTEGER REFERENCES movies(id)     ON DELETE CASCADE,
                                  PRIMARY KEY (list_id, movie_id)
);
