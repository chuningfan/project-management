package com.sxjkwm.pm.wxwork.listener;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.business.finance.dto.WxMessageDto;
import com.sxjkwm.pm.common.PmListener;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.WxWorkTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InvoiceMessageAnnouncer implements PmListener<WxMessageDto> {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceMessageAnnouncer.class);

    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(200);

    private static final int coreSize = Runtime.getRuntime().availableProcessors() << 1;

    private static final String msgUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreSize, coreSize, 3, TimeUnit.SECONDS, queue);

    private static final String msgPattern = "%s你好，你所负责的%s（编号：%s）已完成开票，请速到财务部领取发票。";


    private InvoiceMessageAnnouncer() {
    }

    private static final class MessageAnnouncerHolder {
        private static final InvoiceMessageAnnouncer instance = new InvoiceMessageAnnouncer();
    }

    public static InvoiceMessageAnnouncer getInstance() {
        return MessageAnnouncerHolder.instance;
    }

    @Override
    public void onEvent(WxMessageDto event) {
        threadPoolExecutor.execute(() -> {
            String userId = event.getUserId();
            String username = event.getUsername();
            String projectName = event.getProjectName();
            String taskNum = event.getTaskNum();
            String msg = String.format(msgPattern, username, projectName, taskNum);
            try {
                sendWxWorkMsg(msg, userId);
            } catch (PmException e) {
            }
        });
    }

    private void sendWxWorkMsg(String msg, String userId) throws PmException {
        String url = String.format(msgUrl, WxWorkTokenUtil.getToken());
        JSONObject param = new JSONObject();
        param.put("touser", userId);
        param.put("msgtype", "text");
        param.put("agentid", WxWorkTokenUtil.agentId);
        param.put("safe", 0);
        param.put("enable_id_trans", 0);
        param.put("enable_duplicate_check", 0);
        param.put("duplicate_check_interval", 1800);
        JSONObject content = new JSONObject();
        content.put("content", msg);
        param.put("text", content);
        String res = HttpUtil.post(url, param.toJSONString());
        JSONObject resultJson = JSONObject.parseObject(res);
        Integer errorCode = resultJson.getInteger("errcode");
        if (Objects.nonNull(errorCode) && 0 != errorCode.intValue()) {
            throw new PmException(PmError.WXWORK_SEND_MSG_FAILED, resultJson.getString("errmsg"));
        }
    }

}
