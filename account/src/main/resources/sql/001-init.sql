CREATE TABLE account.user
(
    email        character varying NOT NULL,
    last_name    character varying,
    password     character varying,
    first_name   character varying,
    search_field text              NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (email)
);


CREATE OR REPLACE FUNCTION account.fill_search_field() RETURNS TRIGGER AS
$BODY$
BEGIN
    NEW.search_field = CONCAT(NEW.email, ';', NEW.last_name, ' ', NEW.first_name, ' ', NEW.last_name);

    RETURN NEW;
END;
$BODY$
    LANGUAGE plpgsql;

CREATE TRIGGER field_search_field
    BEFORE INSERT
    ON account.user
    FOR EACH ROW
EXECUTE PROCEDURE account.fill_search_field();
