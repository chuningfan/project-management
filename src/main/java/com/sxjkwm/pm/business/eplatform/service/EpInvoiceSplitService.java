package com.sxjkwm.pm.business.eplatform.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.eplatform.dao.EpDao;
import com.sxjkwm.pm.business.eplatform.dto.InvoiceDto;
import com.sxjkwm.pm.business.eplatform.dto.InvoiceInfoDto;
import com.sxjkwm.pm.business.eplatform.vo.InvoiceVo;
import com.sxjkwm.pm.business.eplatform.vo.SupplierInfoVo;
import com.sxjkwm.pm.common.PageDataDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName EpInvoiceSplitService
 * @Description
 * @Author wubin
 * @Date 2022/8/26 10:53
 * @Version 1.0
 **/
@Service
public class EpInvoiceSplitService {

    private final EpDao epDao;


    public EpInvoiceSplitService(EpDao epDao) {
        this.epDao = epDao;
    }

    public PageDataDto<List<InvoiceVo>> getInvoice(InvoiceDto invoiceDto) throws Exception {
        List<InvoiceInfoDto> list = epDao.queryInvoiceList(EpSql.invoiceSql,invoiceDto);
        Map<String, List<InvoiceInfoDto>> collect = list.stream().collect(Collectors.groupingBy(InvoiceInfoDto::getApplyNumber));
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject jsonObject = JSONObject.parseObject(objectMapper.writeValueAsString(collect));
        List<InvoiceVo> invoiceVoList = Lists.newArrayList();
        jsonObject.forEach((key, value) -> {
            InvoiceVo invoiceVo = new InvoiceVo();
            invoiceVo.setInvoiceCode(key);
            List<InvoiceInfoDto> list1 = JSON.parseArray(JSONObject.toJSONString(value), InvoiceInfoDto.class);
            list1.forEach(invoice->{
                invoiceVo.setInvoiceAmount(invoice.getInvoiceAmount());
            });
            List<SupplierInfoVo> supplierInfoVos = list1.stream().map(infoDto -> new SupplierInfoVo(infoDto.getShopName(), infoDto.getPaymentPrice(),infoDto.getOrderNo())).collect(Collectors.toList());
            invoiceVo.setSupplierInfo(supplierInfoVos);
            invoiceVoList.add(invoiceVo);
        });

        PageDataDto pageDataDto = new PageDataDto();
        pageDataDto.setContent(invoiceVoList);
        pageDataDto.setCurrentPageNo(invoiceDto.getPageNum());
        pageDataDto.setCurrentPageSize(invoiceDto.getPageSize());
        List<InvoiceInfoDto> infoDtos = epDao.queryInvoiceCount(EpSql.invoiceSql,invoiceDto);
        pageDataDto.setTotal(infoDtos.size());
        pageDataDto.setTotalPages(infoDtos.size()/invoiceDto.getPageSize() + ((infoDtos.size()%invoiceDto.getPageSize())!= 0 ? 1:0));
        return pageDataDto;
    }
}
