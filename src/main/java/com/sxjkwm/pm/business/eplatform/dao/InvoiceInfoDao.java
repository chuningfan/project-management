package com.sxjkwm.pm.business.eplatform.dao;

import com.sxjkwm.pm.business.eplatform.entity.InvoiceInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * @ClassName EpInvoiceDao
 * @Description
 * @Author wubin
 * @Date 2022/9/2 10:02
 * @Version 1.0
 **/
@Repository
public interface InvoiceInfoDao extends JpaRepository<InvoiceInfoEntity, Long>,JpaSpecificationExecutor<InvoiceInfoEntity> {

}
