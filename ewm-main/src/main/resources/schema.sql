DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS compilations_events;
DROP TABLE IF EXISTS compilations;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(30) NOT NULL,
    email VARCHAR(30) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    lat DECIMAL(9, 6) NOT NULL,
    lon DECIMAL(9, 6) NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000),
    category_id BIGINT NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    state VARCHAR(9),
    initiator_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    paid BOOLEAN,
    request_moderation BOOLEAN,
    views BIGINT,
    participant_limit INTEGER,
    confirmed_requests BIGINT,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_events_category_id FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_events_initiator_id FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT fk_events_location_id FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    title VARCHAR(100) NOT NULL,
    pinned BOOLEAN NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id),
    CONSTRAINT uq_compilation_name UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    event_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT fk_compilations_events_event_id FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_compilations_events_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR(10) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_user_id FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT fk_requests_event_id FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    text VARCHAR(1000) NOT NULL,
    event_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    status VARCHAR(10) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comments_author_id FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT fk_comments_event_id FOREIGN KEY (event_id) REFERENCES events (id)
)