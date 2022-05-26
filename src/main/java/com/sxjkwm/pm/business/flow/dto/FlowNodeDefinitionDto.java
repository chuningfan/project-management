package com.sxjkwm.pm.business.flow.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;

@Setter
@Getter
public class FlowNodeDefinitionDto implements Serializable {

    private Long id;

    private Long flowNodeId;

    private String propertyKey;

    private String propertyName;

    private Integer propertyIndex;

    private String propertyType;

    private String collectionPropertyHandler;
}
