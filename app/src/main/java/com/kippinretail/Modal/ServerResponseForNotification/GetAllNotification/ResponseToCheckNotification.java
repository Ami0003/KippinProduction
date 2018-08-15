package com.kippinretail.Modal.ServerResponseForNotification.GetAllNotification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep.singh on 7/2Boolean/20Boolean6.
 */
public class ResponseToCheckNotification {
    @SerializedName("IsTradePoint")
    @Expose
    private Boolean IsTradePoint;
    @SerializedName("IstransferGiftCard")
    @Expose
    private Boolean IstransferGiftCard;
    @SerializedName("IsVoucher")
    @Expose
    private Boolean IsVoucher;
    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;
    @SerializedName("IsFriendRequest")
    @Expose
    private Boolean IsFriendRequest;
    @SerializedName("UserId")
    @Expose
    private String UserId;
    @SerializedName("Country")
    @Expose
    private String Country;
    @SerializedName("IsNewMerchant")
    @Expose
    private Boolean IsNewMerchant;
    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

    public Boolean getIsTradePoint() {
        return IsTradePoint;
    }

    public void setIsTradePoint(Boolean isTradePoint) {
        IsTradePoint = isTradePoint;
    }

    public Boolean getIstransferGiftCard() {
        return IstransferGiftCard;
    }

    public void setIstransferGiftCard(Boolean istransferGiftCard) {
        IstransferGiftCard = istransferGiftCard;
    }

    public Boolean getIsVoucher() {
        return IsVoucher;
    }

    public void setIsVoucher(Boolean isVoucher) {
        IsVoucher = isVoucher;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public Boolean getIsFriendRequest() {
        return IsFriendRequest;
    }

    public void setIsFriendRequest(Boolean isFriendRequest) {
        IsFriendRequest = isFriendRequest;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public Boolean getIsNewMerchant() {
        return IsNewMerchant;
    }

    public void setIsNewMerchant(Boolean isNewMerchant) {
        IsNewMerchant = isNewMerchant;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }
}
