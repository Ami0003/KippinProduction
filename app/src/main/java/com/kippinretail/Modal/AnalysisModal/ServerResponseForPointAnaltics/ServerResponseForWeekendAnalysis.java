package com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep.singh on 7/@SerializedName("")     @Expose6/20@SerializedName("")     @Expose6.
 */
public class ServerResponseForWeekendAnalysis {
    @SerializedName("objWeekendDays")
    @Expose
    private String[] objWeekendDays;

    @SerializedName("objWeekDays")
    @Expose
    private String[] objWeekDays;

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("UserId")
    @Expose
    private String UserId;
    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

    public String[] getObjWeekendDays() {
        return objWeekendDays;
    }

    public void setObjWeekendDays(String[] objWeekendDays) {
        this.objWeekendDays = objWeekendDays;
    }

    public String[] getObjWeekDays() {
        return objWeekDays;
    }

    public void setObjWeekDays(String[] objWeekDays) {
        this.objWeekDays = objWeekDays;
    }

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
