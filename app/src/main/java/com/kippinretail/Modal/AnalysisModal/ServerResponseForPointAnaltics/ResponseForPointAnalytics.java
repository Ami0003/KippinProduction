package com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kippinretail.Modal.ModalResponse;

/**
 * Created by sandeep.singh on 7/16/2016.
 */
public class ResponseForPointAnalytics extends ModalResponse{
    @SerializedName("EndDate")
    @Expose
    private String EndDate;
    @SerializedName("StartDate")
    @Expose
    private String StartDate;
    @SerializedName("Date")
    @Expose
    private String Date;
    @SerializedName("Username")
    @Expose
    private String Username;

    @SerializedName("UserId")
    @Expose
    private String UserId;
    @SerializedName("Points")
    @Expose
    private String Points;


    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
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

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }
}
