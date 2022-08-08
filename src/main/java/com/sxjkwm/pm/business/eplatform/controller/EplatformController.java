package com.sxjkwm.pm.business.eplatform.controller;

import com.google.common.collect.Maps;
import com.sxjkwm.pm.business.eplatform.dto.BillCheckingDto;
import com.sxjkwm.pm.business.eplatform.dto.InvoicePrintingDto;
import com.sxjkwm.pm.business.eplatform.service.EplatformService;
import com.sxjkwm.pm.common.PageDataDto;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/7/25 10:08
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/ep")
public class EplatformController {

    private final EplatformService eplatformService;

    public EplatformController(EplatformService eplatformService) {
        this.eplatformService = eplatformService;
    }

    @GetMapping("/billCheckingList")
    public RestResponse<List<BillCheckingDto>> queryBillCheckingList(HttpServletRequest request) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, String> conditionMap = Maps.newHashMap();
        while (paramNames.hasMoreElements()) {
            String paramKey = paramNames.nextElement();
            conditionMap.put(paramKey, request.getParameter(paramKey));
        }
        return RestResponse.of(eplatformService.queryData(conditionMap));
    }

    @GetMapping("/es/syncData")
    public RestResponse<Boolean> syncData() {
        return RestResponse.of(eplatformService.syncToES());
    }

    @GetMapping("/es/query")
    public RestResponse<List<Map<String, Object>>> queryBillCheckingListInEs(HttpServletRequest request, @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestParam(value = "pageNo", required = false) Integer pageNo) throws IOException {
        if (Objects.isNull(pageNo)) {
            pageNo = 1;
        }
        if (Objects.isNull(pageSize)) {
            pageSize = 50;
        }
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, String> conditionMap = Maps.newHashMap();
        while (paramNames.hasMoreElements()) {
            String paramKey = paramNames.nextElement();
            if ("pageSize".equals(paramKey) || "pageNo".equals(paramKey)) {
                continue;
            }
            conditionMap.put(paramKey, request.getParameter(paramKey));
        }
        return RestResponse.of(eplatformService.queryInEs(conditionMap, pageSize, pageNo));
    }

    /**
     * 查询已打印发票的数据
     * @return
     */
    @GetMapping("/invoiceList")
    public RestResponse<PageDataDto<Map<String, Object>>> queryInvoicePrintedList(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("startTime") Long startTime,
            @RequestParam("endTime") Long endTime,
            @RequestParam(value = "buyerOrg", required = false) String buyerOrg,
            @RequestParam(value = "invoiceTitle", required = false) String invoiceTitle,
            @RequestParam(value = "invoiceApplyNum", required = false) String invoiceApplyNum) throws IOException {
        return RestResponse.of(eplatformService.queryPrintedInvoiceInEs(pageSize, pageNo, startTime, endTime, buyerOrg, invoiceTitle, invoiceApplyNum));
    }

    @GetMapping("/es/syncInvoicePrinted")
    public RestResponse<Boolean> syncInvoicePrintedData() throws IOException {
        eplatformService.syncInvoicePrintedListInEs();
        return RestResponse.of(Boolean.TRUE);
    }

    @PostMapping("/invoiceBill")
    public RestResponse<String> generateInvoiceBill(@RequestBody List<InvoicePrintingDto> dataList) throws NoSuchFieldException, IllegalAccessException {
        return RestResponse.of(eplatformService.exportInvoiceBill(dataList));
    }

    @GetMapping("/invoiceBill")
    public void downloadInvoiceBill(@RequestParam("objName") String objName, HttpServletResponse response) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        eplatformService.downloadInvoiceBill(objName, response);
    }

}
