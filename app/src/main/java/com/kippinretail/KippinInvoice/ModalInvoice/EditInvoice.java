package com.kippinretail.KippinInvoice.ModalInvoice;

/**
 * Created by kamaljeet.singh on 12/2/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class EditInvoice {

    @SerializedName("Id")
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
    @SerializedName("Total")
    @Expose
    private String total;
    @SerializedName("DepositePayment")
    @Expose
    private String depositePayment;
    @SerializedName("BalanceDue")
    @Expose
    private String balanceDue;
    @SerializedName("Note")
    @Expose
    private String note;
    @SerializedName("Terms")
    @Expose
    private String terms;
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("CustomerId")
    @Expose
    private String customerId;
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
    @SerializedName("IsDeleted")
    @Expose
    private String isDeleted;
    @SerializedName("Pro_Status")
    @Expose
    private String proStatus;
    @SerializedName("Pro_FlowStatus")
    @Expose
    private String proFlowStatus;
    @SerializedName("Type")
    @Expose
    private String type;
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
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The Id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The invoiceNumber
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * @param invoiceNumber The InvoiceNumber
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * @return The paymentTerms
     */
    public String getPaymentTerms() {
        return paymentTerms;
    }

    /**
     * @param paymentTerms The PaymentTerms
     */
    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    /**
     * @return The dueDate
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate The DueDate
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return The documentRef
     */
    public String getDocumentRef() {
        return documentRef;
    }

    /**
     * @param documentRef The DocumentRef
     */
    public void setDocumentRef(String documentRef) {
        this.documentRef = documentRef;
    }

    /**
     * @return The salesPerson
     */
    public String getSalesPerson() {
        return salesPerson;
    }

    /**
     * @param salesPerson The SalesPerson
     */
    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
    }

    /**
     * @return The shippingCost
     */
    public Object getShippingCost() {
        return shippingCost;
    }

    /**
     * @param shippingCost The ShippingCost
     */
    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    /**
     * @return The subTotal
     */
    public String getSubTotal() {
        return subTotal;
    }

    /**
     * @param subTotal The SubTotal
     */
    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    /**
     * @return The total
     */
    public String getTotal() {
        return total;
    }

    /**
     * @param total The Total
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * @return The depositePayment
     */
    public String getDepositePayment() {
        return depositePayment;
    }

    /**
     * @param depositePayment The DepositePayment
     */
    public void setDepositePayment(String depositePayment) {
        this.depositePayment = depositePayment;
    }

    /**
     * @return The balanceDue
     */
    public String getBalanceDue() {
        return balanceDue;
    }

    /**
     * @param balanceDue The BalanceDue
     */
    public void setBalanceDue(String balanceDue) {
        this.balanceDue = balanceDue;
    }

    /**
     * @return The note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note The Note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return The terms
     */
    public String getTerms() {
        return terms;
    }

    /**
     * @param terms The Terms
     */
    public void setTerms(String terms) {
        this.terms = terms;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The UserId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId The CustomerId
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return The roleId
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId The RoleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * @return The invoiceDate
     */
    public String getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * @param invoiceDate The InvoiceDate
     */
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * @return The createdDate
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate The CreatedDate
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return The modifyDate
     */
    public Object getModifyDate() {
        return modifyDate;
    }

    /**
     * @param modifyDate The ModifyDate
     */
    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * @return The isDeleted
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted The IsDeleted
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * @return The proStatus
     */
    public String getProStatus() {
        return proStatus;
    }

    /**
     * @param proStatus The Pro_Status
     */
    public void setProStatus(String proStatus) {
        this.proStatus = proStatus;
    }

    /**
     * @return The proFlowStatus
     */
    public String getProFlowStatus() {
        return proFlowStatus;
    }

    /**
     * @param proFlowStatus The Pro_FlowStatus
     */
    public void setProFlowStatus(String proFlowStatus) {
        this.proFlowStatus = proFlowStatus;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The Type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The isInvoiceReport
     */
    public String getIsInvoiceReport() {
        return isInvoiceReport;
    }

    /**
     * @param isInvoiceReport The IsInvoiceReport
     */
    public void setIsInvoiceReport(String isInvoiceReport) {
        this.isInvoiceReport = isInvoiceReport;
    }

    /**
     * @return The inRStatus
     */
    public String getInRStatus() {
        return inRStatus;
    }

    /**
     * @param inRStatus The In_R_Status
     */
    public void setInRStatus(String inRStatus) {
        this.inRStatus = inRStatus;
    }

    /**
     * @return The inRFlowStatus
     */
    public String getInRFlowStatus() {
        return inRFlowStatus;
    }

    /**
     * @param inRFlowStatus The In_R_FlowStatus
     */
    public void setInRFlowStatus(String inRFlowStatus) {
        this.inRFlowStatus = inRFlowStatus;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The FirstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return The tax
     */
    public String getTax() {
        return tax;
    }

    /**
     * @param tax The Tax
     */
    public void setTax(String tax) {
        this.tax = tax;
    }

    /**
     * @return The responseCode
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode The ResponseCode
     */
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return The responseMessage
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * @param responseMessage The ResponseMessage
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}