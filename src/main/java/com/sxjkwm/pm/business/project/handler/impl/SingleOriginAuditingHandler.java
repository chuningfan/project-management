package com.sxjkwm.pm.business.project.handler.impl;

import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.business.project.handler.SpecialFormDataHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/9/22 8:46
 */
@Component("SingleOriginAuditingHandler")
public class SingleOriginAuditingHandler implements SpecialFormDataHandler {


    @Override
    public void doHandle(Map<String, Object> formDataMap) {
        formDataMap.put("foa650162829164e819a1b350090a58045", ContextHelper.getUserData().getDeptNames().get(0));
        formDataMap.put("foa06c0f3ecd7343139ca26fb7fdfea63f", ContextHelper.getUserData().getUsername());
        formDataMap.put("fo54a8df9b34c44cd196a396e614ce6464", "集采");
    }
}
