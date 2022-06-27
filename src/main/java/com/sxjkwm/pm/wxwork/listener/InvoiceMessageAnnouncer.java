package com.sxjkwm.pm.wxwork.listener;

import com.sxjkwm.pm.business.finance.dto.WxMessageDto;
import com.sxjkwm.pm.common.PmListener;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.WxWorkMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InvoiceMessageAnnouncer implements PmListener<WxMessageDto> {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceMessageAnnouncer.class);

    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(200);

    private static final int coreSize = Runtime.getRuntime().availableProcessors() << 1;

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
            String projectCode = event.getProjectCode();
            String msg = String.format(msgPattern, username, projectName, projectCode);
            try {
                WxWorkMessageUtil.sendWxWorkMsg(msg, userId);
            } catch (PmException e) {
            }
        });
    }

}
