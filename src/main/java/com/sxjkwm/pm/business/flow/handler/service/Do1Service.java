package com.sxjkwm.pm.business.flow.handler.service;

import cn.com.do1.apisdk.inter.FormApi;
import cn.com.do1.apisdk.inter.UploadApi;
import cn.com.do1.apisdk.inter.UserApi;
import cn.com.do1.apisdk.inter.addressbook.vo.InterfaceUser;
import cn.com.do1.apisdk.inter.addressbook.vo.UserAndDept;
import cn.com.do1.apisdk.inter.rep.vo.ApiFormPushResult;
import cn.com.do1.apisdk.inter.rep.vo.ApiGetUserListResultVO;
import cn.com.do1.apisdk.inter.rep.vo.UploadResultVO;
import cn.com.do1.apisdk.inter.req.vo.ApiGetUserListVO;
import cn.com.do1.apisdk.inter.req.vo.FormDataPushVO;
import cn.com.do1.apisdk.util.QwSdkUtil;
import cn.com.do1.apisdk.util.SdkConfig;
import cn.com.do1.apisdk.util.WxAgentUtil;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.auth.dao.UserDao;
import com.sxjkwm.pm.auth.entity.User;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.thirdparty.do1.dao.FormMappingDao;
import com.sxjkwm.pm.thirdparty.do1.dao.ProjectFormDataMappingDao;
import com.sxjkwm.pm.thirdparty.do1.dao.PropertyMappingDao;
import com.sxjkwm.pm.thirdparty.do1.dto.PropertyMappingDto;
import com.sxjkwm.pm.thirdparty.do1.entitiy.FormMapping;
import com.sxjkwm.pm.thirdparty.do1.entitiy.ProjectFormDataMapping;
import com.sxjkwm.pm.thirdparty.do1.entitiy.PropertyMapping;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/9/15 16:19
 */
@Service
public class Do1Service {

    private static final String corpId = "wp4nJkEAAAD7EZ6NXGnG7REXt3f6wIZg"; // 道一云侧企业ID
    private static final String devId = "qwef9feca6beed4e618b59d3696e766e75";
    private static final String devKey = "YzAxYTY0NzAtOTQ3Zi00YTMwLTg2NTQtY2Q0YWFhZjAwYjdh";
    private static final String baseUrl = "https://qwif.do1.com.cn/qwcgi";
    static {
        Map<String, String> configMap = Maps.newHashMap();
        configMap.put("developerId", devId);
        configMap.put("developerKey", devKey);
        configMap.put("corpId", corpId);
        configMap.put("baseUrl", baseUrl);
        SdkConfig.setConfig(configMap);
    }

    private final UserDao userDao;

    private final FormMappingDao formMappingDao;

    private final PropertyMappingDao propertyMappingDao;

    private final ProjectFormDataMappingDao projectFormDataMappingDao;

    @Autowired
    public Do1Service(UserDao userDao, FormMappingDao formMappingDao, PropertyMappingDao propertyMappingDao, ProjectFormDataMappingDao projectFormDataMappingDao) {
        this.userDao = userDao;
        this.formMappingDao = formMappingDao;
        this.propertyMappingDao = propertyMappingDao;
        this.projectFormDataMappingDao = projectFormDataMappingDao;
    }

