package com.kippinretail.Modal;

/**
 * Created by kamaljeet.singh on 10/7/2016.
 */

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class Merchant_ {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("Firstname")
    @Expose
    private String firstname;
    @SerializedName("Lastname")
    @Expose
    private String lastname;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("MerchantId")
    @Expose
    private Object merchantId;
    @SerializedName("RoleId")
    @Expose
    private Integer roleId;
    @SerializedName("Age")
    @Expose
    private Object age;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("Dob")
    @Expose
    private Object dob;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Mobile")
    @Expose
    private Object mobile;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("BusinessName")
    @Expose
    private String businessName;
    @SerializedName("BusinessNumber")
    @Expose
    private String businessNumber;
    @SerializedName("BusinessPhoneNumber")
    @Expose
    private String businessPhoneNumber;
    @SerializedName("Website")
    @Expose
    private String website;
    @SerializedName("PrivateKey")
    @Expose
    private Object privateKey;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("IsSubscribedMerchant")
    @Expose
    private Object isSubscribedMerchant;
    @SerializedName("DateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("DateModified")
    @Expose
    private String dateModified;
    @SerializedName("Businessdescription")
    @Expose
    private String businessdescription;
    @SerializedName("Industry")
    @Expose
    private String industry;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("IsAuthenticated")
    @Expose
    private Object isAuthenticated;
    @SerializedName("IsEmployee")
    @Expose
    private Object isEmployee;
    @SerializedName("ReferralCode")
    @Expose
    private String referralCode;
    @SerializedName("RefferCodepath")
    @Expose
    private String refferCodepath;
    @SerializedName("ReferralId")
    @Expose
    private Object referralId;
    @SerializedName("MerchantPoint")
    @Expose
    private Double merchantPoint;
    @SerializedName("MultipleRole")
    @Expose
    private Object multipleRole;
    @SerializedName("IndustryId")
    @Expose
    private Integer industryId;
    @SerializedName("CharityPurpose")
    @Expose
    private Object charityPurpose;
    @SerializedName("CharityTypeIdsCommaSeparated")
    @Expose
    private Object charityTypeIdsCommaSeparated;
    @SerializedName("LoyalityCardName")
    @Expose
    private String loyalityCardName;
    @SerializedName("LoyalityBarcode")
    @Expose
    private String loyalityBarcode;
    @SerializedName("IsInvited")
    @Expose
    private Boolean isInvited;
    @SerializedName("IsLoyalityCardActive")
    @Expose
    private Boolean isLoyalityCardActive;
    @SerializedName("ProfileImage")
    @Expose
    private String profileImage;
    @SerializedName("TermsConditions")
    @Expose
    private String termsConditions;
    @SerializedName("IsFirstLogin")
    @Expose
    private Object isFirstLogin;
    @SerializedName("UdId")
    @Expose
    private Object udId;
    @SerializedName("IsRead")
    @Expose
    private Boolean isRead;
    @SerializedName("IsPunchCard")
    @Expose
    private Boolean isPunchCard;
    @SerializedName("LoyaltyCardId")
    @Expose
    private Integer loyaltyCardId;
    @SerializedName("objPunchCard")
    @Expose
    private Object objPunchCard;
    @SerializedName("Currency")
    @Expose
    private Object currency;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;

    public String getResponseCode() {
       return responseCode;
    }

    public void setResponseCode(String responseCode) {
       this.responseCode = responseCode;
    }

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
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     *
     * @param firstname
     * The Firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     *
     * @return
     * The lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     *
     * @param lastname
     * The Lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     * The merchantId
     */
    public Object getMerchantId() {
        return merchantId;
    }

    /**
     *
     * @param merchantId
     * The MerchantId
     */
    public void setMerchantId(Object merchantId) {
        this.merchantId = merchantId;
    }

    /**
     *
     * @return
     * The roleId
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     *
     * @param roleId
     * The RoleId
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     *
     * @return
     * The age
     */
    public Object getAge() {
        return age;
    }

    /**
     *
     * @param age
     * The Age
     */
    public void setAge(Object age) {
        this.age = age;
    }

    /**
     *
     * @return
     * The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     * The Gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     * The dob
     */
    public Object getDob() {
        return dob;
    }

    /**
     *
     * @param dob
     * The Dob
     */
    public void setDob(Object dob) {
        this.dob = dob;
    }

    /**
     *
     * @return
     * The location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The Location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The mobile
     */
    public Object getMobile() {
        return mobile;
    }

    /**
     *
     * @param mobile
     * The Mobile
     */
    public void setMobile(Object mobile) {
        this.mobile = mobile;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     * The businessName
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     *
     * @param businessName
     * The BusinessName
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     *
     * @return
     * The businessNumber
     */
    public String getBusinessNumber() {
        return businessNumber;
    }

    /**
     *
     * @param businessNumber
     * The BusinessNumber
     */
    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    /**
     *
     * @return
     * The businessPhoneNumber
     */
    public String getBusinessPhoneNumber() {
        return businessPhoneNumber;
    }

    /**
     *
     * @param businessPhoneNumber
     * The BusinessPhoneNumber
     */
    public void setBusinessPhoneNumber(String businessPhoneNumber) {
        this.businessPhoneNumber = businessPhoneNumber;
    }

    /**
     *
     * @return
     * The website
     */
    public String getWebsite() {
        return website;
    }

    /**
     *
     * @param website
     * The Website
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     *
     * @return
     * The privateKey
     */
    public Object getPrivateKey() {
        return privateKey;
    }

    /**
     *
     * @param privateKey
     * The PrivateKey
     */
    public void setPrivateKey(Object privateKey) {
        this.privateKey = privateKey;
    }

    /**
     *
     * @return
     * The isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     * The IsActive
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     *
     * @return
     * The isSubscribedMerchant
     */
    public Object getIsSubscribedMerchant() {
        return isSubscribedMerchant;
    }

    /**
     *
     * @param isSubscribedMerchant
     * The IsSubscribedMerchant
     */
    public void setIsSubscribedMerchant(Object isSubscribedMerchant) {
        this.isSubscribedMerchant = isSubscribedMerchant;
    }

    /**
     *
     * @return
     * The dateCreated
     */
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     *
     * @param dateCreated
     * The DateCreated
     */
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     *
     * @return
     * The dateModified
     */
    public String getDateModified() {
        return dateModified;
    }

    /**
     *
     * @param dateModified
     * The DateModified
     */
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    /**
     *
     * @return
     * The businessdescription
     */
    public String getBusinessdescription() {
        return businessdescription;
    }

    /**
     *
     * @param businessdescription
     * The Businessdescription
     */
    public void setBusinessdescription(String businessdescription) {
        this.businessdescription = businessdescription;
    }

    /**
     *
     * @return
     * The industry
     */
    public String getIndustry() {
        return industry;
    }

    /**
     *
     * @param industry
     * The Industry
     */
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    /**
     *
     * @return
     * The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     * The Country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     * The isAuthenticated
     */
    public Object getIsAuthenticated() {
        return isAuthenticated;
    }

    /**
     *
     * @param isAuthenticated
     * The IsAuthenticated
     */
    public void setIsAuthenticated(Object isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    /**
     *
     * @return
     * The isEmployee
     */
    public Object getIsEmployee() {
        return isEmployee;
    }

    /**
     *
     * @param isEmployee
     * The IsEmployee
     */
    public void setIsEmployee(Object isEmployee) {
        this.isEmployee = isEmployee;
    }

    /**
     *
     * @return
     * The referralCode
     */
    public String getReferralCode() {
        return referralCode;
    }

    /**
     *
     * @param referralCode
     * The ReferralCode
     */
    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    /**
     *
     * @return
     * The refferCodepath
     */
    public String getRefferCodepath() {
        return refferCodepath;
    }

    /**
     *
     * @param refferCodepath
     * The RefferCodepath
     */
    public void setRefferCodepath(String refferCodepath) {
        this.refferCodepath = refferCodepath;
    }

    /**
     *
     * @return
     * The referralId
     */
    public Object getReferralId() {
        return referralId;
    }

    /**
     *
     * @param referralId
     * The ReferralId
     */
    public void setReferralId(Object referralId) {
        this.referralId = referralId;
    }

    /**
     *
     * @return
     * The merchantPoint
     */
    public Double getMerchantPoint() {
        return merchantPoint;
    }

    /**
     *
     * @param merchantPoint
     * The MerchantPoint
     */
    public void setMerchantPoint(Double merchantPoint) {
        this.merchantPoint = merchantPoint;
    }

    /**
     *
     * @return
     * The multipleRole
     */
    public Object getMultipleRole() {
        return multipleRole;
    }

    /**
     *
     * @param multipleRole
     * The MultipleRole
     */
    public void setMultipleRole(Object multipleRole) {
        this.multipleRole = multipleRole;
    }

    /**
     *
     * @return
     * The industryId
     */
    public Integer getIndustryId() {
        return industryId;
    }

    /**
     *
     * @param industryId
     * The IndustryId
     */
    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    /**
     *
     * @return
     * The charityPurpose
     */
    public Object getCharityPurpose() {
        return charityPurpose;
    }

    /**
     *
     * @param charityPurpose
     * The CharityPurpose
     */
    public void setCharityPurpose(Object charityPurpose) {
        this.charityPurpose = charityPurpose;
    }

    /**
     *
     * @return
     * The charityTypeIdsCommaSeparated
     */
    public Object getCharityTypeIdsCommaSeparated() {
        return charityTypeIdsCommaSeparated;
    }

    /**
     *
     * @param charityTypeIdsCommaSeparated
     * The CharityTypeIdsCommaSeparated
     */
    public void setCharityTypeIdsCommaSeparated(Object charityTypeIdsCommaSeparated) {
        this.charityTypeIdsCommaSeparated = charityTypeIdsCommaSeparated;
    }

    /**
     *
     * @return
     * The loyalityCardName
     */
    public String getLoyalityCardName() {
        return loyalityCardName;
    }

    /**
     *
     * @param loyalityCardName
     * The LoyalityCardName
     */
    public void setLoyalityCardName(String loyalityCardName) {
        this.loyalityCardName = loyalityCardName;
    }

    /**
     *
     * @return
     * The loyalityBarcode
     */
    public String getLoyalityBarcode() {
        return loyalityBarcode;
    }

    /**
     *
     * @param loyalityBarcode
     * The LoyalityBarcode
     */
    public void setLoyalityBarcode(String loyalityBarcode) {
        this.loyalityBarcode = loyalityBarcode;
    }

    /**
     *
     * @return
     * The isInvited
     */
    public Boolean getIsInvited() {
        return isInvited;
    }

    /**
     *
     * @param isInvited
     * The IsInvited
     */
    public void setIsInvited(Boolean isInvited) {
        this.isInvited = isInvited;
    }

    /**
     *
     * @return
     * The isLoyalityCardActive
     */
    public Boolean getIsLoyalityCardActive() {
        return isLoyalityCardActive;
    }

    /**
     *
     * @param isLoyalityCardActive
     * The IsLoyalityCardActive
     */
    public void setIsLoyalityCardActive(Boolean isLoyalityCardActive) {
        this.isLoyalityCardActive = isLoyalityCardActive;
    }

    /**
     *
     * @return
     * The profileImage
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     *
     * @param profileImage
     * The ProfileImage
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     *
     * @return
     * The termsConditions
     */
    public String getTermsConditions() {
        return termsConditions;
    }

    /**
     *
     * @param termsConditions
     * The TermsConditions
     */
    public void setTermsConditions(String termsConditions) {
        this.termsConditions = termsConditions;
    }

    /**
     *
     * @return
     * The isFirstLogin
     */
    public Object getIsFirstLogin() {
        return isFirstLogin;
    }

    /**
     *
     * @param isFirstLogin
     * The IsFirstLogin
     */
    public void setIsFirstLogin(Object isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    /**
     *
     * @return
     * The udId
     */
    public Object getUdId() {
        return udId;
    }

    /**
     *
     * @param udId
     * The UdId
     */
    public void setUdId(Object udId) {
        this.udId = udId;
    }

    /**
     *
     * @return
     * The isRead
     */
    public Boolean getIsRead() {
        return isRead;
    }

    /**
     *
     * @param isRead
     * The IsRead
     */
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    /**
     *
     * @return
     * The isPunchCard
     */
    public Boolean getIsPunchCard() {
        return isPunchCard;
    }

    /**
     *
     * @param isPunchCard
     * The IsPunchCard
     */
    public void setIsPunchCard(Boolean isPunchCard) {
        this.isPunchCard = isPunchCard;
    }

    /**
     *
     * @return
     * The loyaltyCardId
     */
    public Integer getLoyaltyCardId() {
        return loyaltyCardId;
    }

    /**
     *
     * @param loyaltyCardId
     * The LoyaltyCardId
     */
    public void setLoyaltyCardId(Integer loyaltyCardId) {
        this.loyaltyCardId = loyaltyCardId;
    }

    /**
     *
     * @return
     * The objPunchCard
     */
    public Object getObjPunchCard() {
        return objPunchCard;
    }

    /**
     *
     * @param objPunchCard
     * The objPunchCard
     */
    public void setObjPunchCard(Object objPunchCard) {
        this.objPunchCard = objPunchCard;
    }

    /**
     *
     * @return
     * The currency
     */
    public Object getCurrency() {
        return currency;
    }

    /**
     *
     * @param currency
     * The Currency
     */
    public void setCurrency(Object currency) {
        this.currency = currency;
    }

    /**
     *
     * @return
     * The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     * The City
     */
    public void setCity(String city) {
        this.city = city;
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

}