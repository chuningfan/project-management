package com.sxjkwm.pm.business.file.handler.impl;

import com.sxjkwm.pm.business.file.handler.PatternFileHandler;
import com.sxjkwm.pm.business.file.handler.replacement.BaseReplacement;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/9/17 15:04
 */
@Component
public class AuditingReportHandler implements PatternFileHandler {
    @Override
    public List<BaseReplacement> captureData(Long dataId, Long flowId, Long flowNodeId, Long propertyDefId) throws PmException {

        return null;
    }

    @Override
    public boolean isSingleFile() {
        return true;
    }
}
