package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 5/25/2016.
 */
public class ModalAutomaticStatement extends TemplateData{
    private LastPayment lastPayment;

    private String loanAccountNumber;

    private String holdings;

    private String planName;

    private CurrentBalance currentBalance;

    private String BankName;

    private String ResponseMessage;

    private String rewardActivities;

    private String siteAccountId;

    private String accountNumber;

    private String created;

    private String lastPaymentAmount;

    private String siteName;

    private String insurancePolicys;

    private String accountBalance;

    private String rewardsBalances;

    private String itemAccountId;

    private String createdAsDateTime;

    private String totalBalance;

    private String lastUpdated;

    private String ResponseCode;

    private AvailableBalance availableBalance;

    private AccountDisplayName accountDisplayName;

    private RunningBalance runningBalance;

    private String loans;

    private String accountName;

    private String accountHolder;

    private LastPaymentDate lastPaymentDate;

    private String cash;

    private String amountDue;

    private String bills;

    public LastPayment getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(LastPayment lastPayment) {
        this.lastPayment = lastPayment;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public String getHoldings() {
        return holdings;
    }

    public void setHoldings(String holdings) {
        this.holdings = holdings;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public CurrentBalance getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(CurrentBalance currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getRewardActivities() {
        return rewardActivities;
    }

    public void setRewardActivities(String rewardActivities) {
        this.rewardActivities = rewardActivities;
    }

    public String getSiteAccountId() {
        return siteAccountId;
    }

    public void setSiteAccountId(String siteAccountId) {
        this.siteAccountId = siteAccountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastPaymentAmount() {
        return lastPaymentAmount;
    }

    public void setLastPaymentAmount(String lastPaymentAmount) {
        this.lastPaymentAmount = lastPaymentAmount;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getInsurancePolicys() {
        return insurancePolicys;
    }

    public void setInsurancePolicys(String insurancePolicys) {
        this.insurancePolicys = insurancePolicys;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getRewardsBalances() {
        return rewardsBalances;
    }

    public void setRewardsBalances(String rewardsBalances) {
        this.rewardsBalances = rewardsBalances;
    }

    public String getItemAccountId() {
        return itemAccountId;
    }

    public void setItemAccountId(String itemAccountId) {
        this.itemAccountId = itemAccountId;
    }

    public String getCreatedAsDateTime() {
        return createdAsDateTime;
    }

    public void setCreatedAsDateTime(String createdAsDateTime) {
        this.createdAsDateTime = createdAsDateTime;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public AvailableBalance getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(AvailableBalance availableBalance) {
        this.availableBalance = availableBalance;
    }

    public AccountDisplayName getAccountDisplayName() {
        return accountDisplayName;
    }

    public void setAccountDisplayName(AccountDisplayName accountDisplayName) {
        this.accountDisplayName = accountDisplayName;
    }

    public RunningBalance getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(RunningBalance runningBalance) {
        this.runningBalance = runningBalance;
    }

    public String getLoans() {
        return loans;
    }

    public void setLoans(String loans) {
        this.loans = loans;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public LastPaymentDate getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(LastPaymentDate lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(String amountDue) {
        this.amountDue = amountDue;
    }

    public String getBills() {
        return bills;
    }

    public void setBills(String bills) {
        this.bills = bills;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [lastPayment = "+lastPayment+", loanAccountNumber = "+loanAccountNumber+", holdings = "+holdings+", planName = "+planName+", currentBalance = "+currentBalance+", BankName = "+BankName+", ResponseMessage = "+ResponseMessage+", rewardActivities = "+rewardActivities+", siteAccountId = "+siteAccountId+", accountNumber = "+accountNumber+", created = "+created+", lastPaymentAmount = "+lastPaymentAmount+", siteName = "+siteName+", insurancePolicys = "+insurancePolicys+", accountBalance = "+accountBalance+", rewardsBalances = "+rewardsBalances+", itemAccountId = "+itemAccountId+", createdAsDateTime = "+createdAsDateTime+", totalBalance = "+totalBalance+", lastUpdated = "+lastUpdated+", ResponseCode = "+ResponseCode+", availableBalance = "+availableBalance+", accountDisplayName = "+accountDisplayName+", runningBalance = "+runningBalance+", loans = "+loans+", accountName = "+accountName+", accountHolder = "+accountHolder+", lastPaymentDate = "+lastPaymentDate+", cash = "+cash+", amountDue = "+amountDue+", bills = "+bills+"]";
    }
}
