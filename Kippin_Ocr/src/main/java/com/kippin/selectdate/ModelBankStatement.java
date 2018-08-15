package com.kippin.selectdate;

import com.kippin.webclient.model.TemplateData;

import java.io.Serializable;

/**
 * Created by gaganpreet.singh on 1/28/2016.
 */

public class ModelBankStatement extends TemplateData implements Serializable
{
    private double Credit;

    private double Total;

    private double Debit;

    private String TransactionType;

    private String ResponseMessage;

    private double GSTtax;

    private double QSTtax;

    private double HSTtax;

    private double PSTtax;

    private String CategoryId;

    private String Date;

    private String OcrDataList;

    private String AccountType;

    private String Expense;

    private String Description;

    private String Comments;

    private String ClassificationId;

    private String ClassificationDescription;

    private String ExpenseId;

    private double BillTotal;

    private String IsAccepted;

    private String ResponseCode;

    private String UserId;

    private String IsSaved;

    private String Status;

    private String Category;

    private String Vendor;

    private String ClassificationType;

    private String BankId;

    private String Id;

    private String Purpose;

    private String BillData;

    private String StatusId;

    private boolean IsAutoClassified;

    private boolean IsNewBillAttached;
    private boolean IsCanadian;
    public boolean isIsCanadian() {
        return IsCanadian;
    }

    public void setIsCanadian(boolean IsCanadian) {
        this.IsCanadian = IsCanadian;
    }

    public boolean isIsNewBillAttached() {
        return IsNewBillAttached;
    }

    public void setIsNewBillAttached(boolean IsNewBillAttached) {
        this.IsNewBillAttached = IsNewBillAttached;
    }

    public boolean getIsAutoClassified() {
        return IsAutoClassified;
    }

    public void setIsAutoClassified(boolean IsAutoClassified) {
        IsAutoClassified = IsAutoClassified;
    }
    public String getClassificationDescription() {
        return ClassificationDescription;
    }

    public void setClassificationDescription(String classificationDescription) {
        ClassificationDescription = classificationDescription;
    }

    public double getCredit() {
        return Credit;
    }

    public void setCredit(double credit) {
        Credit = credit;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public double getDebit() {
        return Debit;
    }

    public void setDebit(double debit) {
        Debit = debit;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOcrDataList() {
        return OcrDataList;
    }

    public void setOcrDataList(String ocrDataList) {
        OcrDataList = ocrDataList;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getExpense() {
        return Expense;
    }

    public void setExpense(String expense) {
        Expense = expense;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getClassificationId() {
        return ClassificationId;
    }

    public void setClassificationId(String classificationId) {
        ClassificationId = classificationId;
    }

    public String getStatusId ()
    {
        return StatusId;
    }

    public void setStatusId (String StatusId)
    {
        this.StatusId = StatusId;
    }
    public String getExpenseId() {
        return ExpenseId;
    }

    public void setExpenseId(String expenseId) {
        ExpenseId = expenseId;
    }

    public double getBillTotal() {
        return BillTotal;
    }

    public void setBillTotal(double billTotal) {
        BillTotal = billTotal;
    }

    public String getIsAccepted() {
        return IsAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        IsAccepted = isAccepted;
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

    public String getIsSaved() {
        return IsSaved;
    }

    public void setIsSaved(String isSaved) {
        IsSaved = isSaved;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public String getClassificationType() {
        return ClassificationType;
    }

    public void setClassificationType(String classificationType) {
        ClassificationType = classificationType;
    }

    public String getBankId() {
        return BankId;
    }

    public void setBankId(String bankId) {
        BankId = bankId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getBillData() {
        return BillData;
    }

    public void setBillData(String billData) {
        BillData = billData;
    }

    public double getGSTtax() {
        return GSTtax;
    }

    public void setGSTtax(double GSTtax) {
        this.GSTtax = GSTtax;
    }

    public double getQSTtax() {
        return QSTtax;
    }

    public void setQSTtax(double QSTtax) {
        this.QSTtax = QSTtax;
    }

    public double getHSTtax() {
        return HSTtax;
    }

    public void setHSTtax(double HSTtax) {
        this.HSTtax = HSTtax;
    }

    public double getPSTtax() {
        return PSTtax;
    }

    public void setPSTtax(double PSTtax) {
        this.PSTtax = PSTtax;
    }


}
