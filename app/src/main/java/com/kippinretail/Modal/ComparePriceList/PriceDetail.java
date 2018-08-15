package com.kippinretail.Modal.ComparePriceList;

/**
 * Created by sandeep.saini on 3/15/2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class PriceDetail {
    @SerializedName("City")
    @Expose
    private Object City;
//
//    @SerializedName("ObjMerchantDetails")
//    @Expose
//    private String ObjMerchantDetails;
//
//    @SerializedName("DateModified")
//    @Expose
//    private String DateModified;
//
   @SerializedName("Location")
    @Expose
    private Object Location;

    @SerializedName("Price")
    @Expose
    private String Price;

    @SerializedName("ItemName")
    @Expose
    private String ItemName;


    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    @SerializedName("Quantity")
    @Expose

    private String Quantity;
//    @SerializedName("IsDeleted")
//    @Expose
//    private String IsDeleted;

    @SerializedName("BusinessName")
    @Expose
    private String BusinessName;

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }
    //    @SerializedName("ResponseCode")
//    @Expose
//    private String ResponseCode;
//
//    @SerializedName("Id")
//    @Expose
//    private String Id;
//
//    @SerializedName("UserId")
//    @Expose
//    private String UserId;
//


    public Object getLocation() {
        return Location;
   }
//
    public void setLocation(Object Location) {
        Location = Location;
    }
//
   public Object getCity() {
       return City;
    }
//
   public void setCity(Object City) {
       City = City;
    }
//
//    public String getDateModified() {
//        return DateModified;
//    }
//
//    public void setDateModified(String dateModified) {
//        DateModified = dateModified;
//    }
//
//    public String getMerchantId() {
//        return MerchantId;
//    }
//
//    public void setMerchantId(String merchantId) {
//        MerchantId = merchantId;
//    }
//
//    public String getPrice() {
//        return Price;
//    }
//
//    public void setPrice(String price) {
//        Price = price;
//    }
//
//    public String getItemName() {
//        return ItemName;
//    }
//
//    public void setItemName(String itemName) {
//        ItemName = itemName;
//    }
//
//    public String getIsDeleted() {
//        return IsDeleted;
//    }
//
//    public void setIsDeleted(String isDeleted) {
//        IsDeleted = isDeleted;
//    }
//
//    public String getBusinessName() {
//        return BusinessName;
//    }
//
//    public void setBusinessName(String businessName) {
//        BusinessName = businessName;
//    }
//
//    public String getResponseCode() {
//        return ResponseCode;
//    }
//
//    public void setResponseCode(String responseCode) {
//        ResponseCode = responseCode;
//    }
//
//    public String getId() {
//        return Id;
//    }
//
//    public void setId(String id) {
//        Id = id;
//    }
//
//    public String getUserId() {
//        return UserId;
//    }
//
//    public void setUserId(String userId) {
//        UserId = userId;
//    }
//
//    public String getResponseMessage() {
//        return ResponseMessage;
//    }
//
//    public void setResponseMessage(String responseMessage) {
//        ResponseMessage = responseMessage;
//    }
}
