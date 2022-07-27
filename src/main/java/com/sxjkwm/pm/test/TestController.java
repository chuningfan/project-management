package com.sxjkwm.pm.test;

import com.sxjkwm.pm.constants.Constant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vic.Chu
 * @date 2022/7/26 16:40
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/test")
public class TestController {

    @GetMapping
    public String doTest() {
        return "Hello world!";
    }

}
