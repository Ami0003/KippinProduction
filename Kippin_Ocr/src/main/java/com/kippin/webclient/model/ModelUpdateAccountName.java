package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 2/5/2016.
 */
public class ModelUpdateAccountName  extends TemplateData{
    private String AccountNumber;

    private String AccountName;

    private String ResponseCode;

    private String UserId;

    private String ResponseMessage;

    public String getAccountNumber ()
    {
        return AccountNumber;
    }

    public void setAccountNumber (String AccountNumber)
    {
        this.AccountNumber = AccountNumber;
    }

    public String getAccountName ()
    {
        return AccountName;
    }

    public void setAccountName (String AccountName)
    {
        this.AccountName = AccountName;
    }

    public String getResponseCode ()
    {
        return ResponseCode;
    }

    public void setResponseCode (String ResponseCode)
    {
        this.ResponseCode = ResponseCode;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    public String getResponseMessage ()
    {
        return ResponseMessage;
    }

    public void setResponseMessage (String ResponseMessage)
    {
        this.ResponseMessage = ResponseMessage;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [AccountNumber = "+AccountNumber+", AccountName = "+AccountName+", ResponseCode = "+ResponseCode+", UserId = "+UserId+", ResponseMessage = "+ResponseMessage+"]";
    }
}
