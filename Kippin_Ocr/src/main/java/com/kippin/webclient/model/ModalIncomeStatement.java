package com.kippin.webclient.model;

import java.io.Serializable;

/**
 * Created by gaganpreet.singh on 4/2/2016.
 */
public class ModalIncomeStatement extends TemplateData implements Serializable {
    private ModalRevenueExpense[] objExpenseList;

    private String ResponseCode;

    private String UserId;

    private ModalRevenueExpense[] objRevenueList;

    private String ResponseMessage;

    private double NetIncome1;

    public double getNetIncome1() {
        return NetIncome1;
    }

    public void setNetIncome1(double netIncome1) {
        NetIncome1 = netIncome1;
    }

    public ModalRevenueExpense[] getObjExpenseList() {
        return objExpenseList;
    }

    public void setObjExpenseList(ModalRevenueExpense[] objExpenseList) {
        this.objExpenseList = objExpenseList;
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

    public ModalRevenueExpense[] getObjRevenueList() {
        return objRevenueList;
    }

    public void setObjRevenueList(ModalRevenueExpense[] objRevenueList) {
        this.objRevenueList = objRevenueList;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [objExpenseList = "+objExpenseList+", ResponseCode = "+ResponseCode+", UserId = "+UserId+", objRevenueList = "+objRevenueList+", ResponseMessage = "+ResponseMessage+"]";
    }
}


