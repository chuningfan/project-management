package com.sxjkwm.pm.business.eplatform.task.service;

import com.sxjkwm.pm.business.eplatform.task.dao.PriceChangingDao;
import com.sxjkwm.pm.business.eplatform.task.entity.PriceChangingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Vic.Chu
 * @date 2022/8/15 15:05
 */
@Service
public class PriceChangingService {

    private final PriceChangingDao priceChangingDao;

    @Autowired
    public PriceChangingService(PriceChangingDao priceChangingDao) {
        this.priceChangingDao = priceChangingDao;
    }

    public void save(BigDecimal previousPrice, BigDecimal currentPrice, Long currentTime, String materialCategory) {
        PriceChangingRecord record = new PriceChangingRecord();
        record.setRecordTime(currentTime);
        record.setPreviousPrice(previousPrice);
        record.setTimedPrice(currentPrice);
        record.setMaterialCategory(materialCategory);
        priceChangingDao.save(record);
    }

}
