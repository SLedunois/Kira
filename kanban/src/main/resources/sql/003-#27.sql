CREATE OR REPLACE FUNCTION kanban.set_index() RETURNS TRIGGER AS
$BODY$
DECLARE
    ticketIndex bigint;
BEGIN
    SELECT MAX(index) + 1 FROM kanban.ticket WHERE activity_id = NEW.activity_id INTO ticketIndex;
    NEW.index = ticketIndex;
    RETURN NEW;
END
$BODY$
    LANGUAGE plpgsql;

CREATE TRIGGER set_index
    BEFORE INSERT
    ON kanban.ticket
    FOR EACH ROW
EXECUTE PROCEDURE kanban.set_index();
