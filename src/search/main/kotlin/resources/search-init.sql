create schema if not exists search;
CREATE TABLE search.author
(
    id       VARCHAR(36)  NOT NULL,
    username VARCHAR(36)  NOT NULL,
    fullname VARCHAR(100),
    PRIMARY KEY (id)
);