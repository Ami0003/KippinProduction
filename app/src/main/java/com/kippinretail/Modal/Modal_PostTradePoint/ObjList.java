package com.kippinretail.Modal.Modal_PostTradePoint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep.saini on 5/10/2016.
 */
public class ObjList {
    @SerializedName("MerchantId")
    @Expose
    private int MerchantId;

    @SerializedName("LoyalityBarCode")
    @Expose
    private String LoyalityBarCode;

    @SerializedName("FriendId")
    @Expose
    private int FriendId;

    @SerializedName("UserId")
    @Expose
    private int UserId;

    @SerializedName("Points")
    @Expose
    private int Points;

    public int getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(int merchantId) {
        MerchantId = merchantId;
    }

    public String getLoyalityBarCode() {
        return LoyalityBarCode;
    }

    public void setLoyalityBarCode(String loyalityBarCode) {
        LoyalityBarCode = loyalityBarCode;
    }

    public int getFriendId() {
        return FriendId;
    }

    public void setFriendId(int friendId) {
        FriendId = friendId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }
}
