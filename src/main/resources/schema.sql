-- this file executes when the application is starting because of following line in application.properties
-- spring.datasource.initialization-mode=always

DROP TABLE IF EXISTS todo;

CREATE TABLE todo
(
id varchar(63) not null primary key,
description varchar(255) not null,
created timestamp,
modified timestamp,
completed boolean
);