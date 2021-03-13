CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

insert into category(name) VALUES ('срочно'), ('на перспективу');