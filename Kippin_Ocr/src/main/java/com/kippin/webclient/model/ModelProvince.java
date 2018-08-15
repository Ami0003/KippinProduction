package com.kippin.webclient.model;

/**
 * Created by dilip.singh on 1/29/2016.
 */
public class ModelProvince  extends TemplateData {


    String Id;
    String ProvinceName;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    String ResponseCode;
    String ResponseMessage;
    String UserId;



    @Override
    public String toString()
    {
        return "ClassPojo [ResponseCode = "+ResponseCode+", Id = "+Id+", UserId = "+UserId+", ProvinceName = "+ProvinceName+", ResponseMessage = "+ResponseMessage+"]";
    }


}
