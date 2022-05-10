package com.sxjkwm.pm.business.project.controller;

import com.sxjkwm.pm.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {

//    @PostMapping
//    public RestResponse<ProjectDto> newProject(@RequestBody ProjectDto projectDto) {
//
//    }

    @GetMapping
    public String test() {
        getUserData();
        return "123";
    }


}
