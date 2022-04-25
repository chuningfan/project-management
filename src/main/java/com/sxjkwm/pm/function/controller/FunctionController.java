package com.sxjkwm.pm.function.controller;

import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.function.entity.Function;
import com.sxjkwm.pm.function.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/function")
public class FunctionController {

    private final FunctionService functionService;

    @Autowired
    public FunctionController(FunctionService functionService) {
        this.functionService = functionService;
    }

    @GetMapping("/list")
    public RestResponse<List<Function>> findAll() {
        return RestResponse.of(functionService.findAll());
    }

    @DeleteMapping("/{id}")
    public RestResponse<Boolean> deleteById(@PathVariable("id") Long id) {
        return RestResponse.of(functionService.deleteById(id));
    }

    @PostMapping
    public RestResponse<Function> saveOrUpdate(@RequestBody Function function) {
        return RestResponse.of(functionService.saveOrUpdate(function));
    }

}
