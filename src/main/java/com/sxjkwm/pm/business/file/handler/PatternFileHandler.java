package com.sxjkwm.pm.business.file.handler;

import cn.hutool.core.lang.UUID;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.file.dao.ProjectFileDao;
import com.sxjkwm.pm.business.file.entity.ProjectFile;
import com.sxjkwm.pm.business.file.handler.replacement.BaseReplacement;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.business.project.dao.ProjectNodeDao;
import com.sxjkwm.pm.business.project.dao.ProjectNodePropertyDao;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.business.project.entity.ProjectNode;
import com.sxjkwm.pm.business.project.entity.ProjectNodeProperty;
import com.sxjkwm.pm.business.project.service.ProjectService;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.ContextUtil;
import com.sxjkwm.pm.util.OSSUtil;
import com.sxjkwm.pm.util.S3FileUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.data.domain.Example;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/7/8 14:20
 */
public interface PatternFileHandler {

    @Transactional
    default Long doHandle0(InputStream targetPatternFileStream, Long dataId, Long flowNodeId, Long propertyDefId, String fileName) throws Throwable {
        try (InputStream inputStream = targetPatternFileStream; XWPFDocument document = new XWPFDocument(inputStream);) {
            FlowNodeDefinitionDao flowNodeDefinitionDao = ContextUtil.getBean(FlowNodeDefinitionDao.class);
            ProjectService projectService = ContextUtil.getBean(ProjectService.class);
            ProjectDto projectDto = projectService.getId(dataId);
            FlowNodeDefinition flowNodeDefinition = flowNodeDefinitionDao.findById(propertyDefId).get();
            S3FileUtil s3FileUtil = ContextUtil.getBean(S3FileUtil.class);
            List<BaseReplacement> dataList = captureData(dataId, projectDto.getFlowId(), flowNodeId, propertyDefId);
            processDataAsDefault(document, dataList);
            ProjectFileDao projectFileDao = ContextUtil.getBean(ProjectFileDao.class);
            ProjectFile condition = new ProjectFile();
            condition.setFlowNodeId(flowNodeId);
            condition.setProjectId(dataId);
            condition.setFlowId(projectDto.getFlowId());
            condition.setPropertyDefId(propertyDefId);
//            String fileName = projectDto.getProjectName() + "-" + flowNodeDefinition.getPropertyName() + ".docx";
            ProjectFile oldFile = projectFileDao.findOne(Example.of(condition)).orElse(null);
            if (Objects.nonNull(oldFile)) {
//                fileName = oldFile.getFileName();
                String bucketName = oldFile.getBucketName();
                String objectName = oldFile.getObjectName();
                s3FileUtil.remove(bucketName, objectName);
                projectFileDao.delete(oldFile);
            }
            String bucketName = OSSUtil.getBucketName(dataId);
            String objectName = dataId + "/" + flowNodeId + "/" + (UUID.fastUUID().toString().replace("-", "").toLowerCase());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.write(baos);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            s3FileUtil.upload(bucketName, objectName, fileName, bais);
            ProjectFile newFile = new ProjectFile();
            newFile.setFileName(fileName);
            newFile.setFlowNodeId(flowNodeId);
            newFile.setPropertyDefId(propertyDefId);
            newFile.setBucketName(bucketName);
            newFile.setObjectName(objectName);
            newFile.setFlowId(projectDto.getFlowId());
            newFile.setProjectId(projectDto.getId());
            newFile.setIsDeleted(0);
            projectFileDao.save(newFile);
            // cover property value
            ProjectNodePropertyDao projectNodePropertyDao = ContextUtil.getBean(ProjectNodePropertyDao.class);
            List<ProjectNodeProperty> projectNodePropertyList = projectNodePropertyDao.findByPropDefIdsAndProjectId(Lists.newArrayList(propertyDefId), dataId);
            ProjectNodeProperty projectNodeProperty;
            if (CollectionUtils.isEmpty(projectNodePropertyList)) {
                projectNodeProperty = new ProjectNodeProperty();
                projectNodeProperty.setFlowNodePropertyDefId(propertyDefId);
                projectNodeProperty.setProjectId(dataId);
                projectNodeProperty.setIsDeleted(0);
                ProjectNodeDao projectNodeDao = ContextUtil.getBean(ProjectNodeDao.class);
                ProjectNode projectNodeCondition = new ProjectNode();
                projectNodeCondition.setIsDeleted(0);
                projectNodeCondition.setProjectId(dataId);
                projectNodeCondition.setFlowNodeId(flowNodeId);
                ProjectNode projectNode = projectNodeDao.findOne(Example.of(projectNodeCondition)).orElse(null);
                if (Objects.isNull(projectNode)) {
                    throw new PmException(PmError.NO_DATA_FOUND);
                }
                projectNodeProperty.setProjectNodeId(projectNode.getId());
            } else {
                projectNodeProperty = projectNodePropertyList.get(0);
            }
            projectNodeProperty.setPropertyValue(newFile.getId().toString());
            projectNodePropertyDao.save(projectNodeProperty);
            return newFile.getId();
        }
    }

    // Extension point
    List<BaseReplacement> captureData(Long dataId, Long flowId, Long flowNodeId, Long propertyDefId);

    default void processDataAsDefault(XWPFDocument document, List<BaseReplacement> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (BaseReplacement replacement: dataList) {
            replacement.getReplacementType().convert(document, replacement);
        }
    }

}
