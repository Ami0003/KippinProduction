
package com.kippinretail.Modal.AddressCheck;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressCheck {

    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
