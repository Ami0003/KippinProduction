package com.kippinretail.Modal.PointsData;

/**
 * Created by kamaljeet.singh on 3/24/2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointsDataa {


    @SerializedName("Month")
    @Expose
    private Integer Month;
    @SerializedName("Year")
    @Expose
    private Integer Year;

    private String EmployeeNumber;

    private String Date;

    private String ResponseCode;

    private String UserId;

    private String InvoiceNumber;

    private String ResponseMessage;
    private String PointType;

    public String getPointType() {
        return PointType;
    }

    public void setPointType(String PointType) {
        PointType = PointType;
    }


    private String Point;

    public Integer getMonth() {
        return Month;
    }

    public void setMonth(Integer month) {
        Month = month;
    }

    public Integer getYear() {
        return Year;
    }

    public void setYear(Integer year) {
        Year = year;
    }

    public String getEmployeeNumber() {
        return EmployeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        EmployeeNumber = employeeNumber;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getResponseCode() {
        return Integer.parseInt(ResponseCode);
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

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getPoint() {
        return Point;
    }

    public void setPoint(String point) {
        Point = point;
    }
}