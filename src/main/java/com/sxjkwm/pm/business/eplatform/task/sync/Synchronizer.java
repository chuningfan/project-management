package com.sxjkwm.pm.business.eplatform.task.sync;

import java.math.BigDecimal;

/**
 * @author Vic.Chu
 * @date 2022/8/15 15:14
 */
public interface Synchronizer {

    void syncToDB(BigDecimal currentPrice);

    BigDecimal fetchPrevious();

}
