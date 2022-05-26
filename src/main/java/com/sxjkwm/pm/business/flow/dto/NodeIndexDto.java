package com.sxjkwm.pm.business.flow.dto;

import java.io.Serializable;

public class NodeIndexDto implements Serializable {
    private Long id;
    private int nodeIndex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }
}
