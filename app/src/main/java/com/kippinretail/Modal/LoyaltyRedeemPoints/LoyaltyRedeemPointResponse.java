
package com.kippinretail.Modal.LoyaltyRedeemPoints;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoyaltyRedeemPointResponse implements Serializable
{

    @SerializedName("Points")
    @Expose
    private int points;
    @SerializedName("DollarAmount")
    @Expose
    private int dollarAmount;
    @SerializedName("MaxAmountUse")
    @Expose
    private int maxAmountUse;
    @SerializedName("UserPoints")
    @Expose
    private int userPoints;
    @SerializedName("ResponseCode")
    @Expose
    private int responseCode;
    @SerializedName("UserId")
    @Expose
    private int userId;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    private final static long serialVersionUID = 7151468408674082473L;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDollarAmount() {
        return dollarAmount;
    }

    public void setDollarAmount(int dollarAmount) {
        this.dollarAmount = dollarAmount;
    }

    public int getMaxAmountUse() {
        return maxAmountUse;
    }

    public void setMaxAmountUse(int maxAmountUse) {
        this.maxAmountUse = maxAmountUse;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(int userPoints) {
        this.userPoints = userPoints;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
