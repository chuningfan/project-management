package com.sxjkwm.pm.business.file.handler.impl;

import com.sxjkwm.pm.business.file.handler.PatternFileHandler;
import com.sxjkwm.pm.business.file.handler.replacement.BaseReplacement;
import com.sxjkwm.pm.exception.PmException;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/8/17 8:43
 *
 * 多标包 多供应商
 *
 */
public class MultipleBUYContractHandler implements PatternFileHandler {
    @Override
    public List<BaseReplacement> captureData(Long dataId, Long flowId, Long flowNodeId, Long propertyDefId) throws PmException {
        return null;
    }
}
