package com.sxjkwm.pm.business.eplatform.controller;

import com.sxjkwm.pm.business.eplatform.dto.InvoiceDto;
import com.sxjkwm.pm.business.eplatform.service.EpInvoiceSplitService;
import com.sxjkwm.pm.business.eplatform.vo.InvoiceVo;
import com.sxjkwm.pm.common.PageDataDto;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName EpInvoiceSplitController
 * @Description
 * @Author wubin
 * @Date 2022/8/26 10:15
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = Constant.API_FEATURE + "/split")
public class EpInvoiceSplitController {
    private final EpInvoiceSplitService epInvoiceSplitService;

    public EpInvoiceSplitController(EpInvoiceSplitService epInvoiceSplitService) {
        this.epInvoiceSplitService = epInvoiceSplitService;
    }

    @GetMapping
    public RestResponse<PageDataDto<List<InvoiceVo>>> invoiceSplit(@RequestParam(value = "pageSize") Integer pageSize,
                                                                   @RequestParam(value = "pageNum") Integer pageNum,
                                                                   @RequestParam(value = "applyNumber", required = false) String applyNumber,
                                                                   @RequestParam(value = "supplierName", required = false) String supplierName,
                                                                   @RequestParam(value = "invoiceAmount", required = false) BigDecimal invoiceAmount,
                                                                   @RequestParam(value = "invoiceTitle", required = false) String invoiceTitle,
                                                                   @RequestParam(value = "buyerOrg", required = false) String buyerOrg) {
        try {
            InvoiceDto invoiceDto = new InvoiceDto();
            invoiceDto.setApplyNumber(applyNumber);
            invoiceDto.setOrganizeName(buyerOrg);
            invoiceDto.setInvoiceAmount(invoiceAmount);
            invoiceDto.setInvoiceTitle(invoiceTitle);
            invoiceDto.setShopName(supplierName);
            invoiceDto.setPageNum(pageNum);
            invoiceDto.setPageSize(pageSize);
            return RestResponse.of(epInvoiceSplitService.getInvoice(invoiceDto));
        } catch (Exception e) {
            return RestResponse.of(e);
        }
    }

    @PutMapping
    public RestResponse updateInvoice(@RequestParam(value = "id") Long id) throws PmException {
        Integer payStatus = 1;
        int count = epInvoiceSplitService.invoiceUpdate(id, payStatus);
        if (count == 0) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        return RestResponse.of(count);
    }

}
