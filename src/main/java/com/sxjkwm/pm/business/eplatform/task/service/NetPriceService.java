package com.sxjkwm.pm.business.eplatform.task.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.hp.hpl.sparta.Parser;
import com.sxjkwm.pm.business.eplatform.task.dao.NetPriceDao;
import com.sxjkwm.pm.business.eplatform.task.dao.TaskConfigDao;
import com.sxjkwm.pm.business.eplatform.task.entity.TaskConfig;
import com.sxjkwm.pm.business.eplatform.task.parser.ResponseParser;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.ContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * @author Vic.Chu
 * @date 2022/8/15 14:23
 */
@Service
public class NetPriceService {

    private final TaskConfigDao taskConfigDao;

    private final NetPriceDao netPriceDao;

    @Autowired
    public NetPriceService(TaskConfigDao taskConfigDao, NetPriceDao netPriceDao) {
        this.taskConfigDao = taskConfigDao;
        this.netPriceDao = netPriceDao;
    }

    public BigDecimal getCurrentPrice(TaskConfig taskConfig) throws Exception {
        String methodType = taskConfig.getMethodType();
        if (StringUtils.isBlank(methodType)) {
            throw new PmException(PmError.UNKNOWN_HTTP_METHOD_TYPE);
        }
        methodType = methodType.trim().toLowerCase(Locale.ROOT);
        String url = taskConfig.getNetPriceUrl();
        String paramJsonString = taskConfig.getParams();
        String parserBeanName = taskConfig.getParserBeanName();
        int reqTimeout = 30 * 1000;
        switch (methodType) {
            case "post": return post(url, paramJsonString, parserBeanName, reqTimeout);
            default: return get(url, paramJsonString, parserBeanName, reqTimeout);
        }
    }

    public BigDecimal post(String url, String paramJsonString, String parserBeanName, int timeout) throws Exception {
        String responseString = HttpUtil.post(url, paramJsonString, timeout);
        // parse response
        ResponseParser<BigDecimal> parser = ContextUtil.getBean(parserBeanName);
        return parser.parse(responseString);
    }

    public BigDecimal get(String url, String paramJsonString, String parserBeanName, int timeout) throws Exception {
        JSONObject paramMap = JSONObject.parseObject(paramJsonString);
        String responseString = HttpUtil.get(url, paramMap, timeout);
        ResponseParser<BigDecimal> parser = ContextUtil.getBean(parserBeanName);
        return parser.parse(responseString);
    }

}
