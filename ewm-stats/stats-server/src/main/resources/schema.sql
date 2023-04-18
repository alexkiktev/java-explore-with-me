DROP TABLE IF EXISTS hits;

CREATE TABLE IF NOT EXISTS hits
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    app VARCHAR(50) NOT NULL,
    uri VARCHAR(50) NOT NULL,
    ip VARCHAR(15) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_hits PRIMARY KEY (id)
);