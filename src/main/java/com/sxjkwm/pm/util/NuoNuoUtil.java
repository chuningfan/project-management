package com.sxjkwm.pm.util;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.business.finance.dto.InvoiceDetail;
import com.sxjkwm.pm.business.finance.dto.Order;
import com.sxjkwm.pm.constants.InvoiceError;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import nuonuo.open.sdk.NNOpenSDK;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * 诺诺发票系统
 *
 */
public class NuoNuoUtil {

    private static final String taxNum = "91610000220556561F";

    // 沙箱appKey
    private static final String clientId = "SD36584641";
    // 沙箱环境appSecret
    private static final String clientSecret = "SDF9EC25B2584197";

    private static final String method = "nuonuo.ElectronInvoice.requestBillingNew";

    private static final String url = "https://sandbox.nuonuocs.cn/open/v1/services";

    public static String getToken() throws PmException {
        String json = NNOpenSDK.getIntance().getMerchantToken(clientId, clientSecret);
        JSONObject resultJson = JSONObject.parseObject(json);
        String accessToken = resultJson.getString("access_token");
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        String error = resultJson.getString("error");
        throw new PmException(PmError.NUONUO_GET_TOKEN_FAILED, error);
    }

    public static String processInvoice(Order order) throws PmException {
        NNOpenSDK sdk = NNOpenSDK.getIntance();
        String invoiceCode = order.getInvoiceCode();
        if (StringUtils.isNotBlank(invoiceCode) && invoiceCode.length() == 11) {
            order.setInvoiceCode("0" + invoiceCode);
        }
        String invoiceNum = order.getInvoiceNum();
        if (StringUtils.isNotBlank(invoiceNum)) {
            int invoiceNumLength = invoiceNum.length();
            if (invoiceNumLength < 8) {
                int gap = 8 - invoiceNumLength;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < gap; i++) {
                    stringBuilder.append("0");
                }
                order.setInvoiceNum(stringBuilder + invoiceNum);
            }
        }
        JSONObject contentJson = new JSONObject();
        contentJson.put("order", order);
        String content = contentJson.toJSONString();
        String token = getToken();
        String senid = UUID.randomUUID().toString().replace("-", "");
        String result = sdk.sendPostSyncRequest(url, senid, clientId, clientSecret, token, taxNum, method, content);
        JSONObject resultJson = JSONObject.parseObject(result);
        String code = resultJson.getString("code");
        if (InvoiceError.E0000.getValue().equals(code)) {
            return resultJson.getString("invoiceSerialNum");
        } else {
            try {
                throw new PmException(InvoiceError.valueOf(code));
            } catch (Exception e) {
                throw new PmException(code + ": " +resultJson.getString("describe"));
            }
        }
    }

//    public static void main(String[] args) throws PmException {
//        Order order = new Order();
//        order.setBuyerName("陕西高速电子工程有限公司");
//        order.setBuyerTaxNum("91610000745002334M");
//        order.setBuyerTel("029-87832662");
//        order.setBuyerAddress("西安市友谊东路428号605室");
//        order.setBuyerAccount("上海浦东发展银行西安分行营业部 72010154500000897");
//        order.setSalerTaxNum("91610000220556561F");
//        order.setSalerTel("029-83691018");
//        order.setSalerAddress("陕西省西安市碑林区长安北路14号西安国际奥林匹克中心广场B座");
//        order.setInvoiceDate("2016-01-13 12:30:00");
//        order.setChecker("张三");
//        order.setClerk("李四");
//        order.setOrderNo("20170105120207971597");
//        List<InvoiceDetail> invoiceDetailList = Lists.newArrayList();
//        InvoiceDetail invoiceDetail = new InvoiceDetail();
//        invoiceDetailList.add(invoiceDetail);
//        invoiceDetail.setGoodsName("广场广播系统主机");
//        invoiceDetail.setGoodsCode("1090505990000000000");
//        invoiceDetail.setPrice("17522.123894");
//        invoiceDetail.setNum("1");
//        invoiceDetail.setUnit("台");
//        invoiceDetail.setSpecType("HSD-ZK12H");
//        invoiceDetail.setTax("2277.88");
//        invoiceDetail.setTaxRate("0.13");
//        order.setInvoiceDetail(invoiceDetailList);
//        processInvoice(order);
//    }


}
