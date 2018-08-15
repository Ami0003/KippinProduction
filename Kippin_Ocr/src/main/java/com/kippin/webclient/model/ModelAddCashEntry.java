package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 3/19/2016.
 */
public class ModelAddCashEntry {
    public int UserId ;
    public String Description ;
    public String Comment ;
    public String Classification;
    public String CashBillDate;
    public double Credit ;
    public double Debit ;
    public double Total ;
    public String Purpose ;
    public String Vendor ;
    public int CategoryId ;
    public String BillPath ;
    public double BillTax ;
    public double BillTotal ;
    public String ImageName ;
    public String CloudImageName ;
    public boolean IsCloud ;
    public String AccountNumber;
    public String AccountName;

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getClassification() {
        return Classification;
    }

    public void setClassification(String classification) {
        Classification = classification;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getCashBillDate() {
        return CashBillDate;
    }

    public void setCashBillDate(String cashBillDate) {
        CashBillDate = cashBillDate;
    }


    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getBillPath() {
        return BillPath;
    }

    public void setBillPath(String billPath) {
        BillPath = billPath;
    }


    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getCloudImageName() {
        return CloudImageName;
    }

    public void setCloudImageName(String cloudImageName) {
        CloudImageName = cloudImageName;
    }

    public boolean isCloud() {
        return IsCloud;
    }

    public void setIsCloud(boolean isCloud) {
        IsCloud = isCloud;
    }


    public double getCredit() {
        return Credit;
    }

    public void setCredit(double credit) {
        Credit = credit;
    }

    public double getDebit() {
        return Debit;
    }

    public void setDebit(double debit) {
        Debit = debit;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public double getBillTax() {
        return BillTax;
    }

    public void setBillTax(double billTax) {
        BillTax = billTax;
    }

    public double getBillTotal() {
        return BillTotal;
    }

    public void setBillTotal(double billTotal) {
        BillTotal = billTotal;
    }
}
