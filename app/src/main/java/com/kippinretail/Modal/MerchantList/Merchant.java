package com.kippinretail.Modal.MerchantList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kippinretail.Modal.ModalResponse;

import java.io.Serializable;

/**
 * Created by sandeep.saini on 3/12/2016.
 */
public class Merchant extends ModalResponse implements Serializable {
    @SerializedName("Age")
    @Expose
    private String Age;

    @SerializedName("Password")
    @Expose
    private String Password;

    @SerializedName("Businessdescription")
    @Expose
    private String Businessdescription;

    private String LoyalityBarcode;

    @SerializedName("RefferCodepath")
    @Expose
    private String RefferCodepath;

    @SerializedName("Dob")
    @Expose
    private String Dob;

    @SerializedName("PrivateKey")
    @Expose
    private String PrivateKey;



    @SerializedName("MultipleRole")
    @Expose
    private String MultipleRole;

    @SerializedName("IsActive")
    @Expose
    private String IsActive;

    @SerializedName("MerchantId")
    @Expose
    private String MerchantId;

    @SerializedName("Mobile")
    @Expose
    private String Mobile;

    @SerializedName("Lastname")
    @Expose
    private String Lastname;

    @SerializedName("Firstname")
    @Expose
    private String Firstname;

    @SerializedName("IndustryId")
    @Expose
    private String IndustryId;

    @SerializedName("BusinessNumber")
    @Expose
    private String BusinessNumber;

    @SerializedName("Website")
    @Expose
    private String Website;

    @SerializedName("IsEmployee")
    @Expose
    private String IsEmployee;

    @SerializedName("DateCreated")
    @Expose
    private String DateCreated;

    @SerializedName("Location")
    @Expose
    private String Location;

    @SerializedName("BusinessName")
    @Expose
    private String BusinessName;


    @SerializedName("UserId")
    @Expose
    private String UserId;

    @SerializedName("MerchantPoint")
    @Expose
    private String MerchantPoint;

    @SerializedName("Country")
    @Expose
    private String Country;

    @SerializedName("BusinessPhoneNumber")
    @Expose
    private String BusinessPhoneNumber;

    @SerializedName("RoleId")
    @Expose
    private String RoleId;

    @SerializedName("Email")
    @Expose
    private String Email;

    @SerializedName("DateModified")
    @Expose
    private String DateModified;

    @SerializedName("Gender")
    @Expose
    private String Gender;

    @SerializedName("Username")
    @Expose
    private String Username;

    @SerializedName("Id")
    @Expose
    private String Id;

    @SerializedName("LoyalityTotalPoints")
    @Expose
    private int LoyalityTotalPoints;

    @SerializedName("ReferralId")
    @Expose
    private String ReferralId;

    @SerializedName("IsAuthenticated")
    @Expose
    private String IsAuthenticated;

    @SerializedName("ReferralCode")
    @Expose
    private String ReferralCode;

    @SerializedName("Industry")
    @Expose
    private String Industry;

    @SerializedName("LoyalityCardName")
    @Expose
    private String LoyalityCardName;

    @SerializedName("TermsConditions")
    @Expose
    private String TermsConditions;


    public String isTermsConditions() {
        return TermsConditions;
    }

    public void setTermsConditions(String termsConditions) {
        TermsConditions = termsConditions;
    }

    @SerializedName("IsSubscribedMerchant")
    @Expose

    private boolean IsSubscribedMerchant;

    @SerializedName("IsRead")
    @Expose
    private boolean IsRead;
    @SerializedName("ProfileImage")
    @Expose
    private String ProfileImage;
    @SerializedName("LoyaltyTemplatePath")
    @Expose
    private String LoyaltyTemplatePath;
    //

    public Boolean getIsPunchCard() {
        return IsPunchCard;
    }

    public void setIsPunchCard(Boolean isPunchCard) {
        IsPunchCard = isPunchCard;
    }

    @SerializedName("IsPunchCard")
    @Expose

    private Boolean IsPunchCard;

    @SerializedName("objPunchCard")
    @Expose
    private PunchCardDetail objPunchCard;

    public int getLoyalityTotalPoints() {
        return LoyalityTotalPoints;
    }


    public void setLoyalityTotalPoints(int LoyalityTotalPoints) {
        LoyalityTotalPoints = LoyalityTotalPoints;
    }


    public PunchCardDetail getObjPunchCard() {
        return objPunchCard;
    }

    public void setObjPunchCard(PunchCardDetail objPunchCard) {
        this.objPunchCard = objPunchCard;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getLoyaltyTemplatePath() {
        return LoyaltyTemplatePath;
    }

    public void setLoyaltyTemplatePath(String LoyaltyTemplatePath) {
        LoyaltyTemplatePath = LoyaltyTemplatePath;
    }



    public boolean isRead() {
        return IsRead;
    }

    public void setIsRead(boolean isRead) {
        IsRead = isRead;
    }

    public String getLoyalityBarcode() {
        return LoyalityBarcode;
    }


    public void setLoyalityBarcode(String loyalityBarcode) {
        LoyalityBarcode = loyalityBarcode;
    }

    public boolean isSubscribedMerchant() {
        return IsSubscribedMerchant;
    }

    public void setIsSubscribedMerchant(boolean isSubscribedMerchant) {
        IsSubscribedMerchant = isSubscribedMerchant;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBusinessdescription() {
        return Businessdescription;
    }

    public void setBusinessdescription(String businessdescription) {
        Businessdescription = businessdescription;
    }

    public String getRefferCodepath() {
        return RefferCodepath;
    }

    public void setRefferCodepath(String refferCodepath) {
        RefferCodepath = refferCodepath;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getPrivateKey() {
        return PrivateKey;
    }

    public void setPrivateKey(String privateKey) {
        PrivateKey = privateKey;
    }



    public String getMultipleRole() {
        return MultipleRole;
    }

    public void setMultipleRole(String multipleRole) {
        MultipleRole = multipleRole;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getIndustryId() {
        return IndustryId;
    }

    public void setIndustryId(String industryId) {
        IndustryId = industryId;
    }

    public String getBusinessNumber() {
        return BusinessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        BusinessNumber = businessNumber;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getIsEmployee() {
        return IsEmployee;
    }

    public void setIsEmployee(String isEmployee) {
        IsEmployee = isEmployee;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }




    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMerchantPoint() {
        return MerchantPoint;
    }

    public void setMerchantPoint(String merchantPoint) {
        MerchantPoint = merchantPoint;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getBusinessPhoneNumber() {
        return BusinessPhoneNumber;
    }

    public void setBusinessPhoneNumber(String businessPhoneNumber) {
        BusinessPhoneNumber = businessPhoneNumber;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDateModified() {
        return DateModified;
    }

    public void setDateModified(String dateModified) {
        DateModified = dateModified;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getReferralId() {
        return ReferralId;
    }

    public void setReferralId(String referralId) {
        ReferralId = referralId;
    }

    public String getIsAuthenticated() {
        return IsAuthenticated;
    }

    public void setIsAuthenticated(String isAuthenticated) {
        IsAuthenticated = isAuthenticated;
    }

    public String getReferralCode() {
        return ReferralCode;
    }

    public void setReferralCode(String referralCode) {
        ReferralCode = referralCode;
    }

    public String getIndustry() {
        return Industry;
    }

    public void setIndustry(String industry) {
        Industry = industry;
    }

    public String getLoyalityCardName() {
        return LoyalityCardName;
    }

    public void setLoyalityCardName(String loyalityCardName) {
        LoyalityCardName = loyalityCardName;
    }

}

