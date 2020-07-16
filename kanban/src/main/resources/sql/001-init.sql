CREATE TABLE kanban.column
(
    id         bigserial PRIMARY KEY,
    name       character varying,
    project_id bigint
);

CREATE TABLE kanban.ticket
(
    id        bigserial PRIMARY KEY,
    name      character varying,
    content   text,
    assignee  character varying,
    column_id bigint,
    CONSTRAINT fk_column_id FOREIGN KEY (column_id)
        REFERENCES kanban.column (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE CASCADE
);
