create table pm_flow_node_auditor_relation (
    id bigint primary key,
    flow_id bigint,
    flow_node_id bigint,
    user_id varchar(255),
    is_deleted tinyint,
    created_at bigint,
    created_by varchar(255),
    modified_at bigint,
    modified_by varchar(255)
);
create index idx_far_flow_id on pm_flow_node_auditor_relation(flow_id);
create index idx_far_flow_node_id on pm_flow_node_auditor_relation(flow_node_id);
