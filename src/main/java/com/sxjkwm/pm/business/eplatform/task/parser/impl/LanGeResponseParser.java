package com.sxjkwm.pm.business.eplatform.task.parser.impl;

import com.sxjkwm.pm.business.eplatform.task.parser.ResponseParser;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Vic.Chu
 * @date 2022/8/15 15:02
 */
@Component
public class LanGeResponseParser implements ResponseParser<BigDecimal> {
    @Override
    public BigDecimal parse(String responseJsonString) throws Exception {
        return null;
    }
}
