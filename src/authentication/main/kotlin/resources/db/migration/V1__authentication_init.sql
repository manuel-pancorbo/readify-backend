create schema if not exists authentication;
CREATE TABLE authentication.usercredential
(
    id       VARCHAR(36)  NOT NULL,
    username VARCHAR(36)  NOT NULL,
    email    VARCHAR(150) NOT NULL,
    password VARCHAR(150) NOT NULL,
    PRIMARY KEY (id)
);