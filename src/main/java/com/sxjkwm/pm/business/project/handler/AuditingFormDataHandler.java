package com.sxjkwm.pm.business.project.handler;

import cn.com.do1.apisdk.inter.rep.vo.ApiFormPushResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.business.file.dao.ProjectFileDao;
import com.sxjkwm.pm.business.file.entity.ProjectFile;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.business.project.dao.ProjectNodePropertyDao;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.business.project.entity.ProjectNodeProperty;
import com.sxjkwm.pm.business.project.service.ProjectService;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.thirdparty.do1.dao.PropertyMappingDao;
import com.sxjkwm.pm.thirdparty.do1.entitiy.ProjectFormDataMapping;
import com.sxjkwm.pm.thirdparty.do1.entitiy.PropertyMapping;
import com.sxjkwm.pm.business.flow.handler.service.Do1Service;
import com.sxjkwm.pm.util.ContextUtil;
import com.sxjkwm.pm.util.FileUtil;
import com.sxjkwm.pm.util.S3FileUtil;
import io.minio.GetObjectResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/9/17 16:37
 */
@Component
public class AuditingFormDataHandler {

    private final Do1Service do1Service;

    private final PropertyMappingDao propertyMappingDao;

    private final ProjectNodePropertyDao projectNodePropertyDao;

    private final FlowNodeDefinitionDao flowNodeDefinitionDao;

    private final ProjectFileDao projectFileDao;

    private final S3FileUtil s3FileUtil;

    private final ProjectService projectService;

    @Autowired
    public AuditingFormDataHandler(Do1Service do1Service, PropertyMappingDao propertyMappingDao, ProjectNodePropertyDao projectNodePropertyDao, FlowNodeDefinitionDao flowNodeDefinitionDao, ProjectFileDao projectFileDao, S3FileUtil s3FileUtil, ProjectService projectService) {
        this.do1Service = do1Service;
        this.propertyMappingDao = propertyMappingDao;
        this.projectNodePropertyDao = projectNodePropertyDao;
        this.flowNodeDefinitionDao = flowNodeDefinitionDao;
        this.projectFileDao = projectFileDao;
        this.s3FileUtil = s3FileUtil;
        this.projectService = projectService;
    }

    public ApiFormPushResult doHandle(Long projectId, Long flowId, Long flowNodeId, String formId, String specialFormDataHandler, Integer command) throws Throwable {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<File> tmpFiles = Lists.newArrayList();
        try {
            Map<String, Object> formDataMap = Maps.newHashMap();
            PropertyMapping mappingCondition = new PropertyMapping();
            mappingCondition.setFormId(formId);
            mappingCondition.setFlowId(flowId);
            mappingCondition.setFlowNodeId(flowNodeId);
            List<PropertyMapping> mappingList = propertyMappingDao.findAll(Example.of(mappingCondition));
            if (CollectionUtils.isEmpty(mappingList)) {
                throw new PmException(PmError.NO_DATA_FOUND, "No auditing form mapping found");
            }
            List<Long> simpleDefIds = mappingList.stream().map(PropertyMapping::getDefId).collect(Collectors.toList());
            List<ProjectNodeProperty> projectNodePropertyList = projectNodePropertyDao.findByPropDefIdsAndProjectId(simpleDefIds, projectId);
            if (CollectionUtils.isEmpty(projectNodePropertyList)) {
                throw new PmException(PmError.NO_DATA_FOUND, "No data to audit");
            }
            Map<Long, String> defValueMap = projectNodePropertyList.stream().collect(Collectors.toMap(ProjectNodeProperty::getFlowNodePropertyDefId, ProjectNodeProperty::getPropertyValue, (k1, k2) -> k1));
            List<Long> defIdsWithValues = projectNodePropertyList.stream().map(ProjectNodeProperty::getFlowNodePropertyDefId).collect(Collectors.toList()); // 获取有值数据
            List<PropertyMapping> effectiveMappings = mappingList.stream().filter(m -> defIdsWithValues.contains(m.getDefId())).collect(Collectors.toList());
            List<FlowNodeDefinition> defs = flowNodeDefinitionDao.findByIds(simpleDefIds);
            Map<Long, String> defTypeMap = defs.stream().collect(Collectors.toMap(FlowNodeDefinition::getId, FlowNodeDefinition::getPropertyType, (k1, k2) -> k1));
            Object obj;
            for (PropertyMapping mapping : effectiveMappings) {
                Long defId = mapping.getDefId();
                String fieldId = mapping.getFieldId();
                String val = defValueMap.get(defId);
                obj = val;
                String type = defTypeMap.get(defId);
                if ("FILE".equals(type)) {
                    ProjectFile projectFile = projectFileDao.getOne(Long.valueOf(val));
                    if (Objects.nonNull(projectFile)) {
                        String bucketName = projectFile.getBucketName();
                        String objectName = projectFile.getObjectName();
                        String fileName = projectFile.getFileName();
                        try (GetObjectResponse inputStream = s3FileUtil.getFile(bucketName, objectName)) {
                            File file = FileUtil.streamToFile(inputStream, fileName);
                            obj = file;
                            tmpFiles.add(file);
                        }
                    }
                } else if ("TIME".equals(type)) {
                    obj = simpleDateFormat.format(new Date(Long.valueOf(val)));
                }
                formDataMap.put(fieldId, obj);
            }
            List<ProjectFormDataMapping> projectInfoMappings = do1Service.fetchProjectFormDataMapping(flowId, formId);
            if (CollectionUtils.isNotEmpty(projectInfoMappings)) {
                ProjectDto projectDto = projectService.getById(projectId);
                if (Objects.nonNull(projectDto)) {
                    for (ProjectFormDataMapping mapping : projectInfoMappings) {
                        String fieldId = mapping.getFieldId();
                        String sysField = mapping.getSysField();
                        if (StringUtils.isNotBlank(sysField)) {
                            Field field = ProjectDto.class.getDeclaredField(sysField);
                            boolean isAccessible = field.isAccessible();
                            field.setAccessible(true);
                            Object val = field.get(projectDto);
                            field.setAccessible(isAccessible);
                            formDataMap.put(fieldId, val);
                        }
                    }
                }
            }
            if (StringUtils.isNotBlank(specialFormDataHandler)) {
                SpecialFormDataHandler handler = ContextUtil.getBean(specialFormDataHandler);
                handler.doHandle(formDataMap);
            }
            ApiFormPushResult result = do1Service.startOrCommitFlow(formId, formDataMap, command);
            return result;
        } finally {
            if (CollectionUtils.isNotEmpty(tmpFiles)) {
                for (File file: tmpFiles) {
                    file.delete();
                }
            }
        }
    }

}
