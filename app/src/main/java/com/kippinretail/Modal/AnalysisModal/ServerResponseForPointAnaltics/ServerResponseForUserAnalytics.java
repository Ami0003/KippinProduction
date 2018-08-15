package com.kippinretail.Modal.AnalysisModal.ServerResponseForPointAnaltics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kippinretail.Modal.ModalResponse;

/**
 * Created by sandeep.singh on 7/20/20@SerializedName("")     @Expose6.
 */
public class ServerResponseForUserAnalytics extends ModalResponse {
    @SerializedName("PointAllocated")
    @Expose
    private String PointAllocated;
    @SerializedName("LoyalityBarcode")
    @Expose
    private String LoyalityBarcode;
    @SerializedName("MerchantId")
    @Expose
    private String MerchantId;


    @SerializedName("UserId")
    @Expose
    private String UserId;
    @SerializedName("PointUsed")
    @Expose
    private String PointUsed;
    @SerializedName("LoyalityCardName")
    @Expose
    private String LoyalityCardName;

    public String getPointAllocated() {
        return PointAllocated;
    }

    public void setPointAllocated(String pointAllocated) {
        PointAllocated = pointAllocated;
    }

    public String getLoyalityBarcode() {
        return LoyalityBarcode;
    }

    public void setLoyalityBarcode(String loyalityBarcode) {
        LoyalityBarcode = loyalityBarcode;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPointUsed() {
        return PointUsed;
    }

    public void setPointUsed(String pointUsed) {
        PointUsed = pointUsed;
    }

    public String getLoyalityCardName() {
        return LoyalityCardName;
    }

    public void setLoyalityCardName(String loyalityCardName) {
        LoyalityCardName = loyalityCardName;
    }
}
