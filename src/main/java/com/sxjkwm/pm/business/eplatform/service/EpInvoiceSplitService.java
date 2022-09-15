package com.sxjkwm.pm.business.eplatform.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.eplatform.dao.EpDao;
import com.sxjkwm.pm.business.eplatform.dao.InvoiceInfoDao;
import com.sxjkwm.pm.business.eplatform.dto.InvoiceDto;
import com.sxjkwm.pm.business.eplatform.dto.InvoiceInfoDto;
import com.sxjkwm.pm.business.eplatform.entity.InvoiceInfoEntity;
import com.sxjkwm.pm.business.eplatform.vo.InvoiceVo;
import com.sxjkwm.pm.business.eplatform.vo.SupplierInfoVo;
import com.sxjkwm.pm.common.PageDataDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.*;
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
    private final InvoiceInfoDao invoiceInfoDao;


    public EpInvoiceSplitService(EpDao epDao, InvoiceInfoDao invoiceInfoDao) {
        this.epDao = epDao;
        this.invoiceInfoDao = invoiceInfoDao;
    }

    @Async
    @Scheduled(cron = "0 0 23 * * ?")
    public void syncInvoiceInfo() {
        List<InvoiceInfoEntity> invoiceInfoDtoList = epDao.query(EpSql.invoiceSql, new BeanPropertyRowMapper(InvoiceInfoEntity.class));
        List<InvoiceInfoEntity> list = invoiceInfoDao.findAll();
        HashSet<InvoiceInfoEntity> h1 = new HashSet(invoiceInfoDtoList);
        HashSet<InvoiceInfoEntity> h2 = new HashSet(list);
        HashSet<InvoiceInfoEntity> h3 = new HashSet(list);
        List<InvoiceInfoEntity> list1 = h1.stream().filter(h2::contains).collect(Collectors.toList());
        if (!list1.isEmpty()){
            h3.removeAll(list1);
            List<Long> ids = Lists.newArrayList();
            h3.forEach(entity -> {
                ids.add(entity.getId());
            });
            // 删除ids
            invoiceInfoDao.deleteBatch(ids);
        }
        if (!list.isEmpty()) {
            int count1 = invoiceInfoDtoList.size();
            int count2 = list.size();
            if (count1 > count2) {
                h1.removeAll(h2);
            } else {
                h2.removeAll(h1);
            }
        }
        List<InvoiceInfoEntity> entities = Lists.newArrayList();
        entities.addAll(h1);
        Iterable<InvoiceInfoEntity> iterable = iterableReverseList(entities);
        invoiceInfoDao.saveAll(iterable);
    }

    public Iterable<InvoiceInfoEntity> iterableReverseList(List<InvoiceInfoEntity> l) {
        return () -> new Iterator<InvoiceInfoEntity>() {
            ListIterator<InvoiceInfoEntity> listIter = l.listIterator(l.size());

            public boolean hasNext() {
                return listIter.hasPrevious();
            }
            public InvoiceInfoEntity next() {
                return listIter.previous();
            }
            public void remove() {
                listIter.remove();
            }
        };
    }

    public PageDataDto<List<InvoiceVo>> getInvoice(InvoiceDto invoiceDto) throws Exception {
        Pageable pageable = PageRequest.of(invoiceDto.getPageNum() - 1, invoiceDto.getPageSize());
        InvoiceInfoEntity invoiceInfoEntity1 = new InvoiceInfoEntity();
        BeanUtils.copyProperties(invoiceDto, invoiceInfoEntity1);
        Specification<InvoiceInfoEntity> spec = getSpecification(invoiceInfoEntity1);
        Page<InvoiceInfoEntity> entities = invoiceInfoDao.findAll(spec, pageable);
        List<InvoiceInfoDto> list = Lists.newArrayList();
        List<InvoiceInfoEntity> entityList = entities.getContent();
        entityList.forEach(entity -> {
            InvoiceInfoDto dto = new InvoiceInfoDto();
            BeanUtils.copyProperties(entity, dto);
            list.add(dto);
        });
        Map<String, List<InvoiceInfoDto>> collect = list.stream().collect(Collectors.groupingBy(InvoiceInfoDto::getApplyNumber));
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject jsonObject = JSONObject.parseObject(objectMapper.writeValueAsString(collect));
        List<InvoiceVo> invoiceVoList = Lists.newArrayList();
        jsonObject.forEach((key, value) -> {
            InvoiceVo invoiceVo = new InvoiceVo();
            invoiceVo.setInvoiceCode(key);
            List<InvoiceInfoDto> list1 = JSON.parseArray(JSONObject.toJSONString(value), InvoiceInfoDto.class);
            list1.forEach(invoice -> {
                invoiceVo.setInvoiceAmount(invoice.getInvoiceAmount());
                invoiceVo.setInvoiceTitle(invoice.getInvoiceTitle());
                invoiceVo.setBuyerOrg(invoice.getOrganizeName());
            });
            List<SupplierInfoVo> supplierInfoVos = list1.stream().map(infoDto -> new SupplierInfoVo(infoDto.getId(), infoDto.getOrderNo(), infoDto.getShopName(), infoDto.getPaymentPrice(), infoDto.getPayStatus())).collect(Collectors.toList());
            invoiceVo.setSupplierInfo(supplierInfoVos);
            invoiceVoList.add(invoiceVo);
        });
        PageDataDto pageDataDto = new PageDataDto();
        pageDataDto.setContent(invoiceVoList);
        pageDataDto.setTotal(entities.getTotalElements());
        pageDataDto.setCurrentPageSize(entities.getSize());
        return pageDataDto;
    }

    private Specification<InvoiceInfoEntity> getSpecification(InvoiceInfoEntity entity) {
        Specification<InvoiceInfoEntity> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 判断条件不为空
            if (StringUtils.isNotEmpty(entity.getApplyNumber())) {
                predicates.add(criteriaBuilder.equal(root.get("applyNumber"), entity.getApplyNumber()));
            }
            if (StringUtils.isNotEmpty(entity.getShopName())) {
                predicates.add(criteriaBuilder.equal(root.get("shopName"), entity.getShopName()));
            }
            if (null != entity.getInvoiceAmount()) {
                predicates.add(criteriaBuilder.equal(root.get("invoiceAmount"), entity.getInvoiceAmount()));
            }
            if (StringUtils.isNotEmpty(entity.getInvoiceTitle())) {
                predicates.add(criteriaBuilder.equal(root.get("invoiceTitle"), entity.getInvoiceTitle()));
            }
            if (StringUtils.isNotEmpty(entity.getOrganizeName())) {
                predicates.add(criteriaBuilder.equal(root.get("organizeName"), entity.getOrganizeName()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        return specification;
    }

    @Transactional
    public int invoiceUpdate(Long id, Integer payStatus) {
        int count = invoiceInfoDao.invoiceUpdate(payStatus, id);
        return count;
    }
}
