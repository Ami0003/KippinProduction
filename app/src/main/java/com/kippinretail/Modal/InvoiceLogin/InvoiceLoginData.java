package com.kippinretail.Modal.InvoiceLogin;

/**
 * Created by kamaljeet.singh on 11/9/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvoiceLoginData implements Serializable {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("ContactPerson")
    @Expose
    private String contactPerson;
    @SerializedName("CorporateAptNo")
    @Expose
    private String corporateAptNo;
    @SerializedName("CorporateHouseNo")
    @Expose
    private String corporateHouseNo;
    @SerializedName("CorporateStreet")
    @Expose
    private String corporateStreet;
    @SerializedName("CorporateCity")
    @Expose
    private String corporateCity;
    @SerializedName("CorporateState")
    @Expose
    private String corporateState;
    @SerializedName("CorporatePostalCode")
    @Expose
    private String corporatePostalCode;
    @SerializedName("ShippingAptNo")
    @Expose
    private String shippingAptNo;
    @SerializedName("ShippingHouseNo")
    @Expose
    private String shippingHouseNo;
    @SerializedName("ShippingStreet")
    @Expose
    private String shippingStreet;
    @SerializedName("ShippingCity")
    @Expose
    private String shippingCity;
    @SerializedName("ShippingState")
    @Expose
    private String shippingState;
    @SerializedName("ShippingPostalCode")
    @Expose
    private String shippingPostalCode;
    @SerializedName("GoodsType")
    @Expose
    private String goodsType;
    @SerializedName("BusinessNumber")
    @Expose
    private String businessNumber;
    @SerializedName("TradingCurrency")
    @Expose
    private String tradingCurrency;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("EmailTo")
    @Expose
    private String emailTo;
    @SerializedName("EmailCc")
    @Expose
    private String emailCc;
    @SerializedName("Website")
    @Expose
    private String website;
    @SerializedName("IsDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("IsOnlyInvoice")
    @Expose
    private Boolean isOnlyInvoice = null;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("FromInvoiceOrFinance")
    @Expose
    private Boolean fromInvoiceOrFinance;
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("CustomerId")
    @Expose
    private String CustomerId;

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getOnlyInvoice() {
        return isOnlyInvoice;
    }

    public void setOnlyInvoice(Boolean onlyInvoice) {
        isOnlyInvoice = onlyInvoice;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The Id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName The CompanyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return The contactPerson
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * @param contactPerson The ContactPerson
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * @return The corporateAptNo
     */
    public String getCorporateAptNo() {
        return corporateAptNo;
    }

    /**
     * @param corporateAptNo The CorporateAptNo
     */
    public void setCorporateAptNo(String corporateAptNo) {
        this.corporateAptNo = corporateAptNo;
    }

    /**
     * @return The corporateHouseNo
     */
    public String getCorporateHouseNo() {
        return corporateHouseNo;
    }

    /**
     * @param corporateHouseNo The CorporateHouseNo
     */
    public void setCorporateHouseNo(String corporateHouseNo) {
        this.corporateHouseNo = corporateHouseNo;
    }

    /**
     * @return The corporateStreet
     */
    public String getCorporateStreet() {
        return corporateStreet;
    }

    /**
     * @param corporateStreet The CorporateStreet
     */
    public void setCorporateStreet(String corporateStreet) {
        this.corporateStreet = corporateStreet;
    }

    /**
     * @return The corporateCity
     */
    public String getCorporateCity() {
        return corporateCity;
    }

    /**
     * @param corporateCity The CorporateCity
     */
    public void setCorporateCity(String corporateCity) {
        this.corporateCity = corporateCity;
    }

    /**
     * @return The corporateState
     */
    public String getCorporateState() {
        return corporateState;
    }

    /**
     * @param corporateState The CorporateState
     */
    public void setCorporateState(String corporateState) {
        this.corporateState = corporateState;
    }

    /**
     * @return The corporatePostalCode
     */
    public String getCorporatePostalCode() {
        return corporatePostalCode;
    }

    /**
     * @param corporatePostalCode The CorporatePostalCode
     */
    public void setCorporatePostalCode(String corporatePostalCode) {
        this.corporatePostalCode = corporatePostalCode;
    }

    /**
     * @return The shippingAptNo
     */
    public String getShippingAptNo() {
        return shippingAptNo;
    }

    /**
     * @param shippingAptNo The ShippingAptNo
     */
    public void setShippingAptNo(String shippingAptNo) {
        this.shippingAptNo = shippingAptNo;
    }

    /**
     * @return The shippingHouseNo
     */
    public String getShippingHouseNo() {
        return shippingHouseNo;
    }

    /**
     * @param shippingHouseNo The ShippingHouseNo
     */
    public void setShippingHouseNo(String shippingHouseNo) {
        this.shippingHouseNo = shippingHouseNo;
    }

    /**
     * @return The shippingStreet
     */
    public String getShippingStreet() {
        return shippingStreet;
    }

    /**
     * @param shippingStreet The ShippingStreet
     */
    public void setShippingStreet(String shippingStreet) {
        this.shippingStreet = shippingStreet;
    }

    /**
     * @return The shippingCity
     */
    public String getShippingCity() {
        return shippingCity;
    }

    /**
     * @param shippingCity The ShippingCity
     */
    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    /**
     * @return The shippingState
     */
    public String getShippingState() {
        return shippingState;
    }

    /**
     * @param shippingState The ShippingState
     */
    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }

    /**
     * @return The shippingPostalCode
     */
    public String getShippingPostalCode() {
        return shippingPostalCode;
    }

    /**
     * @param shippingPostalCode The ShippingPostalCode
     */
    public void setShippingPostalCode(String shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
    }

    /**
     * @return The goodsType
     */
    public String getGoodsType() {
        return goodsType;
    }

    /**
     * @param goodsType The GoodsType
     */
    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    /**
     * @return The businessNumber
     */
    public String getBusinessNumber() {
        return businessNumber;
    }

    /**
     * @param businessNumber The BusinessNumber
     */
    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    /**
     * @return The tradingCurrency
     */
    public String getTradingCurrency() {
        return tradingCurrency;
    }

    /**
     * @param tradingCurrency The TradingCurrency
     */
    public void setTradingCurrency(String tradingCurrency) {
        this.tradingCurrency = tradingCurrency;
    }

    /**
     * @return The mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * @param mobileNumber The MobileNumber
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @return The emailTo
     */
    public String getEmailTo() {
        return emailTo;
    }

    /**
     * @param emailTo The EmailTo
     */
    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    /**
     * @return The emailCc
     */
    public String getEmailCc() {
        return emailCc;
    }

    /**
     * @param emailCc The EmailCc
     */
    public void setEmailCc(String emailCc) {
        this.emailCc = emailCc;
    }

    /**
     * @return The website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * @param website The Website
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * @return The isDeleted
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted The IsDeleted
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * @return The isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive The IsActive
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return The isOnlyInvoice
     */
    public boolean getIsOnlyInvoice() {
        return isOnlyInvoice;
    }

    /**
     * @param isOnlyInvoice The IsOnlyInvoice
     */
    public void setIsOnlyInvoice(Boolean isOnlyInvoice) {
        this.isOnlyInvoice = isOnlyInvoice;
    }

    /**
     * @return The createdOn
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn The CreatedOn
     */
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The fromInvoiceOrFinance
     */
    public Boolean getFromInvoiceOrFinance() {
        return fromInvoiceOrFinance;
    }

    /**
     * @param fromInvoiceOrFinance The FromInvoiceOrFinance
     */
    public void setFromInvoiceOrFinance(Boolean fromInvoiceOrFinance) {
        this.fromInvoiceOrFinance = fromInvoiceOrFinance;
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

    /**
     * @return The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId The UserId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}