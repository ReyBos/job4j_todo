CREATE TABLE item (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    created TIMESTAMP,
    is_done BOOLEAN
);