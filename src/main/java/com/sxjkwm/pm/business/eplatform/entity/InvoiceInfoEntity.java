package com.sxjkwm.pm.business.eplatform.entity;



import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @ClassName InvoiceInfoEntity
 * @Description
 * @Author wubin
 * @Date 2022/8/29 9:32
 * @Version 1.0
 **/

@Table(name = "ep_invoice_info")
@Entity
public class InvoiceInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Column(name = "invoice_no")
    protected String invoiceNo;
    @Column(name = "invoice_amount")
    protected BigDecimal invoiceAmount;
    @Column(name = "apply_number")
    protected String applyNumber;
    @Column(name = "tatal_amount")
    protected BigDecimal totalAmount;
    @Column(name = "order_no")
    protected String orderNo;
    @Column(name = "payment_price")
    protected BigDecimal paymentPrice;
    @Column(name = "total_price")
    protected BigDecimal totalPrice;
    @Column(name = "shop_name")
    protected String shopName;
    @Column(name = "invoice_title")
    protected String invoiceTitle;
    @Column(name = "organize_id")
    protected String organizeId;
    @Column(name = "organize_name")
    protected String organizeName;




    public InvoiceInfoEntity(Long id, String invoiceNo, BigDecimal invoiceAmount, String applyNumber, BigDecimal totalAmount, String orderNo, BigDecimal paymentPrice, BigDecimal totalPrice, String shopName, String invoiceTitle, String organizeId, String organizeName) {
        this.id = id;
        this.invoiceNo = invoiceNo;
        this.invoiceAmount = invoiceAmount;
        this.applyNumber = applyNumber;
        this.totalAmount = totalAmount;
        this.orderNo = orderNo;
        this.paymentPrice = paymentPrice;
        this.totalPrice = totalPrice;
        this.shopName = shopName;
        this.invoiceTitle = invoiceTitle;
        this.organizeId = organizeId;
        this.organizeName = organizeName;
    }

    public InvoiceInfoEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getApplyNumber() {
        return applyNumber;
    }

    public void setApplyNumber(String applyNumber) {
        this.applyNumber = applyNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getPaymentPrice() {
        return paymentPrice;
    }

    public void setPaymentPrice(BigDecimal paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getOrganizeId() {
        return organizeId;
    }

    public void setOrganizeId(String organizeId) {
        this.organizeId = organizeId;
    }

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }
}
