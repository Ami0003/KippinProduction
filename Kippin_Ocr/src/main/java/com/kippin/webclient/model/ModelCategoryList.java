package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 2/5/2016.
 */
public class ModelCategoryList extends TemplateData{
    private String CategoryType;

    private String ResponseCode;

    private String Id;

    private String UserId;

    private String ResponseMessage;

    public String getCategoryType ()
    {
        return handleNull(CategoryType);
    }

    public void setCategoryType (String CategoryType)
    {
        this.CategoryType = CategoryType;
    }

    public String getResponseCode ()
    {
        return handleNull(ResponseCode);
    }

    public void setResponseCode (String ResponseCode)
    {
        this.ResponseCode = ResponseCode;
    }

    public String getId ()
    {
        return handleNull(Id);
    }

    public void setId (String Id)
    {
        this.Id = Id;
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

    @Override
    public String toString()
    {
        return "ClassPojo [CategoryType = "+CategoryType+", ResponseCode = "+ResponseCode+", Id = "+Id+", UserId = "+UserId+", ResponseMessage = "+ResponseMessage+"]";
    }
}
