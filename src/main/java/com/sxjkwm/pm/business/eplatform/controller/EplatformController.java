package com.sxjkwm.pm.business.eplatform.controller;

import com.sxjkwm.pm.business.eplatform.dto.InboundInvoiceDto;
import com.sxjkwm.pm.business.eplatform.dto.OutboundInvoiceDto;
import com.sxjkwm.pm.business.eplatform.dto.OutboundQueryParam;
import com.sxjkwm.pm.business.eplatform.service.InboundInvoiceService;
import com.sxjkwm.pm.business.eplatform.service.OutboundInvoiceService;
import com.sxjkwm.pm.common.PageDataDto;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import io.minio.errors.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    @PostMapping("/invoiceList")
    public RestResponse<PageDataDto<Map<String, Object>>> queryInvoicePrintedList(@RequestBody OutboundQueryParam outboundQueryParam) throws IOException {
        String supplierNames = outboundQueryParam.getSupplierNames();
        String[] sNames = null;
        if (StringUtils.isNotBlank(supplierNames)) {
            sNames = supplierNames.split(",");
        }

        String buyerOrgs = outboundQueryParam.getBuyerOrgs();
        String[] oNames = null;
        if (StringUtils.isNotBlank(buyerOrgs)) {
            oNames = buyerOrgs.split(",");
        }
        return RestResponse.of(outboundInvoiceService.queryPrintedInvoiceInEs(outboundQueryParam.getPageSize(), outboundQueryParam.getPageNo(),
                outboundQueryParam.getStartTime(), outboundQueryParam.getEndTime(), oNames, outboundQueryParam.getInvoiceTitle(),
                outboundQueryParam.getInvoiceApplyNum(), sNames));
    }

    @GetMapping("/es/outboundSyncInvoicePrinted")
    public RestResponse<Boolean> syncInvoicePrintedData() {
        return RestResponse.of(outboundInvoiceService.syncInvoicePrintedListInEs());
    }

    @PostMapping("/invoiceBill")
    public RestResponse<String> generateInvoiceBill(@RequestBody List<OutboundInvoiceDto> dataList, @RequestParam(value = "mergeData", required = false) Boolean mergeData) throws NoSuchFieldException, IllegalAccessException {
        if (Objects.isNull(mergeData)) {
            mergeData = false;
        }
        return RestResponse.of(outboundInvoiceService.exportInvoiceBill(dataList, mergeData));
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
            @RequestParam(value = "supplierNames", required = false) String supplierNames,
            @RequestParam(value = "buyInvoiceApplyNumber", required = false) String buyInvoiceApplyNumber
            ) throws IOException {
        String[] sNames = null;
        if (StringUtils.isNotBlank(supplierNames)) {
            sNames = supplierNames.split(",");
        }
        return RestResponse.of(inboundInvoiceService.queryDataInEs(pageSize, pageNo, startTime, endTime, sNames, buyInvoiceApplyNumber));
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
