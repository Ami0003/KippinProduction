package com.kippin.webclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gaganpreet.singh on 1/22/2016.
 */
public class ModelLogin extends TemplateData {
    private String TaxationEndDay;

    private String SectorId;

    private String CurrencyId;

    private String Password;

    private String CorporationAddress;

    private String ProvinceId;

    private String LastName;

    private String PrivateKey;

    private String ResponseMessage;

    private String TaxEndYear;

    private boolean IsUnlinkUser;

    private String TaxEndMonthId;

    private String FirstName;

    private String IndustryId;

    private String GSTNumber;

    private String MobileNumber;

    private boolean IsPaid;

    private boolean IsTrial;

    private String TaxationStartDay;

    private String BusinessNumber;

    private String OwnershipId;

    private boolean IsUnlink;

    private boolean IsCompleted;

    private String AccountantId;

    private String ResponseCode;

    private String UserId;

    private boolean IsDownload;

    private String CompanyName;

    private String City;

    private String RoleId;

    private String TaxStartMonthId;

    private String Status;

    private String ModifiedDate;

    private boolean IsVerified;

    private String PostalCode;

    private String Email;

    private String Username;

    private String LicenseId;

    private String CreatedDate;

    private String CountryId;

    private String TaxStartYear;

    private boolean IsDeleted;

    private String InvoiceId;

    private String CompanyLogo;

    private String ContactPerson;

    private String CorporateAptNo;

    private String CorporateHouseNo;

    private String CorporateStreet;

    private String CorporateCity;

    private String CorporateState;

    private String CorporatePostalCode;

    private String ShippingAptNo;

    private String ShippingHouseNo;

    private String ShippingStreet;

    private String ShippingCity;

    private String ShippingState;

    private String ShippingPostalCode;

    private String GoodsType;

    private String CustomerId;


    @SerializedName("IsOnlyInvoice")
    @Expose
    private boolean is_Only_Invoice;
    public boolean is_IsOnlyInvoice() {
        return is_Only_Invoice;
    }

    public void set_Is_OnlyInvoice(boolean is_Only_Invoice) {
        this.is_Only_Invoice = is_Only_Invoice;
    }

    private Boolean isOnlyInvoice=null;
    //private Boolean IsOnlyInvoice=null;
    public Boolean isIsOnlyInvoice() {
        return isOnlyInvoice;
    }

