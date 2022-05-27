package com.sxjkwm.pm.business.flow.dao;

import com.sxjkwm.pm.business.flow.entity.Flow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlowDao extends JpaRepository<Flow, Long> {

    List<Flow> findByIsDeleted(Integer isDelete);

    @Modifying
    @Query(value = "update Flow f set f.isDeleted = :isDelete where f.id = :id")
    int updateFlow(Integer isDelete, Long id);
}
