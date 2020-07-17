CREATE TABLE kanban.activity
(
    id         bigserial PRIMARY KEY,
    name       character varying,
    position   bigint,
    project_id bigint
);

CREATE TABLE kanban.ticket
(
    id          bigserial PRIMARY KEY,
    name        character varying,
    content     text,
    assignee    character varying,
    index       bigint,
    activity_id bigint,
    CONSTRAINT fk_column_id FOREIGN KEY (activity_id)
        REFERENCES kanban.activity (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE CASCADE
);
