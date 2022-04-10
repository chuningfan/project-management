create table pm_project (
    id bigint primary key,
    project_name varchar(255),
    flow_id bigint,
    part_a varchar(255),
    part_b varchar(255),
    current_node_id bigint,
    is_deleted tinyint,
    created_at bigint,
    created_by varchar(255),
    modified_at bigint,
    modified_by varchar(255)
);
create index idx_project_flow_id on pm_project(flow_id);