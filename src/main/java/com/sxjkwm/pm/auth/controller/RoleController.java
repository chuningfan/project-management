package com.sxjkwm.pm.auth.controller;


import com.sxjkwm.pm.auth.entity.Role;
import com.sxjkwm.pm.auth.service.RoleService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public RestResponse<Role> add(@RequestBody Role role) {
        return RestResponse.of(roleService.saveOrUpdateRole(role));
    }

    @PutMapping
    public RestResponse<Role> update(@RequestBody Role role) {
        return RestResponse.of(roleService.saveOrUpdateRole(role));
    }

    @DeleteMapping("/{id}")
    public RestResponse<Boolean> delete(@PathVariable("id") Long roleId) {
        return RestResponse.of(roleService.deleteRole(roleId));
    }

    @GetMapping("/{id}")
    public RestResponse<Role> getOne(@PathVariable("id") Long id) {
        return RestResponse.of(roleService.getOne(id));
    }

    @GetMapping
    public RestResponse<List<Role>> getAll() {
        return RestResponse.of(roleService.findAllRoles());
    }

}
