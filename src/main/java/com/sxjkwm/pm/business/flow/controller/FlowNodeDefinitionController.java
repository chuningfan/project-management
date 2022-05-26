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
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/flowNodeDefinition")
public class FlowNodeDefinitionController {

    private final FlowNodeDefinitionService flowNodeDefinitionService;

    public FlowNodeDefinitionController(FlowNodeDefinitionService flowNodeDefinitionService) {
        this.flowNodeDefinitionService = flowNodeDefinitionService;
    }

    @PostMapping("/saveFlowNodeDefinition/{flowNodeId}")
    public RestResponse<List<FlowNodeDefinitionDto>> createDefinitions(@PathVariable("flowNodeId") Long flowNodeId, @RequestBody List<FlowNodeDefinitionDto> flowNodeDefinitionDtos) {
        return RestResponse.of(flowNodeDefinitionService.create(flowNodeId, flowNodeDefinitionDtos));
    }
//
//    @PutMapping("/updateFlowNodeDefinition/{flowNodeId}")
//    public RestResponse<List<FlowNodeDto>> updateDefinitions(@PathVariable("flowNodeId") Long flowNodeId, @RequestBody List<FlowNodeDefinitionDto> flowNodeDefinitionDtos) {
//        return RestResponse.of(flowNodeDefinitionService.update(flowNodeId, flowNodeDefinitionDtos));
//    }




    @PostMapping(value = "/getFlowNodeDefinitionList")
    public RestResponse<List<FlowNodeDefinition>> getFlowList( @RequestParam("flowNodeId") Long flowNodeId) {
        return RestResponse.of(flowNodeDefinitionService.getFlowNodeDefinitionList(flowNodeId));
    }

    @PostMapping(value = "/deleteNodeDefinition")
    public RestResponse<Object> removeFlow(@RequestBody Flow flow) {
        try {
            Long id = flow.getId();
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
