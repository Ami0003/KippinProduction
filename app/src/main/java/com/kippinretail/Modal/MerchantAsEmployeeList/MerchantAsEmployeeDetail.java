package com.kippinretail.Modal.MerchantAsEmployeeList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by sandeep.saini on 4/4/2016.
 */
public class MerchantAsEmployeeDetail {

    @SerializedName("Id")
    @Expose
    private String Id;

    @SerializedName("MerchantId")
    @Expose
    private String MerchantId;

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("UserId")
    @Expose
    private String UserId;

    @SerializedName("IsAuthenticated")
    @Expose
    private boolean IsAuthenticated;

    @SerializedName("MerchantUsername")
    @Expose
    private String MerchantUsername;

    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

    @SerializedName("ProfileImage")
    @Expose
    private String ProfileImage;

    @SerializedName("Businessname")
    @Expose
    private String Businessname;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBusinessname() {
        return Businessname;
    }

    public void setBusinessname(String businessname) {
        Businessname = businessname;
    }

    public boolean isAuthenticated() {
        return IsAuthenticated;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
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

    public boolean getIsAuthenticated() {
        return IsAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        IsAuthenticated = isAuthenticated;
    }

    public String getMerchantUsername() {
        return MerchantUsername;
    }

    public void setMerchantUsername(String merchantUsername) {
        MerchantUsername = merchantUsername;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }
}
