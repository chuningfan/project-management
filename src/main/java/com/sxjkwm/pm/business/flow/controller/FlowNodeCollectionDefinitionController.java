package com.sxjkwm.pm.business.flow.controller;

import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionDefDto;
import com.sxjkwm.pm.business.flow.entity.FlowNodeCollectionDefinition;
import com.sxjkwm.pm.business.flow.service.FlowNodeCollectionDefinitionService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/25 10:28
 */
@RestController
@RequestMapping("/collectionDefinition")
public class FlowNodeCollectionDefinitionController {


    private final FlowNodeCollectionDefinitionService flowNodeCollectionDefinitionService;

    @Autowired
    public FlowNodeCollectionDefinitionController(FlowNodeCollectionDefinitionService flowNodeCollectionDefinitionService) {
        this.flowNodeCollectionDefinitionService = flowNodeCollectionDefinitionService;
    }

    @PostMapping("/{flowNodeId}/{collectionPropKey}")
    public RestResponse<List<FlowNodeCollectionDefinition>> saveOrUpdate(@PathVariable("flowNodeId") Long flowNodeId,
                                                                   @PathVariable("collectionPropKey") String collectionPropKey,
                                                                   @RequestBody List<FlowNodeCollectionDefDto> dtoList) throws SQLException {
        return RestResponse.of(flowNodeCollectionDefinitionService.saveOrUpdate(flowNodeId, collectionPropKey, dtoList));
    }

}
