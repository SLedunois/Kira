ALTER TABLE account.user
    ADD COLUMN color character varying;

CREATE OR REPLACE FUNCTION account.color() RETURNS character varying AS
$BODY$
DECLARE
    colors character varying[] := '{primary,secondary,purple,yellow}';
BEGIN
    RETURN colors[floor(random() * 4)::int + 1];
END;
$BODY$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION account.set_color() RETURNS TRIGGER AS
$BODY$
DECLARE
    userColor character varying;
BEGIN
    SELECT account.color() INTO userColor;
    NEW.color = userColor;

    RETURN NEW;
END;
$BODY$
    LANGUAGE plpgsql;

CREATE TRIGGER set_color
    BEFORE INSERT
    ON account.user
    FOR EACH ROW
EXECUTE PROCEDURE account.set_color();
