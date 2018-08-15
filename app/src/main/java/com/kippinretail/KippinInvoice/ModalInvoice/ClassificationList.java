package com.kippinretail.KippinInvoice.ModalInvoice;

/**
 * Created by kamaljeet.singh on 12/2/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassificationList {

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
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("UserId")
    @Expose
    private Integer userId;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The Id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The itemId
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     *
     * @param itemId
     * The ItemId
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     *
     * @return
     * The serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     *
     * @param serviceType
     * The ServiceType
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The quantity
     */
    public Double getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity
     * The Quantity
     */
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return
     * The rate
     */
    public Double getRate() {
        return rate;
    }

    /**
     *
     * @param rate
     * The Rate
     */
    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     *
     * @return
     * The amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     *
     * @param amount
     * The Amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     *
     * @return
     * The item
     */
    public String getItem() {
        return item;
    }

    /**
     *
     * @param item
     * The Item
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     *
     * @return
     * The invoiceId
     */
    public Integer getInvoiceId() {
        return invoiceId;
    }

    /**
     *
     * @param invoiceId
     * The InvoiceId
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     *
     * @return
     * The discount
     */
    public String getDiscount() {
        return discount;
    }

    /**
     *
     * @param discount
     * The Discount
     */
    public void setDiscount(String discount) {
        this.discount = discount;
    }

    /**
     *
     * @return
     * The serviceTypeId
     */
    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    /**
     *
     * @param serviceTypeId
     * The ServiceTypeId
     */
    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    /**
     *
     * @return
     * The responseCode
     */
    public Integer getResponseCode() {
        return responseCode;
    }

    /**
     *
     * @param responseCode
     * The ResponseCode
     */
    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    /**
     *
     * @return
     * The responseMessage
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     *
     * @param responseMessage
     * The ResponseMessage
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    /**
     *
     * @return
     * The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The UserId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}