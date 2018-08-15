package com.kippinretail.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gaganpreet.singh on 4/7/2016.
 */
public class ResponseModal {
    @SerializedName("OwnAmount")
    @Expose
    private float ownAmount=0;
    public float getOwnAmount() {
        return ownAmount;
    }

    public void setOwnAmount(float ownAmount) {
        this.ownAmount = ownAmount;
    }


    private String ResponseCode , UserId, ResponseMessage;

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

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }
}
