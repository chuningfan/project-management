package com.sxjkwm.pm.business.flow.dao;

import com.sxjkwm.pm.business.flow.entity.FlowNodeCollectionDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/25 7:30
 */
public interface FlowNodeCollectionDefinitionDao extends JpaRepository<FlowNodeCollectionDefinition, Long> {

    @Query(value = "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_NAME = ?1", nativeQuery = true)
    Integer tryCreateTable(String tableName);

    @Query("SELECT fc FROM FlowNodeCollectionDefinition fc WHERE isDeleted = 0 AND fc.collectionPropertyDefId IN (?1)")
    List<FlowNodeCollectionDefinition> findByPropDefIds(List<Long> flowNodePropertyDefIds);

    @Query("SELECT fc FROM FlowNodeCollectionDefinition fc WHERE isDeleted = 0 AND fc.id IN (?1)")
    List<FlowNodeCollectionDefinition> findByIds(List<Long> ids);


}