    public ApiFormPushResult startOrCommitFlow(String formId, Map<String, Object> dataMap, Integer command) throws Throwable {
        String creator = ContextHelper.getUserData().getDo1UserId();
        FormDataPushVO<Map<String,Object>> vo = new FormDataPushVO();
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        Set<Map.Entry<String, Object>> entrySet = dataMap.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
        UploadApi uploadApi = QwSdkUtil.getInter(UploadApi.class);
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String fieldId = entry.getKey();
            Object value = entry.getValue();
            if (Objects.isNull(value)) {
                continue;
            }
            if (value instanceof File) {
                File attachment = (File) value;
                UploadResultVO resultVO = uploadApi.upload(QwSdkUtil.getCacheToken(), attachment, creator, WxAgentUtil.getFormCode(),true);
                fieldMap.put(fieldId, resultVO.getUri());
            } else if (value instanceof Collection) {
                Collection collection = (Collection) value;
                List<Map> subForm = Lists.newArrayList();
                for (Object element: collection) {
                    if (!(element instanceof Map)) {
                        throw new PmException(PmError.ILLEGAL_PARAMETER);
                    }
                    Map<String, Object> eleMap = (Map<String, Object>) element;
                    Set<String> keys = eleMap.keySet();
                    for (String key: keys) {
                        Object val = eleMap.getOrDefault(key, "");
                        eleMap.put(key, val instanceof String ? val : val.toString());
                    }
                    subForm.add(eleMap);
                }
                fieldMap.put(fieldId, JSONArray.toJSONString(subForm));
            } else {
                fieldMap.put(fieldId, value.toString());
            }
        }
        vo.setCreator(creator);
        vo.setFieldMap(fieldMap);
        FormApi api = QwSdkUtil.getInter(FormApi.class);
        ApiFormPushResult result = api.pushFormData(QwSdkUtil.getCacheToken(), formId, command, new FormDataPushVO[]{vo});
        return result;
    }

    public ApiGetUserListResultVO getAllUsers() {
        ApiGetUserListVO vo = new ApiGetUserListVO();
        vo.setCurrentPage("1");
        vo.setVersion("0");
        UserApi api = QwSdkUtil.getInter(UserApi.class);
        ApiGetUserListResultVO result = api.getUserList(QwSdkUtil.getCacheToken(),vo);
        return result;
    }

    public Boolean syncIds() {
        ApiGetUserListResultVO do1UserResult = getAllUsers();
        Map<String, String> do1UserMap = Maps.newHashMap();
        if (Objects.nonNull(do1UserResult)) {
            List<UserAndDept> do1UserAndDepts = Lists.newArrayList(do1UserResult.getList());
            if (CollectionUtils.isNotEmpty(do1UserAndDepts)) {
                List<InterfaceUser> do1Users = do1UserAndDepts.stream().map(UserAndDept::getInterfaceUser).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(do1Users)) {
                    do1Users = do1Users.stream().filter(u -> Objects.nonNull(u) && StringUtils.isNotBlank(u.getMobile())).collect(Collectors.toList());
                    do1UserMap = do1Users.stream().collect(Collectors.toMap(InterfaceUser::getMobile, InterfaceUser::getWxUserId, (k1, k2) -> k2));
                }
            }
        }
        List<User> existingUsers = userDao.findAll();
        for (User user: existingUsers) {
            user.setDo1UserId(do1UserMap.get(user.getMobile()));
        }
        userDao.saveAll(existingUsers);
        return true;
    }

    public List<FormMapping> fetchAvailableForms() {
        FormMapping condition = new FormMapping();
        condition.setIsDeleted(Constant.YesOrNo.NO.getValue());
        return formMappingDao.findAll();
    }

    public List<PropertyMapping> saveOrUpdatePropertyMapping(List<PropertyMappingDto> mappings) {
        List<PropertyMapping> propertyMappings = Lists.newArrayList();
        PropertyMapping propertyMapping;
        for (PropertyMappingDto dto: mappings) {
            propertyMapping = new PropertyMapping();
            BeanUtil.copyProperties(dto, propertyMapping, "createdBy,createdAt,modifiedBy,modifiedAt");
            propertyMappings.add(propertyMapping);
        }
        return propertyMappingDao.saveAll(propertyMappings);
    }

    public Boolean removeMappingByDefinition(Long flowId, Long flowNodeId, Long defId, String formId, String fieldId) {
        PropertyMapping condition = new PropertyMapping();
        condition.setFlowId(flowId);
        condition.setAuditingFlowNodeId(flowNodeId);
        condition.setDefId(defId);
        condition.setFormId(formId);
        condition.setFieldId(fieldId);
        propertyMappingDao.delete(condition);
        return true;
    }

    public List<PropertyMappingDto> findPropertyMappingByFlowNodeId(Long auditingFlowNodeId) {
        PropertyMapping condition = new PropertyMapping();
        condition.setIsDeleted(Constant.YesOrNo.NO.getValue());
        condition.setAuditingFlowNodeId(auditingFlowNodeId);
        List<PropertyMapping> propertyMappings = propertyMappingDao.findAll(Example.of(condition));
        if (CollectionUtils.isEmpty(propertyMappings)) {
            return Collections.emptyList();
        }
        List<PropertyMappingDto> dtos = Lists.newArrayList();
        PropertyMappingDto dto;
        for (PropertyMapping propertyMapping: propertyMappings) {
            dto = new PropertyMappingDto();
            BeanUtil.copyProperties(propertyMapping, dto);
            dtos.add(dto);
        }
        return dtos;
    }

    // 查询项目基本信息字段映射
    public List<ProjectFormDataMapping> fetchProjectFormDataMapping(Long flowId, String formId) {
        ProjectFormDataMapping condition = new ProjectFormDataMapping();
        condition.setFormId(formId);
        condition.setFlowId(flowId);
        List<ProjectFormDataMapping> mappings = projectFormDataMappingDao.findAll(Example.of(condition));
        if (CollectionUtils.isEmpty(mappings)) {
            return Collections.emptyList();
        }
        return mappings;
    }

    public Boolean deletePropertyMapping(Long id) {
        propertyMappingDao.deleteById(id);
        return Boolean.TRUE;
    }

}
