CREATE TABLE account.user
(
    email      character varying NOT NULL,
    last_name  character varying,
    password   character varying,
    first_name character varying,
    CONSTRAINT user_pkey PRIMARY KEY (email)
);
