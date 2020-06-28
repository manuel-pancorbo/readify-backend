create schema if not exists userprofiles;
CREATE TABLE userprofiles.userprofile
(
    id       VARCHAR(36)  NOT NULL,
    username VARCHAR(36)  NOT NULL,
    email    VARCHAR(150) NOT NULL,
    fullname VARCHAR(100),
    image    VARCHAR(200),
    website  VARCHAR(200),
    PRIMARY KEY (id)
);