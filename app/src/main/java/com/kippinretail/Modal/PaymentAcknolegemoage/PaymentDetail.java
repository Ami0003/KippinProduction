package com.kippinretail.Modal.PaymentAcknolegemoage;

/**
 * Created by sandeep.saini on 3/30/2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class PaymentDetail
{
    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("UserId")
    @Expose
    private String UserId;

    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

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
