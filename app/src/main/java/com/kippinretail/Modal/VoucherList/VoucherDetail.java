package com.kippinretail.Modal.VoucherList;

/**
 * Created by sandeep.saini on 3/18/2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VoucherDetail implements Serializable
{
    @SerializedName("VoucherImage")
    @Expose
    private String VoucherImage;

    @SerializedName("EndDate")
    @Expose
    private String EndDate;

    @SerializedName("StartDate")
    @Expose
    private String StartDate;

    @SerializedName("DateModified")
    @Expose
    private String DateModified;

    @SerializedName("MerchantId")
    @Expose
    private String MerchantId;

    @SerializedName("DateCreated")
    @Expose
    private String DateCreated;

    @SerializedName("IsDeleted")
    @Expose
    private String IsDeleted;

    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode;

    @SerializedName("Id")
    @Expose
    private String Id;

    @SerializedName("UserId")
    @Expose
    private String UserId;

    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

    @SerializedName("VoucherName")
    @Expose
    private String VoucherName;

    @SerializedName("IsRead")
    @Expose
    private boolean IsRead;

    public boolean isRead() {
        return IsRead;
    }

    public void setIsRead(boolean isRead) {
        IsRead = isRead;
    }

    public String getVoucherImage() {
        return VoucherImage;
    }

    public void setVoucherImage(String voucherImage) {
        VoucherImage = voucherImage;
    }

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

    public String getDateModified() {
        return DateModified;
    }

    public void setDateModified(String dateModified) {
        DateModified = dateModified;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        IsDeleted = isDeleted;
    }

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

    public String getVoucherName() {
        return VoucherName;
    }

    public void setVoucherName(String voucherName) {
        VoucherName = voucherName;
    }
}
