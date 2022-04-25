package com.sxjkwm.pm.wxwork.controller;

import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.wxwork.service.DepartmentService;
import com.sxjkwm.pm.wxwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wxWork")
public class WxWorkController {

    private final DepartmentService departmentService;

    private final UserService userService;

    @Autowired
    public WxWorkController(DepartmentService departmentService, UserService userService) {
        this.departmentService = departmentService;
        this.userService = userService;
    }

    @PostMapping("/users")
    public RestResponse<Boolean> syncUsers() throws PmException {
        return RestResponse.of(userService.syncUsers(null));
    }

    @PostMapping("/departments")
    public RestResponse<Boolean> syncDepartments() throws PmException {
        return RestResponse.of(departmentService.syncDepartments());
    }



}
