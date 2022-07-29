package com.sxjkwm.pm.auth.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.dao.*;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.auth.entity.*;
import com.sxjkwm.pm.common.CacheService;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.function.dao.FunctionDao;
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
            List<Long> roleIds = userAndRoleRelationDao.findRoleIdsByWxUserId(wxUserId);
            if (CollectionUtils.isNotEmpty(roleIds)) {
                List<Role> roles = roleDao.findRolesByIds(roleIds);
                if (CollectionUtils.isNotEmpty(roles)) {
                    result.setRoleNames(roles.stream().map(Role::getName).collect(Collectors.toList()));
                }
                List<RoleAndFunctionRelation> roleAndFunctionRelations = roleAndFunctionRelationDao.findRoleAndFunctionRelationsByRoleIds(roleIds);
                if (CollectionUtils.isNotEmpty(roleAndFunctionRelations)) {
                    List<Long> functionIds = roleAndFunctionRelations.stream().map(RoleAndFunctionRelation::getFunctionId).collect(Collectors.toList());
                    List<Function> accessibleFunctions = Lists.newArrayList();
                    functionIds.forEach(i -> {
                        accessibleFunctions.add(FunctionService.functionMap.get(i));
                    });
                    result.setAccessibleFunctions(accessibleFunctions);
                }
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
        Map<Long, String> roleNameMap = null;
        Map<String, List<Long>> userRoleMap = null;
        Map<Long, List<Long>> roleAndFuncMap = null;
        Map<Long, Function> functionMap = FunctionService.functionMap;
        if (CollectionUtils.isNotEmpty(relations)) {
            userRoleMap = relations.stream().collect(Collectors.groupingBy(UserAndRoleRelation::getWxUserId, Collectors.mapping(UserAndRoleRelation::getRoleId, Collectors.toList())));
            List<Long> relatedRoleIds = relations.stream().map(UserAndRoleRelation::getRoleId).collect(Collectors.toList());
            List<Role> roles = roleDao.findRolesByIds(relatedRoleIds);
            if (CollectionUtils.isNotEmpty(roles)) {
                roleNameMap = roles.stream().collect(Collectors.toMap(Role::getId, Role::getName, (k1, k2) -> k1));
            }
            List<RoleAndFunctionRelation> roleAndFunctionRelations = roleAndFunctionRelationDao.findRoleAndFunctionRelationsByRoleIds(relatedRoleIds);
            if (CollectionUtils.isNotEmpty(roleAndFunctionRelations)) {
                roleAndFuncMap = roleAndFunctionRelations.stream().collect(Collectors.groupingBy(RoleAndFunctionRelation::getRoleId, Collectors.mapping(RoleAndFunctionRelation::getFunctionId, Collectors.toList())));
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
            dataDto.setUsername(user.getName());
            List<String> roleNames = Lists.newArrayList();
            List<Function> functions = Lists.newArrayList();
            dataDto.setRoleNames(roleNames);
            dataDto.setAccessibleFunctions(functions);
            if (Objects.nonNull(userRoleMap)) {
                List<Long> roleIds = userRoleMap.get(wxUserId);
                if (CollectionUtils.isNotEmpty(roleIds) && Objects.nonNull(roleNameMap)) {
                    for (Long roleId: roleIds) {
                        String roleName = roleNameMap.get(roleId);
                        roleNames.add(roleName);
                        if (Objects.nonNull(roleAndFuncMap)) {
                            List<Long> roleFunIds = roleAndFuncMap.get(roleId);
                            if (CollectionUtils.isNotEmpty(roleFunIds) && !functionMap.isEmpty()) {
                                for (Long funcId: roleFunIds) {
                                    Function function = functionMap.get(funcId);
                                    if (Objects.nonNull(function)) {
                                        functions.add(function);
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
