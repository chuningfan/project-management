package com.sxjkwm.pm.function.dao;

import com.sxjkwm.pm.function.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FunctionDao extends JpaRepository<Function, Long> {
}
