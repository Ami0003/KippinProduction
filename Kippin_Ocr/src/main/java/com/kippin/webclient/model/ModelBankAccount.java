package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 1/22/2016.
 */
public class ModelBankAccount extends TemplateData {

    private String AccountNumber;

    private String AccountName;

    private String ResponseCode;

    private String UserId;

    private String ResponseMessage;

//    private transient int bankId;

    private int BankId ;
    Object BankLogo;
    public Object getBankLogo ()
    {
        return  BankLogo;
    }

    public void setBankLogo (Object BankLogo)
    {
        this.BankLogo = BankLogo;
    }
//    [{"AccountName":"Cash","BankId":6,"ResponseCode":1,"ResponseMessage":"Success","UserId":0},{"AccountName":"Main Business Bank Account","BankId":1,"ResponseCode":1,"ResponseMessage":"Success","UserId":0}]

    public String getAccountNumber ()
    {
         return  AccountNumber;
    }

    public void setAccountNumber (String AccountNumber)
    {
        this.AccountNumber = AccountNumber;
    }

    public String getResponseCode ()
    {
         return handleNull(ResponseCode);
    }

    public void setResponseCode (String ResponseCode)
    {
        this.ResponseCode = ResponseCode;
    }

    public String getUserId ()
    {
         return handleNull(UserId);
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    public String getResponseMessage ()
    {
         return handleNull(ResponseMessage);
    }

    public void setResponseMessage (String ResponseMessage)
    {
        this.ResponseMessage = ResponseMessage;
    }
    public String getAccountName ()
    {
         return AccountName;
    }

    public void setAccountName (String AccountName)
    {
        this.AccountName=AccountName;
    }

    @Override
    public String toString()
    {
         return "ClassPojo [AccountNumber = "+AccountNumber+", ResponseCode = "+ResponseCode+", UserId = "+UserId+", ResponseMessage = "+ResponseMessage+"]";
    }

    public int getBankId() { return BankId ;
    }

    public void setBankId(int bankId) {
        this.BankId = bankId;
    }
}
