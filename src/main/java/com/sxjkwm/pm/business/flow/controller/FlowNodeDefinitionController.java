package com.sxjkwm.pm.business.flow.controller;

import com.sxjkwm.pm.business.flow.dto.FlowNodeDefinitionDto;
import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.business.flow.dto.PropertyTypeDto;
import com.sxjkwm.pm.business.flow.entity.Flow;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.business.flow.service.FlowNodeDefinitionService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    public RestResponse<List<FlowNodeDefinitionDto>> createDefinitions(@PathVariable("flowNodeId") Long flowNodeId, @RequestBody FlowNodeDefinitionDto flowNodeDefinitionDto) {
        List<FlowNodeDefinitionDto> flowNodeDefinitionDtos = Lists.newArrayList();
        flowNodeDefinitionDtos.add(flowNodeDefinitionDto);
        return RestResponse.of(flowNodeDefinitionService.create(flowNodeId, flowNodeDefinitionDtos));
    }

    @PutMapping("/{flowNodeId}")
    public RestResponse<List<FlowNodeDefinitionDto>> updateDefinitions(@PathVariable("flowNodeId") Long flowNodeId, @RequestBody FlowNodeDefinitionDto flowNodeDefinitionDto) {
        List<FlowNodeDefinitionDto> flowNodeDefinitionDtos = Lists.newArrayList();
        flowNodeDefinitionDtos.add(flowNodeDefinitionDto);
        return RestResponse.of(flowNodeDefinitionService.update(flowNodeId, flowNodeDefinitionDtos));
    }

    @GetMapping(value = "/{flowNodeId}")
    public RestResponse<List<FlowNodeDefinition>> getFlowList( @PathVariable("flowNodeId") Long flowNodeId) {
        return RestResponse.of(flowNodeDefinitionService.getFlowNodeDefinitionList(flowNodeId));
    }

    @DeleteMapping("/{id}")
    public RestResponse<Object> removeFlow(@PathVariable("id") Long id) {
        try {
            if (Objects.isNull(id)) {
                return new RestResponse<>().setCode("500").setMessage("流程ID不能为空");
            }
            int count = flowNodeDefinitionService.remove(id);
            if (count > 0) {
                return new RestResponse<>().setCode("200").setMessage("删除成功");
            } else {
                return new RestResponse<>().setCode("500").setMessage("删除失败");
            }
        } catch (Exception e) {
            return new RestResponse<>().setCode("500").setMessage("删除失败");
        }
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
