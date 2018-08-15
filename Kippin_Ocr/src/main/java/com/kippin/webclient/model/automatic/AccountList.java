
package com.kippin.webclient.model.automatic;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountList {

    @SerializedName("AccountType")
    @Expose
    private String accountType;
    @SerializedName("AccountList")
    @Expose
    private List<AccountList_> accountList = null;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<AccountList_> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountList_> accountList) {
        this.accountList = accountList;
    }

}
