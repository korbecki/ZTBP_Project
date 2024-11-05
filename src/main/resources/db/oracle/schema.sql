CREATE TABLE users
(
    user_id   NUMBER,
    name      VARCHAR(50),
    surname   VARCHAR(50),
    user_name VARCHAR(50) UNIQUE,
    password  VARCHAR(256),
    email     VARCHAR(256),
    PRIMARY KEY (user_id)
);

CREATE TABLE refresh_token
(
    refresh_token_id         NUMBER,
    user_id                  NUMBER,
    refresh_token            VARCHAR(250),
    refresh_token_expiration TIMESTAMP,
    PRIMARY KEY (refresh_token_id),
    CONSTRAINT fk_user_user_id FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE flashcard
(
    flashcard_id NUMBER,
    name         VARCHAR(50),
    description  VARCHAR(250),
    CONSTRAINT pk_flashcards PRIMARY KEY (flashcard_id)
);

CREATE TABLE flashcard_page
(
    flashcard_page_id NUMBER NOT NULL,
    flashcard_id      NUMBER NOT NULL,
    question          VARCHAR(256),
    answer            VARCHAR(256),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_flashcard_page PRIMARY KEY (flashcard_page_id),
    CONSTRAINT fk_flashcard_flashcard_id FOREIGN KEY (flashcard_id) REFERENCES FLASHCARD (flashcard_id)
);

CREATE TABLE resolved_page
(
    resolved_page_id  NUMBER,
    user_id           NUMBER,
    flashcard_page_id NUMBER,
    answer            VARCHAR(256),
    is_correct        NUMBER(1, 0),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (resolved_page_id),
    CONSTRAINT fk_resolved_page_user_user_id FOREIGN KEY (user_id) REFERENCES USERS (user_id),
    CONSTRAINT fk_resolved_page_flashcard_page_flashcard_page_id FOREIGN KEY (flashcard_page_id) REFERENCES FLASHCARD_PAGE (flashcard_page_id)
);