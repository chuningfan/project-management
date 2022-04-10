create table pm_project_flow (
    id bigint primary key,
    flow_name varchar(255),
    is_deleted tinyint,
    created_at bigint,
    created_by varchar(255),
    modified_at bigint,
    modified_by varchar(255)
);