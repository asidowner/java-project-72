CREATE TABLE IF NOT EXISTS url
(
    id         serial PRIMARY KEY,
    name       VARCHAR(255)             NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
