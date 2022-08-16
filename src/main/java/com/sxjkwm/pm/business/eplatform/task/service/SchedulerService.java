package com.sxjkwm.pm.business.eplatform.task.service;

import org.springframework.stereotype.Service;

/**
 * @author Vic.Chu
 * @date 2022/8/15 15:13
 */
@Service
public class SchedulerService {

    private final NetPriceService netPriceService;

    private final PriceChangingService priceChangingService;

    public SchedulerService(NetPriceService netPriceService, PriceChangingService priceChangingService) {
        this.netPriceService = netPriceService;
        this.priceChangingService = priceChangingService;
    }

    public void sync1() {
        
    }

}
