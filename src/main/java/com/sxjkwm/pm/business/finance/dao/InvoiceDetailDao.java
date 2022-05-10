package com.sxjkwm.pm.business.finance.dao;

import com.sxjkwm.pm.business.finance.entity.InvoiceDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDetailDao extends JpaRepository<InvoiceDetailEntity, Long> {
}
