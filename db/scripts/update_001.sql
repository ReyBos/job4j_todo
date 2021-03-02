CREATE TABLE item (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    created TIMESTAMP DEFAULT now(),
    done BOOLEAN DEFAULT false
);