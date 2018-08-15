package com.kippinretail.Modal.ModelToComparePrize;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep.singh on 8/22/2016.
 */
public class ObjFirstMonth {
    @SerializedName("ResponseCode")     @Expose
    private String ResponseCode;
    @SerializedName("UserId")     @Expose
    private String UserId;
    @SerializedName("Country")     @Expose
    private String Country;

    private List<ObjGiftcardDetails> objGiftcardDetails;

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

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public List<ObjGiftcardDetails> getObjGiftcardDetails() {
        return objGiftcardDetails;
    }

    public void setObjGiftcardDetails(List<ObjGiftcardDetails> objGiftcardDetails) {
        this.objGiftcardDetails = objGiftcardDetails;
    }
}
