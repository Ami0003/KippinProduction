package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 7/15/2016.
 */
public class ModalMobileDropdownListing {

    private String ResponseCode;

    private String Id;

    private String UserId;

    private String BankName;

    private String ResponseMessage;

    public String getResponseCode ()
    {
        return ResponseCode;
    }

    public void setResponseCode (String ResponseCode)
    {
        this.ResponseCode = ResponseCode;
    }

    public String getId ()
    {
        return Id;
    }

    public void setId (String Id)
    {
        this.Id = Id;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    public String getBankName ()
    {
        return BankName;
    }

    public void setBankName (String BankName)
    {
        this.BankName = BankName;
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
        return "ClassPojo [ResponseCode = "+ResponseCode+", Id = "+Id+", UserId = "+UserId+", BankName = "+BankName+", ResponseMessage = "+ResponseMessage+"]";
    }

}
