package com.sxjkwm.pm.auth.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.dao.*;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.auth.entity.Department;
import com.sxjkwm.pm.auth.entity.Role;
import com.sxjkwm.pm.auth.entity.RoleAndFunctionRelation;
import com.sxjkwm.pm.auth.entity.User;
import com.sxjkwm.pm.common.CacheService;
import com.sxjkwm.pm.function.entity.Function;
import com.sxjkwm.pm.function.service.FunctionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserDataService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    private final DepartmentDao departmentDao;

    private final UserAndRoleRelationDao userAndRoleRelationDao;

    private final RoleAndFunctionRelationDao roleAndFunctionRelationDao;

    private final CacheService cacheService;

    @Autowired
    public UserDataService(UserDao userDao, RoleDao roleDao, DepartmentDao departmentDao, UserAndRoleRelationDao userAndRoleRelationDao, RoleAndFunctionRelationDao roleAndFunctionRelationDao, CacheService cacheService) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.departmentDao = departmentDao;
        this.userAndRoleRelationDao = userAndRoleRelationDao;
        this.roleAndFunctionRelationDao = roleAndFunctionRelationDao;
        this.cacheService = cacheService;
    }

    public UserDataDto getUserDataByWxUserId(String wxUserId) {
        String userDataJson = cacheService.getString(wxUserId);
        if (StringUtils.isNotBlank(userDataJson)) {
            return JSONObject.parseObject(userDataJson, UserDataDto.class);
        }
        User user = userDao.findUserByWxUserId(wxUserId);
        if (Objects.nonNull(user)) {
            UserDataDto result = new UserDataDto();
            result.setUserId(user.getId());
            result.setWxUserId(user.getWxUserId());
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

}
