package com.sxjkwm.pm.business.material.controller;

import com.sxjkwm.pm.business.material.dto.CategoryDto;
import com.sxjkwm.pm.business.material.service.CategoryService;
import com.sxjkwm.pm.common.BaseController;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/31 16:17
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/category")
public class CategoryController extends BaseController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public RestResponse<Boolean> saveOrUpdate(@RequestBody CategoryDto categoryDto) {
        return RestResponse.of(categoryService.saveOrUpdate(categoryDto));
    }

//    @PutMapping
//    public RestResponse<Boolean> sync() {
//
//    }

}
