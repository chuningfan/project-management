package com.sxjkwm.pm.business.material.controller;

import com.sxjkwm.pm.business.material.dto.MaterialDto;
import com.sxjkwm.pm.business.material.service.MaterialCacheService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/6/1 21:01
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/material")
public class MaterialController {

    private final MaterialCacheService materialCacheService;

    @Autowired
    public MaterialController(MaterialCacheService materialCacheService) {
        this.materialCacheService = materialCacheService;
    }

    @GetMapping
    public RestResponse<List<MaterialDto>> fetchByKey(@RequestParam("keywords") String keywords) {
        return RestResponse.of(materialCacheService.getByKey(keywords));
    }

}
