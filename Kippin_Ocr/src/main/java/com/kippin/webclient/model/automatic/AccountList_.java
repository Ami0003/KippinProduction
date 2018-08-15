
package com.kippin.webclient.model.automatic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kippin.webclient.model.*;

public class AccountList_ {

    @SerializedName("siteName")
    @Expose
    private Object siteName;
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("siteAccountId")
    @Expose
    private Integer siteAccountId;
    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("ResponseMessage")
    @Expose
    private Object responseMessage;
    @SerializedName("accountDisplayName")
    @Expose
    private com.kippin.webclient.model.AccountDisplayName accountDisplayName;
    @SerializedName("itemAccountId")
    @Expose
    private String itemAccountId;
    @SerializedName("accountName")
    @Expose
    private String accountName;
    @SerializedName("currentBalance")
    @Expose
    private Object currentBalance;
    @SerializedName("accountBalance")
    @Expose
    private Object accountBalance;
    @SerializedName("availableBalance")
    @Expose
    private Object availableBalance;
    @SerializedName("totalBalance")
    @Expose
    private Object totalBalance;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("createdAsDateTime")
    @Expose
    private String createdAsDateTime;
    @SerializedName("lastUpdated")
    @Expose
    private Integer lastUpdated;
    @SerializedName("accountNumber")
    @Expose
    private Object accountNumber;
    @SerializedName("accountHolder")
    @Expose
    private Object accountHolder;
    @SerializedName("lastPaymentAmount")
    @Expose
    private Object lastPaymentAmount;
    @SerializedName("lastPaymentDate")
    @Expose
    private Object lastPaymentDate;
    @SerializedName("loans")
    @Expose
    private Object loans;
    @SerializedName("loanAccountNumber")
    @Expose
    private Object loanAccountNumber;
    @SerializedName("bills")
    @Expose
    private Object bills;
    @SerializedName("holdings")
    @Expose
    private Object holdings;
    @SerializedName("runningBalance")
    @Expose
    private Object runningBalance;
    @SerializedName("amountDue")
    @Expose
    private Object amountDue;
    @SerializedName("lastPayment")
    @Expose
    private Object lastPayment;
    @SerializedName("rewardActivities")
    @Expose
    private Object rewardActivities;
    @SerializedName("rewardsBalances")
    @Expose
    private Object rewardsBalances;
    @SerializedName("insurancePolicys")
    @Expose
    private Object insurancePolicys;
    @SerializedName("cash")
    @Expose
    private Object cash;
    @SerializedName("planName")
    @Expose
    private Object planName;

    public Object getSiteName() {
        return siteName;
    }

    public void setSiteName(Object siteName) {
        this.siteName = siteName;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getSiteAccountId() {
        return siteAccountId;
    }

    public void setSiteAccountId(Integer siteAccountId) {
        this.siteAccountId = siteAccountId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Object getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(Object responseMessage) {
        this.responseMessage = responseMessage;
    }

    public com.kippin.webclient.model.AccountDisplayName getAccountDisplayName() {
        return accountDisplayName;
    }

    public void setAccountDisplayName(com.kippin.webclient.model.AccountDisplayName accountDisplayName) {
        this.accountDisplayName = accountDisplayName;
    }

    public String getItemAccountId() {
        return itemAccountId;
    }

    public void setItemAccountId(String itemAccountId) {
        this.itemAccountId = itemAccountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Object getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Object currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Object getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Object accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Object getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Object availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Object getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Object totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public String getCreatedAsDateTime() {
        return createdAsDateTime;
    }

    public void setCreatedAsDateTime(String createdAsDateTime) {
        this.createdAsDateTime = createdAsDateTime;
    }

    public Integer getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Integer lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Object getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Object accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Object getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(Object accountHolder) {
        this.accountHolder = accountHolder;
    }

    public Object getLastPaymentAmount() {
        return lastPaymentAmount;
    }

    public void setLastPaymentAmount(Object lastPaymentAmount) {
        this.lastPaymentAmount = lastPaymentAmount;
    }

    public Object getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(Object lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public Object getLoans() {
        return loans;
    }

    public void setLoans(Object loans) {
        this.loans = loans;
    }

    public Object getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(Object loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public Object getBills() {
        return bills;
    }

    public void setBills(Object bills) {
        this.bills = bills;
    }

    public Object getHoldings() {
        return holdings;
    }

    public void setHoldings(Object holdings) {
        this.holdings = holdings;
    }

    public Object getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(Object runningBalance) {
        this.runningBalance = runningBalance;
    }

    public Object getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(Object amountDue) {
        this.amountDue = amountDue;
    }

    public Object getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(Object lastPayment) {
        this.lastPayment = lastPayment;
    }

    public Object getRewardActivities() {
        return rewardActivities;
    }

    public void setRewardActivities(Object rewardActivities) {
        this.rewardActivities = rewardActivities;
    }

    public Object getRewardsBalances() {
        return rewardsBalances;
    }

    public void setRewardsBalances(Object rewardsBalances) {
        this.rewardsBalances = rewardsBalances;
    }

    public Object getInsurancePolicys() {
        return insurancePolicys;
    }

    public void setInsurancePolicys(Object insurancePolicys) {
        this.insurancePolicys = insurancePolicys;
    }

    public Object getCash() {
        return cash;
    }

    public void setCash(Object cash) {
        this.cash = cash;
    }

    public Object getPlanName() {
        return planName;
    }

    public void setPlanName(Object planName) {
        this.planName = planName;
    }

}
