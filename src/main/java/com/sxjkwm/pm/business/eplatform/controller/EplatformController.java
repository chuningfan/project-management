package com.sxjkwm.pm.business.eplatform.controller;

import com.sxjkwm.pm.business.eplatform.dto.InboundInvoiceDto;
import com.sxjkwm.pm.business.eplatform.dto.OutboundInvoiceDto;
import com.sxjkwm.pm.business.eplatform.service.InboundInvoiceService;
import com.sxjkwm.pm.business.eplatform.service.OutboundInvoiceService;
import com.sxjkwm.pm.common.PageDataDto;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/7/25 10:08
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/ep")
public class EplatformController {

    private final OutboundInvoiceService outboundInvoiceService;

    private final InboundInvoiceService inboundInvoiceService;

    public EplatformController(OutboundInvoiceService outboundInvoiceService, InboundInvoiceService inboundInvoiceService) {
        this.outboundInvoiceService = outboundInvoiceService;
        this.inboundInvoiceService = inboundInvoiceService;
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
            @RequestParam(value = "invoiceApplyNum", required = false) String invoiceApplyNum,
            @RequestParam(value = "supplierName", required = false) String supplierName) throws IOException {
        return RestResponse.of(outboundInvoiceService.queryPrintedInvoiceInEs(pageSize, pageNo, startTime, endTime, buyerOrg, invoiceTitle, invoiceApplyNum, supplierName));
    }

    @GetMapping("/es/syncInvoicePrinted")
    public RestResponse<Boolean> syncInvoicePrintedData() {
        return RestResponse.of(outboundInvoiceService.syncInvoicePrintedListInEs());
    }

    @PostMapping("/invoiceBill")
    public RestResponse<String> generateInvoiceBill(@RequestBody List<OutboundInvoiceDto> dataList) throws NoSuchFieldException, IllegalAccessException {
        return RestResponse.of(outboundInvoiceService.exportInvoiceBill(dataList));
    }

    @GetMapping("/invoiceBill")
    public void downloadInvoiceBill(@RequestParam("objName") String objName, HttpServletResponse response) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        outboundInvoiceService.downloadInvoiceBill(objName, response);
    }


    /**
     * 查询进项发票的数据
     * @return
     */
    @GetMapping("/inboundInvoiceList")
    public RestResponse<PageDataDto<Map<String, Object>>> queryInboundInvoice(
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageNo") Integer pageNo,
            @RequestParam(value = "startTime", required = false) Long startTime,
            @RequestParam(value = "endTime", required = false) Long endTime,
            @RequestParam(value = "supplierName", required = false) String supplierName,
            @RequestParam(value = "buyInvoiceApplyNumber", required = false) String buyInvoiceApplyNumber
            ) throws IOException {
        return RestResponse.of(inboundInvoiceService.queryDataInEs(pageSize, pageNo, startTime, endTime, supplierName, buyInvoiceApplyNumber));
    }

    @GetMapping("/es/inboundSyncInvoicePrinted")
    public RestResponse<Boolean> syncInboundInvoice() {
        return RestResponse.of(inboundInvoiceService.syncData());
    }

    @PostMapping("/inboundInvoiceBill")
    public RestResponse<String> generateInboundInvoiceBill(@RequestBody List<InboundInvoiceDto> dataList) throws NoSuchFieldException, IllegalAccessException {
        return RestResponse.of(inboundInvoiceService.generateWorkbook(dataList));
    }

    @GetMapping("/inboundInvoiceBill")
    public void downloadInboundInvoiceBill(@RequestParam("objName") String objName, HttpServletResponse response) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        inboundInvoiceService.downloadInvoiceBill(objName, response);
    }

}
