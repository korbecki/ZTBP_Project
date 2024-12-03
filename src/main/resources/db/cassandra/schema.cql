CREATE TABLE IF NOT EXISTS user
(
    name      VARCHAR,
    surname   VARCHAR,
    user_name VARCHAR,
    password  VARCHAR,
    email     VARCHAR,
    PRIMARY KEY (email)
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    refresh_token_id         UUID,
    user_email               VARCHAR,
    refresh_token            VARCHAR,
    refresh_token_expiration TIMESTAMP,
    PRIMARY KEY (user_email, refresh_token_id)
);

CREATE TABLE flashcard
(
    flashcard_id UUID,
    name         VARCHAR,
    description  VARCHAR,
    PRIMARY KEY (flashcard_id)
);

CREATE TABLE page_by_flashcard
(
    flashcard_page_id UUID,
    flashcard_id      UUID,
    question          VARCHAR,
    answer            VARCHAR,
    created_at        TIMESTAMP,
    PRIMARY KEY (flashcard_id, flashcard_page_id)
);

CREATE TABLE resolved_page_by_flashcard
(
    resolved_page_id  UUID,
    user_email        VARCHAR,
    flashcard_page_id UUID,
    answer            VARCHAR,
    is_correct        BOOLEAN,
    created_at        TIMESTAMP,
    PRIMARY KEY (user_email, flashcard_page_id, resolved_page_id)
);