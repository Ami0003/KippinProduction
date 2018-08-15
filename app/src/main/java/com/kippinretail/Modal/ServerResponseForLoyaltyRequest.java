package com.kippinretail.Modal;

/**
 * Created by sandeep.singh on 8/1/2016.
 */
public class ServerResponseForLoyaltyRequest    extends ModalResponse{

    private String TransferId;

    private String BarCode;

    private String FrontImage;

    private String DateCreated;



    private String UserId;

    private boolean IsAccept;

    private String BackImage;

    private String SenderId;

    private String ModifyDate;

    private String LogoImage;

    private String Username;

    private String ReceiverId;

    private String FolderName;

    private String Id;

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getTransferId() {
        return TransferId;
    }

    public void setTransferId(String transferId) {
        TransferId = transferId;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getFrontImage() {
        return FrontImage;
    }

    public void setFrontImage(String frontImage) {
        FrontImage = frontImage;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }


    public boolean isAccept() {
        return IsAccept;
    }

    public void setIsAccept(boolean isAccept) {
        IsAccept = isAccept;
    }

    public String getBackImage() {
        return BackImage;
    }

    public void setBackImage(String backImage) {
        BackImage = backImage;
    }

    public String getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        ModifyDate = modifyDate;
    }

    public String getLogoImage() {
        return LogoImage;
    }

    public void setLogoImage(String logoImage) {
        LogoImage = logoImage;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
