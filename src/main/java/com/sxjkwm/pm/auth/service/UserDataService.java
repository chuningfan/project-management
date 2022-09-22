package com.sxjkwm.pm.auth.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.dao.*;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.auth.entity.Department;
import com.sxjkwm.pm.auth.entity.RoleAndFunctionRelation;
import com.sxjkwm.pm.auth.entity.User;
import com.sxjkwm.pm.auth.entity.UserAndRoleRelation;
import com.sxjkwm.pm.common.CacheService;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.function.dao.FunctionDao;
import com.sxjkwm.pm.function.dto.FunctionDto;
import com.sxjkwm.pm.function.entity.Function;
import com.sxjkwm.pm.function.service.FunctionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDataService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    private final DepartmentDao departmentDao;

    private final UserAndRoleRelationDao userAndRoleRelationDao;

    private final RoleAndFunctionRelationDao roleAndFunctionRelationDao;

    private final CacheService cacheService;

    private final FunctionDao functionDao;

    @Autowired
    public UserDataService(UserDao userDao, RoleDao roleDao, DepartmentDao departmentDao, UserAndRoleRelationDao userAndRoleRelationDao, RoleAndFunctionRelationDao roleAndFunctionRelationDao, CacheService cacheService, FunctionDao functionDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.departmentDao = departmentDao;
        this.userAndRoleRelationDao = userAndRoleRelationDao;
        this.roleAndFunctionRelationDao = roleAndFunctionRelationDao;
        this.cacheService = cacheService;
        this.functionDao = functionDao;
    }

    public UserDataDto getUserDataByToken(String token) {
        String wxUserId = String.valueOf(Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8)));
        return getUserDataByWxUserId(wxUserId);
    }

    public UserDataDto getUserDataByWxUserId(String wxUserId) {
        String userDataJson = cacheService.getString(Constant.userCachePrefix + wxUserId);
        if (StringUtils.isNotBlank(userDataJson)) {
            return JSONObject.parseObject(userDataJson, UserDataDto.class);
        }
        User user = userDao.findUserByWxUserId(wxUserId);
        if (Objects.nonNull(user)) {
            UserDataDto result = new UserDataDto();
            result.setUsername(user.getName());
            result.setUserId(user.getId());
            result.setWxUserId(user.getWxUserId());
            result.setMobile(user.getMobile());
            result.setAvatar(user.getAvatar());
            result.setDo1UserId(user.getDo1UserId());
            String deptIdStr = user.getDeptIds();
            if (StringUtils.isNotBlank(deptIdStr)) {
                List<Long> deptIds = Lists.newArrayList();
                Splitter.on(",").split(deptIdStr).forEach(i -> {deptIds.add(Long.valueOf(i));});
                List<Department> departments = departmentDao.findDepartmentsByIds(deptIds);
                if (CollectionUtils.isNotEmpty(departments)) {
                    List<String> deptNames = departments.stream().map(Department::getDeptName).collect(Collectors.toList());
                    result.setDeptNames(deptNames);
                }
            }
            List<String> roleNames = userAndRoleRelationDao.findRoleNamesByWxUserId(wxUserId);
            if (CollectionUtils.isEmpty(roleNames)) {
                roleNames = Lists.newArrayList(Constant.UserRole.BUSINESS_STAFF.getValue());
            }
            result.setRoleNames(roleNames);
            List<RoleAndFunctionRelation> roleAndFunctionRelations = roleAndFunctionRelationDao.findRoleAndFunctionRelationsByRoleNames(roleNames);
            if (CollectionUtils.isNotEmpty(roleAndFunctionRelations)) {
                List<Long> functionIds = roleAndFunctionRelations.stream().map(RoleAndFunctionRelation::getFunctionId).collect(Collectors.toList());
                List<FunctionDto> accessibleFunctions = Lists.newArrayList();
                functionIds.forEach(i -> {
                    Function function = FunctionService.functionMap.get(i);
                    if (Objects.nonNull(function)) {
                        accessibleFunctions.add(new FunctionDto(function));
                    }
                });
                result.setAccessibleFunctions(accessibleFunctions);
            }
            return result;
        }
        return null;
    }

    public List<UserDataDto> fetchAvailableUsers() {
        List<UserDataDto> result = Lists.newArrayList();
        List<User> users = userDao.fetchAvailableUsers();
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        Map<Long, String> deptNameMap = null;
        List<Department> departments = departmentDao.findAll();
        if (CollectionUtils.isNotEmpty(departments)) {
            deptNameMap = departments.stream().collect(Collectors.toMap(Department::getWxDeptId, Department::getDeptName, (k1, k2) -> k1));
        }
        List<String> wxUserIds = users.stream().map(User::getWxUserId).collect(Collectors.toList());
        List<UserAndRoleRelation> relations = userAndRoleRelationDao.findByWxUserIds(wxUserIds);
        Map<String, List<String>> userRoleMap = null;
        Map<String, List<Long>> roleAndFuncMap = null;
        Map<Long, Function> functionMap = FunctionService.functionMap;
        if (CollectionUtils.isNotEmpty(relations)) {
            userRoleMap = relations.stream().collect(Collectors.groupingBy(UserAndRoleRelation::getWxUserId, Collectors.mapping(UserAndRoleRelation::getRoleName, Collectors.toList())));
            List<String> relatedRoleNames = relations.stream().map(UserAndRoleRelation::getRoleName).collect(Collectors.toList());
            List<RoleAndFunctionRelation> roleAndFunctionRelations = roleAndFunctionRelationDao.findRoleAndFunctionRelationsByRoleNames(relatedRoleNames);
            if (CollectionUtils.isNotEmpty(roleAndFunctionRelations)) {
                roleAndFuncMap = roleAndFunctionRelations.stream().collect(Collectors.groupingBy(RoleAndFunctionRelation::getRoleName, Collectors.mapping(RoleAndFunctionRelation::getFunctionId, Collectors.toList())));
            }
        }
        UserDataDto dataDto;
        for (User user: users) {
            String wxUserId = user.getWxUserId();
            dataDto = new UserDataDto();
            dataDto.setUserId(user.getId());
            dataDto.setWxUserId(wxUserId);
            dataDto.setMobile(user.getMobile());
            dataDto.setAvatar(user.getAvatar());
            dataDto.setDo1UserId(user.getDo1UserId());
            dataDto.setUsername(user.getName());
            List<String> roleNames = userRoleMap.get(wxUserId);
            if (CollectionUtils.isEmpty(roleNames)) {

            }
            List<FunctionDto> functions = Lists.newArrayList();
            dataDto.setRoleNames(roleNames);
            dataDto.setAccessibleFunctions(functions);
            if (Objects.nonNull(userRoleMap)) {
                if (CollectionUtils.isNotEmpty(roleNames)) {
                    for (String roleName: roleNames) {
                        roleNames.add(roleName);
                        if (Objects.nonNull(roleAndFuncMap)) {
                            List<Long> roleFunIds = roleAndFuncMap.get(roleName);
                            if (CollectionUtils.isNotEmpty(roleFunIds) && !functionMap.isEmpty()) {
                                for (Long funcId: roleFunIds) {
                                    Function function = functionMap.get(funcId);
                                    if (Objects.nonNull(function)) {
                                        functions.add(new FunctionDto(function));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            List<String> deptNames = Lists.newArrayList();
            dataDto.setDeptNames(deptNames);
            List<Long> wxDeptIds = Splitter.on(",").splitToList(user.getDeptIds()).stream().map(Long::valueOf).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(wxDeptIds) && Objects.nonNull(deptNameMap)) {
                for (Long wxDeptId: wxDeptIds) {
                    String deptName = deptNameMap.get(wxDeptId);
                    if (StringUtils.isNotBlank(deptName)) {
                        deptNames.add(deptName);
                    }
                }
            }
            result.add(dataDto);
        }
        return result;
    }

}
