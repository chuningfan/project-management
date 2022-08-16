package com.sxjkwm.pm.business.eplatform.task.sync.impl;

import com.sxjkwm.pm.business.eplatform.task.sync.Synchronizer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Vic.Chu
 * @date 2022/8/15 15:16
 */
@Component
public class LanGeSynchronizer implements Synchronizer {
    @Override
    public void syncToDB(BigDecimal currentPrice) {

    }

    @Override
    public BigDecimal fetchPrevious() {
        return null;
    }

}
