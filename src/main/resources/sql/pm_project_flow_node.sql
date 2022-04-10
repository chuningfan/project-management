create table pm_project_flow_node (
    id bigint primary key, -- 主键
    flow_id bigint, -- 所属流程ID
    node_name varchar(255),  -- 流程节点ID
    skippable tinyint, -- 是否可以跳过
    audittable tinyint, -- 是否需要审批
    node_index int(4), -- 节点位置
    pattern_files text, -- 模板文件
    `description` varchar(4000), -- 节点描述
    node_version int(4), -- 节点版本号
    form_id varchar(255), -- 道一云审批流表单id
    is_deleted tinyint,
    created_at bigint,
    created_by varchar(255),
    modified_at bigint,
    modified_by varchar(255)
);
create index idx_project_flow_node_flow_id on pm_project_flow_node(flow_id);