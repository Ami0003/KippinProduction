package com.kippinretail.Modal.PointList;

/**
 * Created by sandeep.saini on 3/26/2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kippinretail.Modal.ModalResponse;

public class PointDetail extends ModalResponse {

    @SerializedName("FreindName")
    @Expose
    private String FreindName;

    @SerializedName("UserId")
    @Expose
    private String UserId;

    @SerializedName("Points")
    @Expose
    private String Points;

    @SerializedName("TermsConditions")
    private String TermsConditions;
    @SerializedName("MerchantName")
    @Expose
    private String MerchantName;

    private String LoyalityBarCode;
    private Boolean IsLoyalitySignUp;
    private String UserLoyalityBarCode;

    public String getTermsConditions() {
        return TermsConditions;
    }

    public void setTermsConditions(String termsConditions) {
        TermsConditions = termsConditions;
    }
    public String getLoyalityBarCode() {
        return LoyalityBarCode;
    }

    public void setLoyalityBarCode(String loyalityBarCode) {
        LoyalityBarCode = loyalityBarCode;
    }

    public Boolean getIsLoyalitySignUp() {
        return IsLoyalitySignUp;
    }

    public void setIsLoyalitySignUp(Boolean isLoyalitySignUp) {
        IsLoyalitySignUp = isLoyalitySignUp;
    }

    public String getUserLoyalityBarCode() {
        return UserLoyalityBarCode;
    }

    public void setUserLoyalityBarCode(String userLoyalityBarCode) {
        UserLoyalityBarCode = userLoyalityBarCode;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getFreindName() {
        return FreindName;
    }

    public void setFreindName(String freindName) {
        FreindName = freindName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String merchantName) {
        MerchantName = merchantName;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }
}
