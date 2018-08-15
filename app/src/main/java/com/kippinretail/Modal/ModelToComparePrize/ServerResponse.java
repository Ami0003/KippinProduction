package com.kippinretail.Modal.ModelToComparePrize;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep.singh on 8/22/20@SerializedName("")     @Expose6.
 */
public class ServerResponse {
    @SerializedName("objFirstMonth")     @Expose
    private List<ObjFirstMonth> objFirstMonth;
    @SerializedName("ResponseCode")     @Expose
    private String ResponseCode;
    @SerializedName("objSecondMonth")     @Expose
    private List<ObjSecondMonth> objSecondMonth;
    @SerializedName("objthirdMonth")     @Expose
    private List<ObjthirdMonth> objthirdMonth;
    @SerializedName("UserId")     @Expose
    private String UserId;

    @SerializedName("ResponseMessage")     @Expose
    private String ResponseMessage;

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }


    public List<ObjFirstMonth> getObjFirstMonth() {
        return objFirstMonth;
    }

    public void setObjFirstMonth(List<ObjFirstMonth> objFirstMonth) {
        this.objFirstMonth = objFirstMonth;
    }

    public List<ObjSecondMonth> getObjSecondMonth() {
        return objSecondMonth;
    }

    public void setObjSecondMonth(List<ObjSecondMonth> objSecondMonth) {
        this.objSecondMonth = objSecondMonth;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }



    public List<ObjthirdMonth> getObjthirdMonth() {
        return objthirdMonth;
    }

    public void setObjthirdMonth(List<ObjthirdMonth> objthirdMonth) {
        this.objthirdMonth = objthirdMonth;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
