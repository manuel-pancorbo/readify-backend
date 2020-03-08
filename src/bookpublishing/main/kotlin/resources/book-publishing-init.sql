create schema if not exists bookpublishing;
CREATE TABLE bookpublishing.book
(
    id       VARCHAR(36)  NOT NULL,
    authorId VARCHAR(36)  NOT NULL,
    title    VARCHAR(150) NOT NULL,
    summary  TEXT,
    cover    VARCHAR(200),
    tags     VARCHAR(20)[],
    PRIMARY KEY (id)
);

CREATE TABLE bookpublishing.chapter
(
    id         VARCHAR(36)  NOT NULL,
    authorId   VARCHAR(36)  NOT NULL,
    bookId     VARCHAR(36)  NOT NULL,
    title      VARCHAR(150) NOT NULL,
    content    TEXT,
    modifiedAt timestamp with time zone,
    PRIMARY KEY (id)
);