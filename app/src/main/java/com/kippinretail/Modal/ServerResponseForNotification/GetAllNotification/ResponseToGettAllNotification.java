package com.kippinretail.Modal.ServerResponseForNotification.GetAllNotification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kippinretail.Modal.ModalResponse;

/**
 * Created by sandeep.singh on 7/20/20@SerializedName("")     @Expose6.
 */
public class ResponseToGettAllNotification extends ModalResponse {
    @SerializedName("IsFirstLoyaltyCard")
    @Expose
    private boolean IsFirstLoyaltyCard;
    @SerializedName("DateCreated")
    @Expose
    private String DateCreated;
    @SerializedName("ProfileImage")
    @Expose
    private String ProfileImage;
    @SerializedName("BusinessName")
    @Expose
    private String BusinessName;

    @SerializedName("UserId")
    @Expose
    private String UserId;
    @SerializedName("Country")
    @Expose
    private String Country;

    @SerializedName("IsFirstPunchCard")
    @Expose
    private boolean IsFirstPunchCard;
    @SerializedName("DateModified")
    @Expose
    private String DateModified;
    @SerializedName("MerchantId")
    @Expose
    private String MerchantId;
    @SerializedName("Username")
    @Expose
    private String Username;
    @SerializedName("IsRead")
    @Expose
    private String IsRead;
    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("IsFirstGiftcard")
    @Expose
    private boolean IsFirstGiftcard;


    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
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

    public boolean isFirstLoyaltyCard() {
        return IsFirstLoyaltyCard;
    }

    public void setIsFirstLoyaltyCard(boolean isFirstLoyaltyCard) {
        IsFirstLoyaltyCard = isFirstLoyaltyCard;
    }

    public boolean isFirstPunchCard() {
        return IsFirstPunchCard;
    }

    public void setIsFirstPunchCard(boolean isFirstPunchCard) {
        IsFirstPunchCard = isFirstPunchCard;
    }

    public boolean isFirstGiftcard() {
        return IsFirstGiftcard;
    }

    public void setIsFirstGiftcard(boolean isFirstGiftcard) {
        IsFirstGiftcard = isFirstGiftcard;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }


    public String getDateModified() {
        return DateModified;
    }

    public void setDateModified(String dateModified) {
        DateModified = dateModified;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }


}
