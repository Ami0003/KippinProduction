
package com.kippinretail.KippinInvoice.ModalInvoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddNewItem implements Serializable {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ItemId")
    @Expose
    private Integer itemId;
    @SerializedName("ServiceType")
    @Expose
    private String serviceType;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Quantity")
    @Expose
    private Double quantity;
    @SerializedName("Rate")
    @Expose
    private Double rate;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("Item")
    @Expose
    private String item;
    @SerializedName("InvoiceId")
    @Expose
    private Integer invoiceId;
    @SerializedName("Discount")
    @Expose
    private String discount;
    @SerializedName("ServiceTypeId")
    @Expose
    private Integer serviceTypeId;
    @SerializedName("Tax")
    @Expose
    private Object tax;
    @SerializedName("PST_Tax")
    @Expose
    private Object pSTTax;
    @SerializedName("HST_Tax")
    @Expose
    private Object hSTTax;
    @SerializedName("QST_Tax")
    @Expose
    private Object qSTTax;
    @SerializedName("GST_Tax")
    @Expose
    private Object gSTTax;
    @SerializedName("SubTotal")
    @Expose
    private Double subTotal;
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("UserId")
    @Expose
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Object getTax() {
        return tax;
    }

    public void setTax(Object tax) {
        this.tax = tax;
    }

    public Object getPSTTax() {
        return pSTTax;
    }

    public void setPSTTax(Object pSTTax) {
        this.pSTTax = pSTTax;
    }

    public Object getHSTTax() {
        return hSTTax;
    }

    public void setHSTTax(Object hSTTax) {
        this.hSTTax = hSTTax;
    }

    public Object getQSTTax() {
        return qSTTax;
    }

    public void setQSTTax(Object qSTTax) {
        this.qSTTax = qSTTax;
    }

    public Object getGSTTax() {
        return gSTTax;
    }

    public void setGSTTax(Object gSTTax) {
        this.gSTTax = gSTTax;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


}
