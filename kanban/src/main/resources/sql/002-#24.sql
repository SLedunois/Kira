CREATE OR REPLACE FUNCTION kanban.refreshIndexes(activity bigint, new_index bigint) RETURNS VOID AS
$BODY$
BEGIN
    UPDATE kanban.ticket
    SET index = index - 1
    WHERE activity_id = activity
      AND index >= new_index;
END;
$BODY$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION kanban.decrement_indexes(ticket_id bigint, activity bigint, new_index bigint) RETURNS VOID AS
$BODY$
BEGIN
    UPDATE kanban.ticket
    SET index = index - 1
    WHERE id <> ticket_id
      AND activity_id = activity
      AND index <= new_index;
END;
$BODY$
    LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION kanban.increment_indexes(ticket_id bigint, activity bigint, new_index bigint) RETURNS VOID AS
$BODY$
BEGIN
    UPDATE kanban.ticket
    SET index = index + 1
    WHERE id <> ticket_id
      AND activity_id = activity
      AND index >= new_index;
END;
$BODY$
    LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION kanban.move_ticket(ticket_id bigint, activity bigint, new_index bigint) RETURNS kanban.ticket AS
$BODY$
DECLARE
    ticket kanban.ticket%rowtype;

BEGIN
    SELECT * FROM kanban.ticket WHERE id = ticket_id INTO ticket;
    IF ticket.activity_id = activity THEN
        IF new_index > ticket.index THEN
            PERFORM kanban.decrement_indexes(ticket_id, activity, new_index);
        END IF;

        IF new_index < ticket.index THEN
            PERFORM kanban.increment_indexes(ticket_id, activity, new_index);
        END IF;
    END IF;

    IF ticket.activity_id <> activity THEN
        PERFORM kanban.increment_indexes(ticket_id, activity, new_index);
        UPDATE kanban.ticket
        SET activity_id = activity
        WHERE id = ticket_id;
        PERFORM kanban.refreshIndexes(ticket.activity_id, ticket.index);
    END IF;

    UPDATE kanban.ticket
    SET index = new_index
    WHERE id = ticket_id;

    SELECT * FROM kanban.ticket WHERE id = ticket_id INTO ticket;
    RETURN ticket;
END;
$BODY$
    LANGUAGE plpgsql;
