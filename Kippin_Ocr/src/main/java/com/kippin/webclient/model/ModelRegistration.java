package com.kippin.webclient.model;

/**
 * Created by dilip.singh on 1/22/2016.
 */
public class ModelRegistration  extends TemplateData{


    @Override
    public String toString() {

        return "ModelRegistration[" +
                "ResponseCode='" + ResponseCode + '\'' +
                ", ResponseMessage='" + ResponseMessage + '\'' +
                ", UserId='" + UserId + '\'' +
                ']';

    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public String getUserId() {
        return UserId;
    }

    String ResponseCode;

    public void setResponseMessage(String responseMessage) {
        this.ResponseMessage = responseMessage;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }

    public void setResponseCode(String responseCode) {
        this.ResponseCode = responseCode;
    }

    String ResponseMessage;
        String UserId;

}
