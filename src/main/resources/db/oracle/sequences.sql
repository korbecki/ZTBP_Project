CREATE SEQUENCE users_seq
    START WITH 1
    INCREMENT BY 1
    NOMAXVALUE
    CACHE 100;

CREATE SEQUENCE refresh_token_seq
    START WITH 1
    INCREMENT BY 1
    NOMAXVALUE
    CACHE 100;

CREATE SEQUENCE flashcard_seq
    START WITH 1
    INCREMENT BY 1
    NOMAXVALUE
    CACHE 100;

CREATE SEQUENCE flashcard_page_seq
    START WITH 1
    INCREMENT BY 1
    NOMAXVALUE
    CACHE 100;

CREATE SEQUENCE resolved_page_seq
    START WITH 1
    INCREMENT BY 1
    NOMAXVALUE
    CACHE 100;