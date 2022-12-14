CREATE TABLE IF NOT EXISTS USERS
(
    ID    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    NAME  VARCHAR(120)                            NOT NULL,
    EMAIL VARCHAR(254)                            NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY (ID),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (EMAIL)
);

CREATE TABLE IF NOT EXISTS CATEGORIES
(
    ID   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    NAME VARCHAR(120)                            NOT NULL,
    CONSTRAINT PK_CATEGORIES PRIMARY KEY (ID),
    CONSTRAINT UQ_CATEGORIES_NAME UNIQUE (NAME)
);

CREATE TABLE IF NOT EXISTS EVENTS
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY  NOT NULL,
    TITLE VARCHAR(120)                          NOT NULL,
    ANNOTATION VARCHAR(2000)                     NOT NULL,
    DESCRIPTION VARCHAR(7000)                   NOT NULL,
    CATEGORY_ID BIGINT,
    CREATED_ON TIMESTAMP,
    EVENT_DATE TIMESTAMP                        NOT NULL,
    INITIATOR_ID BIGINT                         NOT NULL,
    PAID BOOLEAN,
    PARTICIPANT_LIMIT BIGINT DEFAULT 0          NOT NULL,
    PUBLISHED_ON TIMESTAMP,
    REQUEST_MODERATION BOOLEAN,
    STATE VARCHAR(12)                           NOT NULL,
    LOCATION_LAT FLOAT,
    LOCATION_LON FLOAT,
    CONSTRAINT PK_EVENT PRIMARY KEY (ID),
    CONSTRAINT FK_EVENT_ON_CATEGORY FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORIES (ID),
    CONSTRAINT FK_EVENT_ON_USER FOREIGN KEY (INITIATOR_ID) REFERENCES USERS (ID)
 );

CREATE TABLE IF NOT EXISTS REQUESTS
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    EVENT_ID     BIGINT                        NOT NULL,
    REQUESTER_ID BIGINT                        NOT NULL,
    STATUS       VARCHAR(12)                   NOT NULL,
    CREATED      TIMESTAMP                     NOT NULL,
    CONSTRAINT PK_REQUEST PRIMARY KEY (ID),
    UNIQUE (REQUESTER_ID, EVENT_ID),
    CONSTRAINT FK_REQUESTER FOREIGN KEY (REQUESTER_ID) REFERENCES USERS (ID),
    CONSTRAINT FK_EVENTS_REQUESTS FOREIGN KEY (EVENT_ID) REFERENCES EVENTS (ID)
);

CREATE TABLE IF NOT EXISTS COMPILATIONS
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    TITLE  VARCHAR(120)                        NOT NULL,
    PINNED BOOLEAN,
    CONSTRAINT PK_COMPILATIONS PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS EVENTS_COMPILATION
(
    EVENT_ID       BIGINT                               NOT NULL,
    COMPILATION_ID BIGINT                               NOT NULL,
    CONSTRAINT FK_COMPILATION_EVENTS_ON_COMPILATION FOREIGN KEY (COMPILATION_ID) REFERENCES COMPILATIONS (ID),
    CONSTRAINT FK_COMPILATION_EVENTS FOREIGN KEY (EVENT_ID) REFERENCES EVENTS (ID)
);

CREATE TABLE IF NOT EXISTS COMMENTS (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY          NOT NULL,
    TEXT VARCHAR(7000)                                   NOT NULL,
    EVENT_ID BIGINT                                             ,
    AUTHOR_ID BIGINT                                            ,
    CREATED TIMESTAMP WITHOUT TIME ZONE                 NOT NULL,
    EDITED BOOLEAN,
    CONSTRAINT PK_COMMENTS PRIMARY KEY (ID),
    CONSTRAINT FK_COMMENTS_ON_AUTHOR FOREIGN KEY (AUTHOR_ID) REFERENCES USERS (ID),
    CONSTRAINT FK_COMMENTS_ON_ITEM FOREIGN KEY (EVENT_ID) REFERENCES EVENTS (ID)
);