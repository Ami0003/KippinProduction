package com.kippin.webclient.model;

/**
 * Created by gaganpreet.singh on 2/3/2016.
 */
public class ModelImagePost extends TemplateData{
    private String Value;

    private String Description;

    private String Comments;

    private String ClassificationId;

    private String StatementID;

    private String ExpenseId;

    private String IsAccepted;

    private String Tax;

    private String ResponseCode;

    private String FilePath;

    private String UserId;

    private String IsSaved;

    private String ResponseMessage;

    private String Source;

    private String ModifiedDate;

    private String Vendor;

    private String Date;

    private String BankId;

    private String CreatedDate;

    private String Id;

    private String Total;

    public String getValue ()
    {
        return handleNull(Value);
    }

    public void setValue (String Value)
    {
        this.Value = Value;
    }

    public String getDescription ()
    {
        return handleNull(Description);
    }

    public void setDescription (String Description)
    {
        this.Description = Description;
    }

    public String getComments ()
    {
        return handleNull(Comments);
    }

    public void setComments (String Comments)
    {
        this.Comments = Comments;
    }

    public String getClassificationId ()
    {
        return handleNull(ClassificationId);
    }

    public void setClassificationId (String ClassificationId)
    {
        this.ClassificationId = ClassificationId;
    }

    public String getStatementID ()
    {
        return handleNull(StatementID);
    }

    public void setStatementID (String StatementID)
    {
        this.StatementID = StatementID;
    }

    public String getExpenseId ()
    {
        return handleNull(ExpenseId);
    }

    public void setExpenseId (String ExpenseId)
    {
        this.ExpenseId = ExpenseId;
    }

    public String getIsAccepted ()
    {
        return handleNull(IsAccepted);
    }

    public void setIsAccepted (String IsAccepted)
    {
        this.IsAccepted = IsAccepted;
    }

    public String getTax ()
    {
        return handleNull(Tax);
    }

    public void setTax (String Tax)
    {
        this.Tax = Tax;
    }

    public String getResponseCode ()
    {
        return handleNull(ResponseCode);
    }

    public void setResponseCode (String ResponseCode)
    {
        this.ResponseCode = ResponseCode;
    }

    public String getFilePath ()
    {
        return handleNull(FilePath);
    }

    public void setFilePath (String FilePath)
    {
        this.FilePath = FilePath;
    }

    public String getUserId ()
    {
        return handleNull(UserId);
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    public String getIsSaved ()
    {
        return handleNull(IsSaved);
    }

    public void setIsSaved (String IsSaved)
    {
        this.IsSaved = IsSaved;
    }

    public String getResponseMessage ()
    {
        return handleNull(ResponseMessage);
    }

    public void setResponseMessage (String ResponseMessage)
    {
        this.ResponseMessage = ResponseMessage;
    }

    public String getSource ()
    {
        return handleNull(Source);
    }

    public void setSource (String Source)
    {
        this.Source = Source;
    }

    public String getModifiedDate ()
    {
        return handleNull(ModifiedDate);
    }

    public void setModifiedDate (String ModifiedDate)
    {
        this.ModifiedDate = ModifiedDate;
    }

    public String getVendor ()
    {
        return handleNull(Vendor);
    }

    public void setVendor (String Vendor)
    {
        this.Vendor = Vendor;
    }

    public String getDate ()
    {
        return handleNull(Date);
    }

    public void setDate (String Date)
    {
        this.Date = Date;
    }

    public String getBankId ()
    {
        return handleNull(BankId);
    }

    public void setBankId (String BankId)
    {
        this.BankId = BankId;
    }

    public String getCreatedDate ()
    {
        return handleNull(CreatedDate);
    }

    public void setCreatedDate (String CreatedDate)
    {
        this.CreatedDate = CreatedDate;
    }

    public String getId ()
    {
        return handleNull(Id);
    }

    public void setId (String Id)
    {
        this.Id = Id;
    }

    public String getTotal ()
    {
        return handleNull(Total);
    }

    public void setTotal (String Total)
    {
        this.Total = Total;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Value = "+Value+", Description = "+Description+", Comments = "+Comments+", ClassificationId = "+ClassificationId+", StatementID = "+StatementID+", ExpenseId = "+ExpenseId+", IsAccepted = "+IsAccepted+", Tax = "+Tax+", ResponseCode = "+ResponseCode+", FilePath = "+FilePath+", UserId = "+UserId+", IsSaved = "+IsSaved+", ResponseMessage = "+ResponseMessage+", Source = "+Source+", ModifiedDate = "+ModifiedDate+", Vendor = "+Vendor+", CashBillDate = "+Date+", BankId = "+BankId+", CreatedDate = "+CreatedDate+", Id = "+Id+", Total = "+Total+"]";
    }
}
