package com.sxjkwm.pm.business.flow.controller;

import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionDefDto;
import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionPropTypeDto;
import com.sxjkwm.pm.business.flow.entity.FlowNodeCollectionDefinition;
import com.sxjkwm.pm.business.flow.service.FlowNodeCollectionDefinitionService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/25 10:28
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/collectionDefinition")
public class FlowNodeCollectionDefinitionController {


    private final FlowNodeCollectionDefinitionService flowNodeCollectionDefinitionService;

    @Autowired
    public FlowNodeCollectionDefinitionController(FlowNodeCollectionDefinitionService flowNodeCollectionDefinitionService) {
        this.flowNodeCollectionDefinitionService = flowNodeCollectionDefinitionService;
    }

    @PostMapping("/{flowNodeId}/{collectionPropDefId}")
    public RestResponse<List<FlowNodeCollectionDefinition>> saveOrUpdate(@PathVariable("flowNodeId") Long flowNodeId,
                                                                   @PathVariable("collectionPropDefId") Long collectionPropDefId,
                                                                   @RequestBody List<FlowNodeCollectionDefDto> dtoList) throws SQLException {
        return RestResponse.of(flowNodeCollectionDefinitionService.saveOrUpdate(flowNodeId, collectionPropDefId, dtoList));
    }

    @GetMapping("/{flowNodeId}/{collectionPropDefId}")
    public RestResponse<List<FlowNodeCollectionDefDto>> findAll(@PathVariable("flowNodeId") Long flowNodeId,
                                                                 @PathVariable("collectionPropDefId") Long collectionPropDefId) {
        return RestResponse.of(flowNodeCollectionDefinitionService.findByFlowNodeIdAndCollectionDefId(flowNodeId, collectionPropDefId));
    }

    @PostMapping(value = "/sort")
    public RestResponse<Boolean> sort(@RequestBody List<Long> nodeIds) {
        return RestResponse.of(flowNodeCollectionDefinitionService.sort(nodeIds));
    }

    @GetMapping("/collectionPropTypes")
    public RestResponse<List<FlowNodeCollectionPropTypeDto>> getCollectionPropTypes() {
        return RestResponse.of(flowNodeCollectionDefinitionService.getCollectionPropTypes());
    }

}
