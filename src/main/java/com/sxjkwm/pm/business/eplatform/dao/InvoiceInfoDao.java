package com.sxjkwm.pm.business.eplatform.dao;

import com.sxjkwm.pm.business.eplatform.entity.InvoiceInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @ClassName EpInvoiceDao
 * @Description
 * @Author wubin
 * @Date 2022/9/2 10:02
 * @Version 1.0
 **/
@Repository
public interface InvoiceInfoDao extends JpaRepository<InvoiceInfoEntity, Long>,JpaSpecificationExecutor<InvoiceInfoEntity> {

    @Modifying
    @Query(value = "update InvoiceInfoEntity f set f.payStatus = :payStatus where f.id = :id")
    int invoiceUpdate(Integer payStatus,Long id);



    @Modifying
    @Transactional
    @Query("delete from InvoiceInfoEntity s where s.id in (?1)")
    void deleteBatch(List ids);



}
