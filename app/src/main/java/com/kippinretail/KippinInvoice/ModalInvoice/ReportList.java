package com.kippinretail.KippinInvoice.ModalInvoice;

/**
 * Created by kamaljeet.singh on 12/14/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportList {

    @SerializedName("InvoiceId")
    @Expose
    private String id;
    @SerializedName("InvoiceNumber")
    @Expose
    private String invoiceNumber;
    @SerializedName("PaymentTerms")
    @Expose
    private String paymentTerms;
    @SerializedName("DueDate")
    @Expose
    private String dueDate;
    @SerializedName("DocumentRef")
    @Expose
    private String documentRef;
    @SerializedName("SalesPerson")
    @Expose
    private String salesPerson;
    @SerializedName("ShippingCost")
    @Expose
    private String shippingCost;
    @SerializedName("SubTotal")
    @Expose
    private String subTotal;
    @SerializedName("TotalText")
    @Expose
    private String total;
    @SerializedName("Total")
    @Expose
    private Double totalAmount;
    @SerializedName("DepositPayment")
    @Expose
    private Double depositPayment;
    @SerializedName("BalanceDue")
    @Expose
    private String balanceDue;
    @SerializedName("Note")
    @Expose
    private String note;
    @SerializedName("Terms")
    @Expose
    private String terms;
    @SerializedName("FlowStatus")
    @Expose
    private String FlowStatus;
    @SerializedName("InvoiceAge")
    @Expose
    private int InvoiceAge;
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("CustomerId")
    @Expose
    private String customerId;
    @SerializedName("ManualJVID")
    @Expose
    private String ManualJVID;
    @SerializedName("RoleId")
    @Expose
    private String roleId;
    @SerializedName("InvoiceDate")
    @Expose
    private String invoiceDate;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifyDate")
    @Expose
    private String modifyDate;
    @SerializedName("InvoiceTypeText")
    @Expose
    private String InvoiceTypeText;
    @SerializedName("IsDeleted")
    @Expose
    private String isDeleted;
    @SerializedName("Status")
    @Expose
    private String proStatus;
    @SerializedName("Pro_FlowStatus")
    @Expose
    private String proFlowStatus;
    @SerializedName("InvoiceType")
    @Expose
    private Integer type;
    @SerializedName("IsInvoiceReport")
    @Expose
    private String isInvoiceReport;
    @SerializedName("In_R_Status")
    @Expose
    private String inRStatus;
    @SerializedName("In_R_FlowStatus")
    @Expose
    private String inRFlowStatus;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("Tax")
    @Expose
    private String tax;
    @SerializedName("IsCustomer")
    @Expose
    private boolean isCustomer;
    @SerializedName("Customer")
    @Expose
    private String Customer;
    @SerializedName("Supplier")
    @Expose
    private String Supplier;

    @SerializedName("IsSupplierManualPaid")
    @Expose
    private String isSupplierManualPaid;
    @SerializedName("SupplierManualPaidAmount")
    @Expose
    private String supplierManualPaidAmount;
    @SerializedName("SupplierManualPaidJVID")
    @Expose
    private String supplierManualPaidJVID;
    @SerializedName("IsCustomerManualPaid")
    @Expose
    private String isCustomerManualPaid;
    @SerializedName("CustomerManualPaidAmount")
    @Expose
    private String customerManualPaidAmount;
    @SerializedName("CustomerManualPaidJVID")
    @Expose
    private String customerManualPaidJVID;
    @SerializedName("IsStripeUser")
    @Expose
    private String isStripe;
    @SerializedName("PaymentDate")
    @Expose
    private String paymentDate;
    @SerializedName("InvoiceJVID")
    @Expose
    private String invoiceJVID;
    @SerializedName("IsPaymentbyStripe")
    @Expose
    private String isPaymentbyStripe;
    @SerializedName("StripeJVID")
    @Expose
    private String stripeJVID;
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("PaidAmountText")
    @Expose
    private String PaidAmountText;

    public String getFlowStatus() {
        return FlowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        FlowStatus = flowStatus;
    }

    public int getInvoiceAge() {
        return InvoiceAge;
    }

    public void setInvoiceAge(int invoiceAge) {
        InvoiceAge = invoiceAge;
    }

    public String getManualJVID() {
        return ManualJVID;
    }

    public void setManualJVID(String manualJVID) {
        ManualJVID = manualJVID;
    }

    public String getInvoiceTypeText() {
        return InvoiceTypeText;
    }

    public void setInvoiceTypeText(String invoiceTypeText) {
        InvoiceTypeText = invoiceTypeText;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }


    public String getSupplier() {
        return Supplier;
    }

    public void setSupplier(String Supplier) {
        Supplier = Supplier;
    }

    public String getPaidAmountText() {
        return PaidAmountText;
    }

    public void setPaidAmountText(String paidAmountText) {
        PaidAmountText = paidAmountText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDocumentRef() {
        return documentRef;
    }

    public void setDocumentRef(String documentRef) {
        this.documentRef = documentRef;
    }

    public String getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Double getDepositPayment() {
        return depositPayment;
    }

    public void setDepositPayment(Double depositPayment) {
        this.depositPayment = depositPayment;
    }

    public String getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(String balanceDue) {
        this.balanceDue = balanceDue;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getInvoiceDate() {
        return (invoiceDate.substring(0, invoiceDate.indexOf('T')));
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getProStatus() {
        return proStatus;
    }

    public void setProStatus(String proStatus) {
        this.proStatus = proStatus;
    }

    public String getProFlowStatus() {
        return proFlowStatus;
    }

    public void setProFlowStatus(String proFlowStatus) {
        this.proFlowStatus = proFlowStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIsInvoiceReport() {
        return isInvoiceReport;
    }

    public void setIsInvoiceReport(String isInvoiceReport) {
        this.isInvoiceReport = isInvoiceReport;
    }

    public String getInRStatus() {
        return inRStatus;
    }

    public void setInRStatus(String inRStatus) {
        this.inRStatus = inRStatus;
    }

    public String getInRFlowStatus() {
        return inRFlowStatus;
    }

    public void setInRFlowStatus(String inRFlowStatus) {
        this.inRFlowStatus = inRFlowStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public boolean getIsCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(boolean isCustomer) {
        this.isCustomer = isCustomer;
    }

    public String getIsSupplierManualPaid() {
        return isSupplierManualPaid;
    }

    public void setIsSupplierManualPaid(String isSupplierManualPaid) {
        this.isSupplierManualPaid = isSupplierManualPaid;
    }

    public String getSupplierManualPaidAmount() {
        return supplierManualPaidAmount;
    }

    public void setSupplierManualPaidAmount(String supplierManualPaidAmount) {
        this.supplierManualPaidAmount = supplierManualPaidAmount;
    }

    public String getSupplierManualPaidJVID() {
        return supplierManualPaidJVID;
    }

    public void setSupplierManualPaidJVID(String supplierManualPaidJVID) {
        this.supplierManualPaidJVID = supplierManualPaidJVID;
    }

    public String getIsCustomerManualPaid() {
        return isCustomerManualPaid;
    }

    public void setIsCustomerManualPaid(String isCustomerManualPaid) {
        this.isCustomerManualPaid = isCustomerManualPaid;
    }

    public String getCustomerManualPaidAmount() {
        return customerManualPaidAmount;
    }

    public void setCustomerManualPaidAmount(String customerManualPaidAmount) {
        this.customerManualPaidAmount = customerManualPaidAmount;
    }

    public String getCustomerManualPaidJVID() {
        return customerManualPaidJVID;
    }

    public void setCustomerManualPaidJVID(String customerManualPaidJVID) {
        this.customerManualPaidJVID = customerManualPaidJVID;
    }

    public String getIsStripe() {
        return isStripe;
    }

    public void setIsStripe(String isStripe) {
        this.isStripe = isStripe;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getInvoiceJVID() {
        return invoiceJVID;
    }

    public void setInvoiceJVID(String invoiceJVID) {
        this.invoiceJVID = invoiceJVID;
    }

    public String getIsPaymentbyStripe() {
        return isPaymentbyStripe;
    }

    public void setIsPaymentbyStripe(String isPaymentbyStripe) {
        this.isPaymentbyStripe = isPaymentbyStripe;
    }

    public String getStripeJVID() {
        return stripeJVID;
    }

    public void setStripeJVID(String stripeJVID) {
        this.stripeJVID = stripeJVID;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Double getTotalAccount() {
        return totalAmount;
    }

    public void setTotalAccount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}