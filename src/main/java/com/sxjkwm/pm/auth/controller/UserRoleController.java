package com.sxjkwm.pm.auth.controller;

import com.sxjkwm.pm.auth.service.UserAndRoleRelationService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.API_FEATURE + "/userRole")
public class UserRoleController {

    private final UserAndRoleRelationService userAndRoleRelationService;

    @Autowired
    public UserRoleController(UserAndRoleRelationService userAndRoleRelationService) {
        this.userAndRoleRelationService = userAndRoleRelationService;
    }

    @PostMapping("/{wxUserId}")
    public RestResponse<Boolean> addRelations(@PathVariable("wxUserId") String wxUserId, @RequestBody List<Long> roleIds) {
        return RestResponse.of(userAndRoleRelationService.populateRelations(wxUserId, roleIds));
    }

}
