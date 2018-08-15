
package com.kippinretail.KippinInvoice.ModalInvoice;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditRejectInvoice {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ButtonType")
    @Expose
    private Object buttonType;
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
    private Double shippingCost;
    @SerializedName("Total")
    @Expose
    private Double total;
    @SerializedName("DepositePayment")
    @Expose
    private Double depositePayment;
    @SerializedName("BalanceDue")
    @Expose
    private Double balanceDue;
    @SerializedName("Note")
    @Expose
    private String note;
    @SerializedName("Terms")
    @Expose
    private String terms;
    @SerializedName("InvoiceDate")
    @Expose
    private String invoiceDate;
    @SerializedName("CreatedDate")
    @Expose
    private Object createdDate;
    @SerializedName("ModifyDate")
    @Expose
    private Object modifyDate;
    @SerializedName("IsDeleted")
    @Expose
    private Object isDeleted;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("CustomerId")
    @Expose
    private Integer customerId;
    @SerializedName("RoleId")
    @Expose
    private Object roleId;
    @SerializedName("ServiceType")
    @Expose
    private List<String> serviceType = null;
    @SerializedName("Description")
    @Expose
    private List<String> description = null;
    @SerializedName("Quantity")
    @Expose
    private List<Double> quantity = null;
    @SerializedName("Rate")
    @Expose
    private List<Double> rate = null;
    @SerializedName("Amount")
    @Expose
    private List<Double> amount = null;
    @SerializedName("Item")
    @Expose
    private List<String> item = null;
    @SerializedName("InvoiceId")
    @Expose
    private Object invoiceId;
    @SerializedName("Discount")
    @Expose
    private List<String> discount = null;
    @SerializedName("ServiceTypeId")
    @Expose
    private List<Integer> serviceTypeId = null;
    @SerializedName("ItemId")
    @Expose
    private List<Integer> itemId = null;
    @SerializedName("Pro_Status")
    @Expose
    private Object proStatus;
    @SerializedName("Pro_FlowStatus")
    @Expose
    private Object proFlowStatus;
    @SerializedName("Type")
    @Expose
    private Integer type;
    @SerializedName("IsInvoiceReport")
    @Expose
    private Object isInvoiceReport;
    @SerializedName("In_R_Status")
    @Expose
    private Object inRStatus;
    @SerializedName("In_R_FlowStatus")
    @Expose
    private Object inRFlowStatus;
    @SerializedName("Customer_ServiceTypeId")
    @Expose
    private List<Integer> customerServiceTypeId = null;
    @SerializedName("Customer_Service")
    @Expose
    private List<Object> customerService = null;
    @SerializedName("Tax")
    @Expose
    private List<String> tax = null;
    @SerializedName("GST_Tax")
    @Expose
    private List<String> gSTTax = null;
    @SerializedName("HST_Tax")
    @Expose
    private List<String> hSTTax = null;
    @SerializedName("PST_Tax")
    @Expose
    private List<String> pSTTax = null;
    @SerializedName("QST_Tax")
    @Expose
    private List<String> qSTTax = null;
    @SerializedName("TaxType")
    @Expose
    private Object taxType;
    @SerializedName("SectionType")
    @Expose
    private Object sectionType;
    @SerializedName("SubTotal")
    @Expose
    private List<Double> subTotal = null;
    @SerializedName("RejectReason")
    @Expose
    private Object rejectReason;
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private Object responseMessage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getButtonType() {
        return buttonType;
    }

    public void setButtonType(Object buttonType) {
        this.buttonType = buttonType;
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

    public Double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getDepositePayment() {
        return depositePayment;
    }

    public void setDepositePayment(Double depositePayment) {
        this.depositePayment = depositePayment;
    }

    public Double getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(Double balanceDue) {
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

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Object getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    public Object getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Object modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Object getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Object isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Object getRoleId() {
        return roleId;
    }

    public void setRoleId(Object roleId) {
        this.roleId = roleId;
    }

    public List<String> getServiceType() {
        return serviceType;
    }

    public void setServiceType(List<String> serviceType) {
        this.serviceType = serviceType;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<Double> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<Double> quantity) {
        this.quantity = quantity;
    }

    public List<Double> getRate() {
        return rate;
    }

    public void setRate(List<Double> rate) {
        this.rate = rate;
    }

    public List<Double> getAmount() {
        return amount;
    }

    public void setAmount(List<Double> amount) {
        this.amount = amount;
    }

    public List<String> getItem() {
        return item;
    }

    public void setItem(List<String> item) {
        this.item = item;
    }

    public Object getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Object invoiceId) {
        this.invoiceId = invoiceId;
    }

    public List<String> getDiscount() {
        return discount;
    }

    public void setDiscount(List<String> discount) {
        this.discount = discount;
    }

    public List<Integer> getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(List<Integer> serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public List<Integer> getItemId() {
        return itemId;
    }

    public void setItemId(List<Integer> itemId) {
        this.itemId = itemId;
    }

    public Object getProStatus() {
        return proStatus;
    }

    public void setProStatus(Object proStatus) {
        this.proStatus = proStatus;
    }

    public Object getProFlowStatus() {
        return proFlowStatus;
    }

    public void setProFlowStatus(Object proFlowStatus) {
        this.proFlowStatus = proFlowStatus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getIsInvoiceReport() {
        return isInvoiceReport;
    }

    public void setIsInvoiceReport(Object isInvoiceReport) {
        this.isInvoiceReport = isInvoiceReport;
    }

    public Object getInRStatus() {
        return inRStatus;
    }

    public void setInRStatus(Object inRStatus) {
        this.inRStatus = inRStatus;
    }

    public Object getInRFlowStatus() {
        return inRFlowStatus;
    }

    public void setInRFlowStatus(Object inRFlowStatus) {
        this.inRFlowStatus = inRFlowStatus;
    }

    public List<Integer> getCustomerServiceTypeId() {
        return customerServiceTypeId;
    }

    public void setCustomerServiceTypeId(List<Integer> customerServiceTypeId) {
        this.customerServiceTypeId = customerServiceTypeId;
    }

    public List<Object> getCustomerService() {
        return customerService;
    }

    public void setCustomerService(List<Object> customerService) {
        this.customerService = customerService;
    }

    public List<String> getTax() {
        return tax;
    }

    public void setTax(List<String> tax) {
        this.tax = tax;
    }

    public List<String> getGSTTax() {
        return gSTTax;
    }

    public void setGSTTax(List<String> gSTTax) {
        this.gSTTax = gSTTax;
    }

    public List<String> getHSTTax() {
        return hSTTax;
    }

    public void setHSTTax(List<String> hSTTax) {
        this.hSTTax = hSTTax;
    }

    public List<String> getPSTTax() {
        return pSTTax;
    }

    public void setPSTTax(List<String> pSTTax) {
        this.pSTTax = pSTTax;
    }

    public List<String> getQSTTax() {
        return qSTTax;
    }

    public void setQSTTax(List<String> qSTTax) {
        this.qSTTax = qSTTax;
    }

    public Object getTaxType() {
        return taxType;
    }

    public void setTaxType(Object taxType) {
        this.taxType = taxType;
    }

    public Object getSectionType() {
        return sectionType;
    }

    public void setSectionType(Object sectionType) {
        this.sectionType = sectionType;
    }

    public List<Double> getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(List<Double> subTotal) {
        this.subTotal = subTotal;
    }

    public Object getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(Object rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Object getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(Object responseMessage) {
        this.responseMessage = responseMessage;
    }

}
