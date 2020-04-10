create schema if not exists readerlibrary;
CREATE TABLE readerlibrary.book
(
    id                   VARCHAR(36)  NOT NULL,
    authorId             VARCHAR(36)  NOT NULL,
    status               VARCHAR(15),
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

CREATE TABLE readerlibrary.chapter
(
    id            VARCHAR(36)  NOT NULL,
    authorId      VARCHAR(36)  NOT NULL,
    bookId        VARCHAR(36)  NOT NULL,
    title         VARCHAR(150) NOT NULL,
    content       TEXT,
    modifiedAt    timestamp,
    publishedAt   timestamp,
    priceamount   NUMERIC(6, 2),
    pricecurrency VARCHAR(4),
    chapterOrder  NUMERIC(2, 0),
    excerpt       TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE readerlibrary.payment
(
    id           VARCHAR(100) NOT NULL,
    readerId     VARCHAR(36)  NOT NULL,
    bookId       VARCHAR(36)  NOT NULL,
    chapterId    VARCHAR(36),
    status       VARCHAR(15),
    type         VARCHAR(15),
    amount       NUMERIC(6, 2),
    currency     VARCHAR(4),
    startedAt    timestamp,
    completedAt  timestamp,
    PRIMARY KEY (id)
);