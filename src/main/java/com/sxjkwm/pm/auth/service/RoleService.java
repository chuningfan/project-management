package com.sxjkwm.pm.auth.service;

import com.sxjkwm.pm.auth.dao.RoleDao;
import com.sxjkwm.pm.auth.entity.Role;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleDao roleDao;

    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Role saveOrUpdateRole(Role role) {
        return roleDao.save(role);
    }

    public Boolean deleteRole(Long roleId) {
        Role role = roleDao.findById(roleId).get();
        role.setIsDeleted(Constant.YesOrNo.YES.getValue());
        roleDao.save(role);
        return true;
    }

    public List<Role> findAllRoles() {
        Role condtion = new Role();
        condtion.setIsDeleted(Constant.YesOrNo.NO.getValue());
        return roleDao.findAll(Example.of(condtion));
    }

    public Role getOne(Long roleId) {
        return roleDao.getOne(roleId);
    }

}
