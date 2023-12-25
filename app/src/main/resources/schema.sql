DROP TABLE IF EXISTS url_checks;
DROP TABLE IF EXISTS url;

CREATE TABLE url
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255)             NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE url_checks
(
    id          SERIAL PRIMARY KEY,
    url_id      SERIAL REFERENCES url (id) ON DELETE CASCADE,
    status_code INT                      NOT NULL,
    h1          VARCHAR(MAX)             NOT NULL,
    title       VARCHAR(MAX)             NOT NULL,
    description VARCHAR(MAX)             NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL
);
