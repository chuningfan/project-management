package com.sxjkwm.pm.business.flow.controller;

import com.sxjkwm.pm.business.flow.dto.FlowNodeDefinitionDto;
import com.sxjkwm.pm.business.flow.dto.PropertyTypeDto;
import com.sxjkwm.pm.business.flow.service.FlowNodeDefinitionService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/flowNodeDefinitions")
public class FlowNodeDefinitionController {

    private final FlowNodeDefinitionService flowNodeDefinitionService;

    @Autowired
    public FlowNodeDefinitionController(FlowNodeDefinitionService flowNodeDefinitionService) {
        this.flowNodeDefinitionService = flowNodeDefinitionService;
    }

    @PostMapping("/{flowNodeId}")
    public RestResponse<FlowNodeDefinitionDto> saveOrUpdate(@PathVariable("flowNodeId") Long flowNodeId, @RequestBody FlowNodeDefinitionDto flowNodeDefinitionDto) throws SQLException, PmException {
        return RestResponse.of(flowNodeDefinitionService.saveOrUpdate(flowNodeId, flowNodeDefinitionDto));
    }

    @GetMapping(value = "/{flowNodeId}")
    public RestResponse<List<FlowNodeDefinitionDto>> getFlowList( @PathVariable("flowNodeId") Long flowNodeId) {
        return RestResponse.of(flowNodeDefinitionService.getFlowNodeDefinitionDtoList(flowNodeId));
    }

    @DeleteMapping("/{id}")
    public RestResponse<Integer> removeFlow(@PathVariable("id") Long id) throws PmException {
        if (Objects.isNull(id)) {
            throw new PmException(PmError.ILLEGAL_PARAMETER);
        }
        int count = flowNodeDefinitionService.remove(id);
        if (count == 0) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        return RestResponse.of(count);
    }

    @PostMapping("/sort")
    public RestResponse<Boolean> sort(@RequestBody List<Long> ids) {
        return RestResponse.of(flowNodeDefinitionService.sort(ids));
    }

    @GetMapping("/propTypes")
    public RestResponse<List<PropertyTypeDto>> findTypes() {
        Constant.PropertyType[] types = Constant.PropertyType.values();
        List<PropertyTypeDto> dataList = Lists.newArrayList();
        PropertyTypeDto propertyTypeDto;
        for (Constant.PropertyType type: types) {
            propertyTypeDto = new PropertyTypeDto();
            propertyTypeDto.setValue(type.name());
            propertyTypeDto.setLabel(type.getLabel());
            dataList.add(propertyTypeDto);
        }
        return RestResponse.of(dataList);
    }
}
