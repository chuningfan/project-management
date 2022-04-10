create table pm_project_node (
    id bigint primary key, -- 主键
    project_id bigint, -- 所属项目ID
    flow_id bigint, -- 所属流程ID
    flow_node_id bigint, -- 所属节点ID
    attached_files text, -- 上传文件
    is_done tinyint, -- 是否完成阶段
    node_time bigint, -- 节点时间
    project_node_name varchar(255), -- 节点名称
    `description` varchar(4000), -- 描述
    node_version int(4), -- 流程节点版本号
    audit_status int(4), -- 审批状态
    form_id varchar(255), -- 道一云审批流表单id
    is_deleted tinyint,
    created_at bigint,
    created_by varchar(255),
    modified_at bigint,
    modified_by varchar(255)
);
create index idx_project_node_project_id on pm_project_node(project_id);
create index idx_project_node_flow_id on pm_project_node(flow_id);
create index idx_project_node_flow_node_id on pm_project_node(flow_node_id);