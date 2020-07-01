CREATE TABLE project.project
(
    id      bigserial                   NOT NULL,
    name    character varying,
    owner   character varying,
    created timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT project_pkey PRIMARY KEY (id)
);

CREATE TABLE project.project_member
(
    project_id bigint            NOT NULL,
    email      character varying NOT NULL,
    CONSTRAINT project_member_pkey PRIMARY KEY (email, project_id),
    CONSTRAINT fk_project_id FOREIGN KEY (project_id)
        REFERENCES project.project (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE CASCADE
)
