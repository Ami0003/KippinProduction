
package com.kippin.webclient.model.automatic;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModalAutomaticStatementAccountList {

    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("AccountList")
    @Expose
    private List<AccountList> accountList = null;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<AccountList> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountList> accountList) {
        this.accountList = accountList;
    }

}
