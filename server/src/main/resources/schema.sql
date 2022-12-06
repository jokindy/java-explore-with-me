CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT UQ_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title              VARCHAR(150)                            NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    category_id        INT                                     NOT NULL,
    created            TIMESTAMP                               NOT NULL,
    event_date         TIMESTAMP                               NOT NULL,
    initiator_id       INT                                     NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INT                                     NOT NULL,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN                                 NOT NULL,
    state              VARCHAR                                 NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT EV_INITIATOR FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT EV_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id)
);

create table if not exists event_locations
(
    event_id bigint  not null
        constraint event_id_location
            references events
            on update cascade on delete cascade,
    lat      numeric not null,
    lon      numeric not null,
    CONSTRAINT LOCATION FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    requester_id INT                                     NOT NULL,
    event_id     INT                                     NOT NULL,
    created      TIMESTAMP,
    status       VARCHAR,
    CONSTRAINT pk_request PRIMARY KEY (id),
    UNIQUE (requester_id, event_id),
    CONSTRAINT REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT EVENT FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    author_id INT                                     NOT NULL,
    event_id  INT                                     NOT NULL,
    content   VARCHAR(2000)                           NOT NULL,
    created   TIMESTAMP                               NOT NULL,
    positive  boolean                                 NOT NULL,
    state     VARCHAR                                 NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT COM_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT COM_EVENT FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS comment_rating
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    comment_id BIGINT                                  NOT NULL,
    user_id    BIGINT                                  NOT NULL,
    is_like    BOOLEAN                                 NOT NULL,
    CONSTRAINT pk_comment_rate PRIMARY KEY (id),
    UNIQUE (comment_id, user_id),
    CONSTRAINT COMMENT_RATE FOREIGN KEY (comment_id) REFERENCES comments (id),
    CONSTRAINT USER_RATE FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR(255)                            NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS event_compilations
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,
    CONSTRAINT EC_COMPILATIONS FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT EC_EVENT FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS subscribers
(
    user_id       BIGINT  NOT NULL,
    subscriber_id BIGINT  NOT NULL,
    status        VARCHAR NOT NULL,
    CONSTRAINT PK_SUBSCRIBERS PRIMARY KEY (user_id, subscriber_id),
    CONSTRAINT S_USER FOREIGN KEY (subscriber_id) REFERENCES users (id),
    CONSTRAINT S_SUBSCRIBER FOREIGN KEY (user_id) REFERENCES users (id)
);