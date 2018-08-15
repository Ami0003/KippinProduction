
package com.kippinretail.KippinInvoice.ModalInvoice;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditPerformaDetail {

    @SerializedName("InvoiceID")
    @Expose
    private Integer invoiceID;
    @SerializedName("InvoiceNumber")
    @Expose
    private String invoiceNumber;
    @SerializedName("FlowStatus")
    @Expose
    private String flowStatus;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("InvoiceDate")
    @Expose
    private String invoiceDate;
    @SerializedName("InvoiceDateText")
    @Expose
    private String invoiceDateText;
    @SerializedName("Supplier")
    @Expose
    private String supplier;
    @SerializedName("Customer")
    @Expose
    private String customer;
    @SerializedName("Total")
    @Expose
    private Double total;
    @SerializedName("TotalText")
    @Expose
    private String totalText;
    @SerializedName("DepositePayment")
    @Expose
    private Double depositePayment;
    @SerializedName("DepositePaymentText")
    @Expose
    private String depositePaymentText;
    @SerializedName("BalanceDue")
    @Expose
    private Double balanceDue;
    @SerializedName("BalanceDueText")
    @Expose
    private String balanceDueText;
    @SerializedName("ItemsCount")
    @Expose
    private Integer itemsCount;
    @SerializedName("RejectReason")
    @Expose
    private Object rejectReason;
    @SerializedName("ServiceTypeId")
    @Expose
    private List<Integer> serviceTypeId = null;
    @SerializedName("IsInvoiceOnlyUser")
    @Expose
    private Boolean isInvoiceOnlyUser;
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("UserId")
    @Expose
    private Integer userId;

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceDateText() {
        return invoiceDateText;
    }

    public void setInvoiceDateText(String invoiceDateText) {
        this.invoiceDateText = invoiceDateText;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getTotalText() {
        return totalText;
    }

    public void setTotalText(String totalText) {
        this.totalText = totalText;
    }

    public Double getDepositePayment() {
        return depositePayment;
    }

    public void setDepositePayment(Double depositePayment) {
        this.depositePayment = depositePayment;
    }

    public String getDepositePaymentText() {
        return depositePaymentText;
    }

    public void setDepositePaymentText(String depositePaymentText) {
        this.depositePaymentText = depositePaymentText;
    }

    public Double getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(Double balanceDue) {
        this.balanceDue = balanceDue;
    }

    public String getBalanceDueText() {
        return balanceDueText;
    }

    public void setBalanceDueText(String balanceDueText) {
        this.balanceDueText = balanceDueText;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }

    public Object getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(Object rejectReason) {
        this.rejectReason = rejectReason;
    }

    public List<Integer> getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(List<Integer> serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Boolean getIsInvoiceOnlyUser() {
        return isInvoiceOnlyUser;
    }

    public void setIsInvoiceOnlyUser(Boolean isInvoiceOnlyUser) {
        this.isInvoiceOnlyUser = isInvoiceOnlyUser;
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
