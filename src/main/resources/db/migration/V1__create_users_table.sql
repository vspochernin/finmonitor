CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL
);

CREATE INDEX idx_users_username ON users (username);