package com.sxjkwm.pm.function.dao;

import com.sxjkwm.pm.function.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FunctionDao extends JpaRepository<Function, Long> {

    @Query("SELECT f FROM Function f WHERE f.isDeleted = 0 AND f.id IN (?1)")
    public List<Function> findByIds(List<Long> ids);

}
