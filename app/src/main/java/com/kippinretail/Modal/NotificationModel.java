package com.kippinretail.Modal;

/**
 * Created by sandeep.singh on 8/3/2016.
 */
public class NotificationModel {
    private boolean IsTradePoint;

    private boolean IstransferGiftCard;

    private boolean IsVoucher;

    private String ResponseCode;

    private boolean IsFriendRequest;

    private String UserId;

    private String Country;

    private boolean IsNewMerchant;

    private String ResponseMessage;

    private boolean IsNonKippinLoyalty;

    private boolean IsNonKippinPhysical;


    public boolean isTradePoint() {
        return IsTradePoint;
    }

    public void setIsTradePoint(boolean isTradePoint) {
        IsTradePoint = isTradePoint;
    }

    public boolean istransferGiftCard() {
        return IstransferGiftCard;
    }

    public void setIstransferGiftCard(boolean istransferGiftCard) {
        IstransferGiftCard = istransferGiftCard;
    }

    public boolean isVoucher() {
        return IsVoucher;
    }

    public void setIsVoucher(boolean isVoucher) {
        IsVoucher = isVoucher;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public boolean isFriendRequest() {
        return IsFriendRequest;
    }

    public void setIsFriendRequest(boolean isFriendRequest) {
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

    public boolean isNewMerchant() {
        return IsNewMerchant;
    }

    public void setIsNewMerchant(boolean isNewMerchant) {
        IsNewMerchant = isNewMerchant;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public boolean isNonKippinLoyalty() {
        return IsNonKippinLoyalty;
    }

    public void setIsNonKippinLoyalty(boolean isNonKippinLoyalty) {
        IsNonKippinLoyalty = isNonKippinLoyalty;
    }

    public boolean isNonKippinPhysical() {
        return IsNonKippinPhysical;
    }

    public void setIsNonKippinPhysical(boolean isNonKippinPhysical) {
        IsNonKippinPhysical = isNonKippinPhysical;
    }
}
