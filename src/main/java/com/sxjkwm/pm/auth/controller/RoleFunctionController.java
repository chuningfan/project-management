package com.sxjkwm.pm.auth.controller;

import com.sxjkwm.pm.auth.service.RoleAndFunctionRelationService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.API_FEATURE + "/roleFunction")
public class RoleFunctionController {

    private final RoleAndFunctionRelationService roleAndFunctionRelationService;

    @Autowired
    public RoleFunctionController(RoleAndFunctionRelationService roleAndFunctionRelationService) {
        this.roleAndFunctionRelationService = roleAndFunctionRelationService;
    }

    @PostMapping("/{roleName}")
    public RestResponse<Boolean> populateRelations(@PathVariable("roleName") String roleName, @RequestBody List<Long> functionIds) {
        return RestResponse.of(roleAndFunctionRelationService.populateRelations(roleName, functionIds));
    }

}