    public void setIsOnlyInvoice(Boolean isOnlyInvoice) {
        this.isOnlyInvoice = isOnlyInvoice;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public void setUnlinkUser(boolean unlinkUser) {
        IsUnlinkUser = unlinkUser;
    }

    public void setPaid(boolean paid) {
        IsPaid = paid;
    }

    public void setTrial(boolean trial) {
        IsTrial = trial;
    }

    public void setUnlink(boolean unlink) {
        IsUnlink = unlink;
    }

    public void setCompleted(boolean completed) {
        IsCompleted = completed;
    }

    public void setDownload(boolean download) {
        IsDownload = download;
    }

    public void setVerified(boolean verified) {
        IsVerified = verified;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    public String getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        InvoiceId = invoiceId;
    }

    public String getCompanyLogo() {
        return CompanyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        CompanyLogo = companyLogo;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getCorporateAptNo() {
        return CorporateAptNo;
    }

    public void setCorporateAptNo(String corporateAptNo) {
        CorporateAptNo = corporateAptNo;
    }

    public String getCorporateHouseNo() {
        return CorporateHouseNo;
    }

 /*   public Boolean isOnlyInvoice() {
        return IsOnlyInvoice;
    }

    public void setOnlyInvoice(Boolean onlyInvoice) {
        IsOnlyInvoice = onlyInvoice;
    }*/

    public void setCorporateHouseNo(String corporateHouseNo) {
        CorporateHouseNo = corporateHouseNo;
    }

    public String getCorporateStreet() {
        return CorporateStreet;
    }

    public void setCorporateStreet(String corporateStreet) {
        CorporateStreet = corporateStreet;
    }

    public String getCorporateCity() {
        return CorporateCity;
    }

    public void setCorporateCity(String corporateCity) {
        CorporateCity = corporateCity;
    }

    public String getCorporateState() {
        return CorporateState;
    }

    public void setCorporateState(String corporateState) {
        CorporateState = corporateState;
    }

    public String getCorporatePostalCode() {
        return CorporatePostalCode;
    }

    public void setCorporatePostalCode(String corporatePostalCode) {
        CorporatePostalCode = corporatePostalCode;
    }

    public String getShippingAptNo() {
        return ShippingAptNo;
    }

    public void setShippingAptNo(String shippingAptNo) {
        ShippingAptNo = shippingAptNo;
    }

    public String getShippingHouseNo() {
        return ShippingHouseNo;
    }

    public void setShippingHouseNo(String shippingHouseNo) {
        ShippingHouseNo = shippingHouseNo;
    }

    public String getShippingStreet() {
        return ShippingStreet;
    }

    public void setShippingStreet(String shippingStreet) {
        ShippingStreet = shippingStreet;
    }

    public String getShippingCity() {
        return ShippingCity;
    }

    public void setShippingCity(String shippingCity) {
        ShippingCity = shippingCity;
    }

    public String getShippingState() {
        return ShippingState;
    }

    public void setShippingState(String shippingState) {
        ShippingState = shippingState;
    }

    public String getShippingPostalCode() {
        return ShippingPostalCode;
    }

    public void setShippingPostalCode(String shippingPostalCode) {
        ShippingPostalCode = shippingPostalCode;
    }

    public String getGoodsType() {
        return GoodsType;
    }

    public void setGoodsType(String goodsType) {
        GoodsType = goodsType;
    }

    public void setEmailSent(boolean emailSent) {
        IsEmailSent = emailSent;
    }

    private String Id;

    private boolean IsEmailSent;

    public String getGSTNumber() {
        return GSTNumber;
    }

    public void setGSTNumber(String GSTNumber) {
        this.GSTNumber = GSTNumber;
    }

    public String getTaxationEndDay() {
        return TaxationEndDay;
    }

    public void setTaxationEndDay(String taxationEndDay) {
        TaxationEndDay = taxationEndDay;
    }

    public String getSectorId() {
        return SectorId;
    }

    public void setSectorId(String sectorId) {
        SectorId = sectorId;
    }

    public String getCurrencyId() {
        return CurrencyId;
    }

    public void setCurrencyId(String currencyId) {
        CurrencyId = currencyId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getCorporationAddress() {
        return CorporationAddress;
    }

    public void setCorporationAddress(String corporationAddress) {
        CorporationAddress = corporationAddress;
    }

    public String getProvinceId() {
        return ProvinceId;
    }

    public void setProvinceId(String provinceId) {
        ProvinceId = provinceId;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPrivateKey() {
        return PrivateKey;
    }

    public void setPrivateKey(String privateKey) {
        PrivateKey = privateKey;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getTaxEndYear() {
        return TaxEndYear;
    }

    public void setTaxEndYear(String taxEndYear) {
        TaxEndYear = taxEndYear;
    }

    public boolean isUnlinkUser() {
        return IsUnlinkUser;
    }

    public void setIsUnlinkUser(boolean isUnlinkUser) {
        IsUnlinkUser = isUnlinkUser;
    }

    public String getTaxEndMonthId() {
        return TaxEndMonthId;
    }

    public void setTaxEndMonthId(String taxEndMonthId) {
        TaxEndMonthId = taxEndMonthId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getIndustryId() {
        return IndustryId;
    }

    public void setIndustryId(String industryId) {
        IndustryId = industryId;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public boolean isPaid() {
        return IsPaid;
    }

    public void setIsPaid(boolean isPaid) {
        IsPaid = isPaid;
    }

    public boolean isTrial() {
        return IsTrial;
    }

    public void setIsTrial(boolean isTrial) {
        IsTrial = isTrial;
    }

    public String getTaxationStartDay() {
        return TaxationStartDay;
    }

    public void setTaxationStartDay(String taxationStartDay) {
        TaxationStartDay = taxationStartDay;
    }

    public String getBusinessNumber() {
        return BusinessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        BusinessNumber = businessNumber;
    }

    public String getOwnershipId() {
        return OwnershipId;
    }

    public void setOwnershipId(String ownershipId) {
        OwnershipId = ownershipId;
    }

    public boolean isUnlink() {
        return IsUnlink;
    }

    public void setIsUnlink(boolean isUnlink) {
        IsUnlink = isUnlink;
    }

    public boolean isCompleted() {
        return IsCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        IsCompleted = isCompleted;
    }

    public String getAccountantId() {
        return AccountantId;
    }

    public void setAccountantId(String accountantId) {
        AccountantId = accountantId;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public boolean isDownload() {
        return IsDownload;
    }

    public void setIsDownload(boolean isDownload) {
        IsDownload = isDownload;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getTaxStartMonthId() {
        return TaxStartMonthId;
    }

    public void setTaxStartMonthId(String taxStartMonthId) {
        TaxStartMonthId = taxStartMonthId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }

    public boolean isVerified() {
        return IsVerified;
    }

    public void setIsVerified(boolean isVerified) {
        IsVerified = isVerified;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getLicenseId() {
        return LicenseId;
    }

    public void setLicenseId(String licenseId) {
        LicenseId = licenseId;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getCountryId() {
        return CountryId;
    }

    public void setCountryId(String countryId) {
        CountryId = countryId;
    }

    public String getTaxStartYear() {
        return TaxStartYear;
    }

    public void setTaxStartYear(String taxStartYear) {
        TaxStartYear = taxStartYear;
    }

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        IsDeleted = isDeleted;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public boolean isEmailSent() {
        return IsEmailSent;
    }

    public void setIsEmailSent(boolean isEmailSent) {
        IsEmailSent = isEmailSent;
    }
}

