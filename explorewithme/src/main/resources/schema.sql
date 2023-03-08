CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(150) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    category_id BIGINT NOT NULL,
    initiator_id BIGINT NOT NULL,
    participant_limit INT NOT NULL,
    confirmed_requests INT NOT NULL,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL,
    paid BOOLEAN NOT NULL,
    request_moderation BOOLEAN NOT NULL,
    state INT NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT FK_EVENTS_ON_CAT FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT FK_EVENTS_ON_INITIATOR FOREIGN KEY (initiator_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(150) NOT NULL NOT NULL,
    pinned BOOLEAN NOT NULL NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS participation_requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    state INT NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_participation_requests PRIMARY KEY (id),
    CONSTRAINT FK_PARTICIPATION_REQUESTS_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT FK_PARTICIPATION_REQUESTS_ON_REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT UQ_PARTICIPANT_PER_EVENT UNIQUE (requester_id, event_id)
);

CREATE TABLE IF NOT EXISTS event_user (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    event_id BIGINT REFERENCES events(id) NOT NULL,
    requester_id BIGINT REFERENCES users(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS event_compilation (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT pk_compilation_of_events PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_comofeve_on_compilation FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT fk_comofeve_on_event FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(2000) NOT NULL,
    author_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    state INT NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_on TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comment_event FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users (id)
);
