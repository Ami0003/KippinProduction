
package com.kippin.webclient.model.automatic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Common {


    private Object bankType;

    public Object getBankType() {
        return bankType;
    }

    public void setBankType(Object bankType) {
        this.bankType = bankType;
    }
    private Integer bankId;

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }
    private Object bankName;

    private Integer siteAccountId;

    private Object itemAccountId;

    private Object accountName;


    private Object accountNumber;

    public Object getItemAccountId() {
        return itemAccountId;
    }

    public void setItemAccountId(Object itemAccountId) {
        this.itemAccountId = itemAccountId;
    }



    public Object getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Object accountNumber) {
        this.accountNumber = accountNumber;
    }



    public Object getBankName() {
        return bankName;
    }

    public void setBankName(Object bankName) {
        this.bankName = bankName;
    }



    public Integer getSiteAccountId() {
        return siteAccountId;
    }

    public void setSiteAccountId(Integer siteAccountId) {
        this.siteAccountId = siteAccountId;
    }



    public Object getAccountName() {
        return accountName;
    }

    public void setAccountName(Object accountName) {
        this.accountName = accountName;
    }

}
