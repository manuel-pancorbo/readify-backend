create schema if not exists bookpublishing;
CREATE TABLE bookpublishing.book
(
    id                   VARCHAR(36)  NOT NULL,
    authorId             VARCHAR(36)  NOT NULL,
    status               VARCHAR(15),
    visibility           VARCHAR(15),
    title                VARCHAR(150) NOT NULL,
    summary              TEXT,
    cover                VARCHAR(200),
    tags                 VARCHAR(20)[],
    priceamount          NUMERIC(6, 2),
    pricecurrency        VARCHAR(4),
    finishedAt           timestamp with time zone,
    completionpercentage NUMERIC(3, 0),
    PRIMARY KEY (id)
);

CREATE TABLE bookpublishing.chapter
(
    id            VARCHAR(36)  NOT NULL,
    authorId      VARCHAR(36)  NOT NULL,
    bookId        VARCHAR(36)  NOT NULL,
    title         VARCHAR(150) NOT NULL,
    content       TEXT,
    modifiedAt    timestamp,
    publishedAt   timestamp,
    status        VARCHAR(10),
    priceamount   NUMERIC(6, 2),
    pricecurrency VARCHAR(4),
    PRIMARY KEY (id)
);