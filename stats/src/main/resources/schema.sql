CREATE TABLE IF NOT EXISTS stats (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY  NOT NULL,
    app VARCHAR(256)                            NOT NULL,
    uri TEXT                                    NOT NULL,
    ip VARCHAR(24)                              NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_stats PRIMARY KEY (id)
);