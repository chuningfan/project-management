package com.sxjkwm.pm.business.flow.dao;

import com.sxjkwm.pm.business.flow.entity.Flow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FlowDao extends JpaRepository<Flow, Long> {

    Page<Flow> findByIsDeleted(Integer isDelete, Pageable pageable);

    Page<Flow> findByIsDeletedAndFlowNameLike(Integer isDelete, String flowName, Pageable pageable);

    @Modifying
    @Query(value = "update Flow f set f.isDeleted = :isDelete where f.id = :id")
    int updateFlow(Integer isDelete, Long id);
}
