package com.sxjkwm.pm.auth.controller;

import com.sxjkwm.pm.auth.entity.User;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.wxwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.API_FEATURE + "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, UserService userService1) {
        this.userService = userService1;
    }

    @GetMapping("/list")
    public RestResponse<Page<User>> listUsers(@RequestParam("page") int pageNo, @RequestParam("pageSize") int pageSize) {
        return RestResponse.of(userService.findUsersByPage(pageSize, pageNo));
    }

}
