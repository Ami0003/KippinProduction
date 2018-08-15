package com.kippinretail.Modal.Invoice;

/**
 * Created by kamaljeet.singh on 11/16/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerList {

    @SerializedName("CustomerUserMappingId")
    @Expose
    private Integer customerUserMappingId;
    @SerializedName("CreatedByUserID")
    @Expose
    private Integer createdByUserID;
    @SerializedName("CustomerId")
    @Expose
    private String customerId;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("ContactPerson")
    @Expose
    private String contactPerson;
    @SerializedName("AptNo")
    @Expose
    private String aptNo;
    @SerializedName("HouseNo")
    @Expose
    private String houseNo;
    @SerializedName("Street")
    @Expose
    private String street;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("PostalCode")
    @Expose
    private String postalCode;
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
    @SerializedName("AreaCode")
    @Expose
    private String areaCode;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Website")
    @Expose
    private String website;
    @SerializedName("AdditionalEmail")
    @Expose
    private String additionalEmail;
    @SerializedName("Telephone")
    @Expose
    private String telephone;
    @SerializedName("KippinUser")
    @Expose
    private Boolean kippinUser;
    @SerializedName("IsFinanceUser")
    @Expose
    private Boolean isFinanceUser;
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("UserId")
    @Expose
    private Integer userId;

    public Integer getCustomerUserMappingId() {
        return customerUserMappingId;
    }

    public void setCustomerUserMappingId(Integer customerUserMappingId) {
        this.customerUserMappingId = customerUserMappingId;
    }

    public Integer getCreatedByUserID() {
        return createdByUserID;
    }

    public void setCreatedByUserID(Integer createdByUserID) {
        this.createdByUserID = createdByUserID;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAptNo() {
        return aptNo;
    }

    public void setAptNo(String aptNo) {
        this.aptNo = aptNo;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getShippingAptNo() {
        return shippingAptNo;
    }

    public void setShippingAptNo(String shippingAptNo) {
        this.shippingAptNo = shippingAptNo;
    }

    public String getShippingHouseNo() {
        return shippingHouseNo;
    }

    public void setShippingHouseNo(String shippingHouseNo) {
        this.shippingHouseNo = shippingHouseNo;
    }

    public String getShippingStreet() {
        return shippingStreet;
    }

    public void setShippingStreet(String shippingStreet) {
        this.shippingStreet = shippingStreet;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingState() {
        return shippingState;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }

    public String getShippingPostalCode() {
        return shippingPostalCode;
    }

    public void setShippingPostalCode(String shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAdditionalEmail() {
        return additionalEmail;
    }

    public void setAdditionalEmail(String additionalEmail) {
        this.additionalEmail = additionalEmail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Boolean getKippinUser() {
        return kippinUser;
    }

    public void setKippinUser(Boolean kippinUser) {
        this.kippinUser = kippinUser;
    }

    public Boolean getIsFinanceUser() {
        return isFinanceUser;
    }

    public void setIsFinanceUser(Boolean isFinanceUser) {
        this.isFinanceUser = isFinanceUser;
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