CREATE TABLE users(
    id uuid not null primary key,
    username varchar not null unique,
    password varchar not null
)