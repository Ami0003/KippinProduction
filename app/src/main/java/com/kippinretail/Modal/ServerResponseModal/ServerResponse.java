package com.kippinretail.Modal.ServerResponseModal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep.saini on 5/6/2016.
 */
public class ServerResponse {
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
