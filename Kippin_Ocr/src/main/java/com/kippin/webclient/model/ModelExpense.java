package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 1/29/2016.
 */

public class ModelExpense extends TemplateData
{
    private String ExpenseName;

    private String ResponseCode;

    private String Id;

    private String UserId;

    private String ResponseMessage;

    public String getExpenseName ()
    {
        return ExpenseName;
    }

    public void setExpenseName (String ExpenseName)
    {
        this.ExpenseName = ExpenseName;
    }

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
        return "ClassPojo [ExpenseName = "+ExpenseName+", ResponseCode = "+ResponseCode+", Id = "+Id+", UserId = "+UserId+", ResponseMessage = "+ResponseMessage+"]";
    }
}

