package com.kippinretail.retrofit;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kippinretail.Modal.Industry;
import com.kippinretail.Modal.InvoiceLogin.InvoiceLoginData;
import com.kippinretail.Modal.LoginData.LoginDataClass;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedInput;


/**
 * Define all server calls here
 */
public interface ApiService {
    // <------------------------------------  All POST API ----------------------------------------------->
    // Login
    @FormUrlEncoded
    @POST("/Account/User/Login")
    void LoginWithEmail(@Field("Email") String username, @Field("Password") String password, Callback<JsonElement> callback);

    // MerchantRegistrationActivity
    @FormUrlEncoded
    @POST("/Account/User/Register")
    void
    MerchantRegistration(@Field("ProfileImage") String ProfileImage, @Field("FirstName") String FirstName, @Field("Lastname") String Lastname,
                         @Field("Username") String username, @Field("Email") String Email, @Field("Password") String Password,
                         @Field("BusinessName") String BusinessName, @Field("IndustryId") String Industry,
                         @Field("Businessdescription") String Businessdescription, @Field("City") String City,
                         @Field("Location") String location, @Field("BusinessPhoneNumber") String BusinessPhoneNumber,
                         @Field("BusinessNumber") String BusinessNumber, @Field("Website") String Website,
                         @Field("Country") String Country, @Field("RoleId") String RoleId,
                         Callback<JsonElement> callback);


    @POST("/Account/User/Register")
    void UserRegistration(@Body HashMap<String, String> jsonBody, Callback<JsonElement> callback);


    @POST("/Account/User/ForgotPassword/{Email}")
    void ForgotPassword(@Path("Email") String email, @Body String emptyBody, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST("/Account/User/UserWithMultipleRole")
    void MultipleLoginl(@Field("Email") String email, @Field("Password") String Password, @Field("RoleId") String RoleId, Callback<LoginDataClass> callback);

    @POST("/Voucher/EnableMerchant/{CustomerID}/{MerchantId}")
    public void EnableMerchant(@Path("CustomerID") String CustomerID, @Path("MerchantId") String MerchantId, @Body String emptyString, Callback<JsonElement> response);

    // MAKE PAYMENT
    @POST("/GiftCard/Payment")
    public void Payment(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);
//
//    public void Payment(@Path("userID") String userID,
//                        @Path("barcodeID") String barcodeID,
//                        @Path("amount") String amount,
//                        @Path("merchantId") String merchantId,
//                        @Path("cardNumber") String cardNumber,
//                        @Path("CVV") String CVV,
//                        @Path("expiryMonth") String expiryMonth,
//                        @Path("expiryYear") String expiryYear,
//                        @Body String emptyString, Callback<JsonElement> response);

    // GET CURRENCY
    @GET("/Merchant/GetMerchantCurrencyDetail/{merchantId}")
    public void GetMerchantCurrencyDetail(@Path("merchantId") String merchantId, Callback<JsonElement> callBack);

    // TRANSFER GIFT TO OTHER FRIENDS
    @POST("/GiftCard/TransferGiftCard/{customerId}/{friendId}/{giftCardId}")
    public void TransferGiftCard(@Path("customerId") String customerId,
                                 @Path("friendId") String friendId,
                                 @Path("giftCardId") String giftCardId,
                                 @Body String emptyString, Callback<JsonElement> response);

    //  TRADE POINTS
    @Headers("Content-Type: application/json")
    @POST("/MobilePointAllotmentManagement/TradePoints")
    public void TradePoints(@Body TypedInput body, Callback<JsonElement> response);

    //AUTHENTICATE USER
    @POST("/Account/User/AuthenticateUserForEmployee/{customerId}/{privateKey}")
    public void AuthenticateUserForEmployee(@Path("customerId") String customerId, @Path("privateKey") String privateKey, @Body String emptyString, Callback<JsonElement> response);

    // UPLOAD FRONT AND BACK IMAGE OF CARD
    @FormUrlEncoded
    @POST("/GiftCard/CreatePhysicalLoyalityCard")
    public void CreatePhysicalLoyalityCard(@Field("country") String country, @Field("FrontImage") String FrontImage,
                                           @Field("BackImage") String BackImage, @Field("currentId") String currentId,
                                           @Field("merchantId") String merchantId, @Field("folderName") String folderName);

    @FormUrlEncoded
    @POST("/GiftCard/CreatePurchaseGiftCardBarcode")
    public void CreatePurchaseGiftCardBarcode(@Field("UserId") String UserId, @Field("GiftCardId") String GiftCardId, @Field("giftcardBarcode") String giftcardBarcode, Callback<JsonElement> response);

    @POST("/GiftCard/CreateGiftCard/{MerchantID}")
    void QueryToCreateGiftCardsMerchant(@Path("MerchantID") String merchantId,
                                        @Body TypedInput body, Callback<JsonElement> callback);


    @POST("/PunchCard/CreatePunchCard/{MerchantID}")
    void CreatePunchCard(@Path("MerchantID") String merchantId, @Body TypedInput body, Callback<JsonElement> callback);


    // Send KEY

    @POST("/Account/User/SendPrivateKey/{Email}/{MerchantID}/{employeeId}")
    void SendingKey(@Path("Email") String email, @Path("MerchantID") String MerchantID, @Path("employeeId") String employeeId, @Body String kamal, Callback<JsonElement> callback);

    // CharityRegistrationActivity
    @FormUrlEncoded
    @POST("/Account/User/NotForProfitRegister")
    void CharityRegistration(@Field("ProfileImage") String ProfileImage, @Field("FirstName") String FirstName, @Field("Lastname") String Lastname,
                             @Field("Username") String username, @Field("Email") String Email, @Field("Password") String Password,
                             @Field("BusinessName") String BusinessName, @Field("CharityPurpose") String CharityPurpose,
                             @Field("CharityTypeIdsCommaSeparated") String CharityTypeIdsCommaSeparated, @Field("City") String city,
                             @Field("Location") String Location,
                             /*@Field("BusinessPhoneNumber") String BusinessNumber,*/ @Field("Businessdescription") String Businessdescription,
                             @Field("BusinessNumber") String BusinessNumber
            , @Field("BusinessPhoneNumber") String BusinessPhoneNumber, @Field("Website") String Website,
                             @Field("Country") String Country, @Field("RoleId") String RoleId, Callback<JsonElement> callback);

    // Upload Voucher
//    @Headers("Content-Type: application/json")
    @POST("/Voucher/CreateVoucher/{MerchantID}")
    void QueryToUploadVouchers(@Path("MerchantID") String MerchantID,
                               @Body TypedInput body, Callback<JsonElement> callback);


    @POST("/Account/User/UpdateProfile")
    void UpdateUser(@Body HashMap<String, String> jsonBody, Callback<JsonElement> callback);

    @POST("/Account/User/UpdateReferralId")
    void UpdateReferralId(@Body HashMap<String, String> jsonBody, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST("/Account/User/Changepassword")
    void Changepassword(@Field("ProfileUserId") String userId, @Field("OldPassword") String OldPassword, @Field("NewPassword") String NewPassword,
                        Callback<JsonElement> callback);


    @POST("/Account/User/UpdateProfile")
    void UpdateMerchantProfile(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);

    @POST("/Voucher/EnableMerchant/{currentUserId}/{merchantId}")
    void enableMerchant(@Path("currentUserId") String currentUserId, @Path("merchantId") String merchantId, @Body String emptyString, Callback<JsonElement> response);


    @POST("/Voucher/DisableMerchant/{currentUserId}/{merchantId}")
    void DisableMerchant(@Path("currentUserId") String currentUserId, @Path("merchantId") String merchantId, @Body String emptyString, Callback<JsonElement> response);

    @POST("/Account/UnsubscribeMerchant/{userId}/{merchantId}")
    public void UnsubscribeMerchant(@Path("userId") String userId, @Path("merchantId") String merchantId, @Body String emptyString, Callback<JsonElement> response);

    @POST("/Voucher/RemoveIsRead/{CustomerID}/{MerchantID}/{VoucherID}/")
    public void RemoveIsRead(@Path("CustomerID") String CustomerID, @Path("MerchantID") String MerchantID,
                             @Path("VoucherID") String VoucherID, @Body String emptyString, Callback<JsonElement> response);
    // ------------------------- GET API'S--------------------------------//

    // Get Data for Current Location
    @GET("/geocode/json")
    public void getCurrentLocation(@Query("latlng") String latlng, @Query("sensor") String sensor, Callback<JsonElement> response);

    // Get Data for Current Location
    @GET("/geocode/json")
    public void getCurrentLocation(@Query("latlng") String latlng, @Query("sensor") String sensor, @Query("key") String key, Callback<JsonElement> response);


    // getAll Groups API
    @GET("/Account/Industry")
    public void Industry(Callback<List<Industry>> response);
    // GET ALL VOUCHER LIST

    @GET("/Voucher/GetMerchantsByUser/{CustomerID}")
    public void GetEnabledMerchantList(@Path("CustomerID") String CustomerID, Callback<JsonElement> response);


    // GET ALL MERCHANT LIST // ENABLE MARCHENT SCREEN
    @GET("/Merchant/GetMerchant/{Country}/{customerID}")
    public void GetMerchantList(@Path("Country") String Country, @Path("customerID") String customerID, Callback<JsonElement> response);

    //GET ALL VOUCHER LISTED BY A MERCHANT
    @GET("/Voucher/GetVouchers/{merchantId}/{userId}")
    public void GetVouchers(@Path("merchantId") String merchantId, @Path("userId") String userId, Callback<JsonElement> response);

    // GET COMPARE THE PRICES
    @POST("/Item/ItemList/")
    public void GetAllPrices(@Body TypedInput bodyMerchant, Callback<JsonElement> response);

    // get Merchant list on behalf of country
    @GET("/Merchant/GetMerchantListByCountry/{country}")
    public void GetMerchantListByCountry(@Path("country") String country, Callback<JsonElement> response);

    //Gift Card By merchant Id
    @GET("/GiftCard/GetGiftcardListByMerchantId/{merchantID}/{CustomerID}")
    public void GetGiftcardListByMerchantId(@Path("merchantID") String merchantID, @Path("CustomerID") String CustomerID, Callback<JsonElement> response);


//    //Gift Card By merchant Id
//    @GET("/GiftCard/GetGiftcardListByMerchantId/{merchantID}/{CustomerID}")
//    public void GetGiftcardListByMerchantId(@Path("merchantID") String merchantID, @Path("CustomerID") String CustomerID, Callback<JsonElement> response);

    // get merchant list whose voucher is purchased
    @GET("/GiftCard/MyGiftcardMerchantList/{CustomerID}")
    public void MyGiftcardMerchantList(@Path("CustomerID") String CustomerID, Callback<JsonElement> response);

    //GET PURCHASE NEW GIFT CARD
    @GET("/Account/CountryList")
    public void GetAllCountries(Callback<JsonElement> response);

    // GET FRIEND LIST TO TRADE
    @GET("/Account/User/GetAcceptedFriendListForTradePoint/{customerID}")
    public void GetAcceptedFriendListForTradePoint(@Path("customerID") String customerID, Callback<JsonElement> response);

    // GET FRIEND LIST TO TRANSFER GIFT CARD
    @GET("/Account/User/GetFriendList/{customerID}")
    public void GetFriendList(@Path("customerID") String customerID, Callback<JsonElement> response);

    // GET ALL PURCHASED GIFT CARDS OF A CUSTOMER
    @GET("/GiftCard/GetMyPurchasedGiftcardListByMerchantId/{merchantId}/{customerId}")
    public void GetMyPurchasedGiftcardListByMerchantId(@Path("merchantId") String merchantId, @Path("customerId") String customerId, Callback<JsonElement> response);

    // GET ALL GIFT CARD GIFTED TO CUSTOMER
    @GET("/GiftCard/GetRecievedGiftcardByMerchantId/{merchantId}/{customerId}")
    public void GetRecievedGiftcardByMerchantId(@Path("merchantId") String merchantId, @Path("customerId") String customerId, Callback<JsonElement> response);

    // GET ALL THE MERCHANT YOU WANT TO SUBSCRIBE
    @GET("/Account/GetMerchantListForLoyalityCard/{country}/{customerId}")
    public void GetMerchantListForLoyalityCard(@Path("country") String country, @Path("customerId") String customerId, Callback<JsonElement> response);

    @GET("/Account/GetSubscribedMerchantListForLoyalityCard/{customerID}")
    public void GetSubscribedMerchantListForLoyalityCard(@Path("country") String country, @Path("customerID") String customerID, Callback<JsonElement> response);

    @GET("/Account/GetSubscribedMerchantListForLoyalityCard/{customerID}")
    public void GetSubscribedMerchantListForLoyalityCard(@Path("customerID") String customerID, Callback<JsonElement> response);

    @GET("/GiftCard/MyLoyalityFolderList/{customerId}/{merchantId}")
    public void MyLoyalityFolderList(@Path("customerId") String customerId, @Path("merchantId") String merchantId, Callback<JsonElement> response);

    // GET ALL IMAGS FROM FOLDER UNDEWR MYPOINT SECTION
    @GET("/GiftCard/LoyalityGiftcardImages/{customerId}/{merchantId}/{folderName}")
    public void LoyalityGiftcardImages(@Path("customerId") String customerId, @Path("merchantId") String merchantId, @Path("folderName") String folderName, Callback<JsonElement> response);

    // GET MERCHANT LIST TO TRADE POINTS
    @GET("/Merchant/GetMerchantWithPoints/{country}/{customerID}")
    public void GetMerchantWithPoints(@Path("country") String country, @Path("customerID") String customerID, Callback<JsonElement> response);

    // GET INCOMING LOYALITY_CARD REQUEST
    @GET("/MobilePointAllotmentManagement/GetIncomingPointList/{customerID}")
    public void GetIncomingPointList(@Path("customerID") String customerID, Callback<JsonElement> response);


    // GET INCOMING LOYALITY_CARD REQUEST
    @POST("/MobilePointAllotmentManagement/AcceptIncomingTradePoints/{UserId}/{LoyalityBarCode}/{Points}/{UserLoyalityBarCode}")
    public void AcceptIncomingTradePoints(@Path("UserId") String UserId,
                                          @Path("LoyalityBarCode") String LoyalityBarCode,
                                          @Path("Points") String Points,
                                          @Path("UserLoyalityBarCode") String UserLoyalityBarCode,
                                          @Body String emptyString,
                                          Callback<JsonElement> response);


    // GET OUTGOING LOYALITY_CARD REQUEST
    @GET("/MobilePointAllotmentManagement/GetOutgoingPointList/{customerID}")
    public void GetOutgoingPointList(@Path("customerID") String customerID, Callback<JsonElement> response);

    @GET("/Account/User/GetEmployeeMerchantList/{customerId}/{cardType}")
    public void GetEmployeeMerchantList(@Path("customerId") String customerId, @Path("cardType") String cardType, Callback<JsonElement> response);

    //GET - DONATE TO CHARITY USER LIST
    @GET("/Account/User/GetCharityUserList")
    public void GetDonateToCharityUserList(Callback<JsonElement> response);

    // getLocation and Palace Name
    @GET("/Account/GetUserListForAddingEmployee/{MerchantID}/{Country}")
    public void queryToGetEmployeeList(@Query("MerchantID") String MerchantID, @Query("Country") String Country, Callback<JsonElement> response);

    // get Points
    @GET("/MobilePointAllotmentManagement/GetMerchantPointHistory/{MerchantID}")
    public void QueryToGetPoints(@Query("MerchantID") String MerchantID, Callback<JsonElement> response);

    // get search Points
    @GET("/MobilePointAllotmentManagement/GetMerchantPointHistoryByDate/{MerchantID}/{fromdate}/{todate}")
    public void QueryToGetSearchedPoints(@Path("MerchantID") String MerchantID, @Path("fromdate") String fromdate,
                                         @Path("todate") String todate, Callback<JsonElement> response);

    // get QueryToGiftCardsMerchant
    @GET("/Templates/GetFreeTemplatesList")
    public void QueryToGiftCardsMerchant(Callback<JsonElement> response);

    // get QueryToGiftCardsMerchant
    @GET("/Templates/GetFreePunchCardTemplatesList")
    public void GetFreePunchCardTemplatesList(Callback<JsonElement> response);


    // Get  Voucher
    // GET ALL VOUCHER LIST
    @GET("/Voucher/GetVouchers/{MerchantID}/{MerchantID}")
    public void QueryToVouchers(@Path("MerchantID") String MerchantID, @Path("MerchantID") String MerchantID1, Callback<JsonElement> response);

    //GET - GetCharityMerchantListByUserId
    @GET("/Charity/GetCharityMerchantListByUserId/{customerID}")
    public void GetCharityMerchantListByUserId(@Path("customerID") String CustomerID, Callback<JsonElement> response);


    //GET - GetCharityMerchantListByUserId
    @GET("/Merchant/GetMerchantListForRecievedUser/{customerID}")
    public void GetCharityMerchantListByUserId1(@Path("customerID") String CustomerID, Callback<JsonElement> response);

    @GET("/Account/User/GetCharityPaymentDetail/{customerID}")
    public void GetAllCharityData(@Path("customerID") String customerID, Callback<JsonElement> response);

    //GET - GetCharityMerchantListByUserId
    @GET("/Charity/GetCharityCardByUserId/{userId}/{MerchantId}")
    void GetCharityCardByUserId(@Path("userId") String userId, @Path("MerchantId") String MerchantId, Callback<JsonElement> callback);


    @GET("/Account/User/GetCharityUserStripDetailByUserId/{UserId}")
    public void StripeAvailability(@Path("UserId") String userId, Callback<JsonElement> response);

    // GET LIST OF non kippin LOYALTY CARD
    @GET("/Account/User/GetUserCurrencyByUserId/{CharityUserId}")
    public void GetCurrency(@Path("CharityUserId") String CharityUserId, Callback<JsonElement> response);

    //GET - GetCharityMerchantListByUserId
    @GET("/GiftCard/GetRecievedGiftcardByMerchantId/{userId}/{MerchantId}")
    void GetCharityCardByUserId1(@Path("userId") String userId, @Path("MerchantId") String MerchantId, Callback<JsonElement> callback);


    //  TRADE POINTS
    @POST("/MobilePointAllotmentManagement/TradePoints")
    public void TradePoints1(@Body TypedInput bodyMerchant, Callback<JsonElement> response);

    //  TRADE POINTS
    @POST("/GiftCard/CreatePhysicalLoyalityCard")
    public void CreatePhysicalLoyalityCard(@Body TypedInput bodyMerchant, Callback<JsonElement> response);

    // GIFT CARD
    @POST("/GiftCard/CreatePhysicalGiftCard")
    public void CreatePhysicalGiftCard(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);

    // SHARE LOYALTY CARD
    @POST("/GiftCard/TransferLoyaltyCard/{SenderId}/{ReceiverId}/{LoyaltyCardId}/{FolderName}")
    public void transferLoyaltyCard(@Path("SenderId") String SenderId,
                                    @Path("ReceiverId") String ReceiverId,
                                    @Path("LoyaltyCardId") String LoyaltyCardId,
                                    @Path("FolderName") String FolderName, @Body String emptybody, Callback<JsonElement> response);


    // UNSHARE LOYALTY CARD
    @POST("/GiftCard/UnShareLoyaltyCard/{SenderId}/{ReceiverId}/{LoyaltyCardId}/{FolderName}")
    public void UnShareLoyaltyCard(@Path("SenderId") String SenderId,
                                   @Path("ReceiverId") String ReceiverId,
                                   @Path("LoyaltyCardId") String LoyaltyCardId,
                                   @Path("FolderName") String FolderName, @Body String emptybody, Callback<JsonElement> response);


//    @POST("/GiftCard/TransferLoyaltyCard/{SenderId}/{ReceiverId}/{LoyaltyCardId}/{FolderName}")
//    public void transferLoyaltyCard(@Path("SenderId") String SenderId  ,
//                                    @Path("ReceiverId") String ReceiverId,
//                                    @Path("LoyaltyCardId") String LoyaltyCardId,
//                                    @Path("FolderName") String FolderName,@Body String emptybody, Callback<JsonElement> response);

    @POST("/GiftCard/AcceptLoyaltyCard/{SenderId}/{ReceiverId}/{LoyaltyCardId}")
    public void AcceptLoyaltyCard(@Path("SenderId") String SenderId,
                                  @Path("ReceiverId") String ReceiverId,
                                  @Path("LoyaltyCardId") String LoyaltyCardId,
                                  @Body String emptybody, Callback<JsonElement> response);

    @POST("/GiftCard/RejectLoyaltyCard/{SenderId}/{ReceiverId}/{LoyaltyCardId}/{FolderName}")
    public void RejectLoyaltyCard(@Path("SenderId") String SenderId,
                                  @Path("ReceiverId") String ReceiverId,
                                  @Path("LoyaltyCardId") String LoyaltyCardId,
                                  @Path("FolderName") String FolderName,
                                  @Body String emptybody, Callback<JsonElement> response);


    @POST("/GiftCard/TransferPhysicalGiftCard/{SenderId}/{ReceiverId}/{PhysicalCardId}/{FolderName}")
    public void TransferPhysicalGiftCard(@Path("SenderId") String SenderId,
                                         @Path("ReceiverId") String ReceiverId,
                                         @Path("PhysicalCardId") String PhysicalCardId,
                                         @Path("FolderName") String FolderName, @Body String emptybody, Callback<JsonElement> response);

    @GET("/GiftCard/GetRequestListForTranferLoyaltyCard/{ReceiverId}")
    public void GetRequestListForTranferLoyaltyCard(@Path("ReceiverId") String ReceiverId,
                                                    Callback<JsonElement> response);


    // Loyalty CARD
    @POST("/GiftCard/CreatePhysicalLoyalityCard")
    public void CreatePhysicalLoyalityCard(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);

    @GET("/GiftCard/GetPhysicalCardFolderName/{USER_ID}/{MERCHANT_ID}")
    public void GetPhysicalCardFolderName(@Path("USER_ID") String customerId, @Path("MERCHANT_ID") String merchantId, Callback<JsonElement> response);

    @GET("/GiftCard/GetPhysicalCardUploadedImages/{customerId}/{merchantId}/{folderName}")
    public void GetPhysicalCardUploadedImages(@Path("customerId") String customerId, @Path("merchantId") String merchantId, @Path("folderName") String folderName, Callback<JsonElement> response);

    @GET("/MobilePointAllotmentManagement/GetIncomingPointList/{userId}")
    public void Getincomingpointsincharity(@Path("userId") int userId, Callback<JsonElement> response);

    @GET("/Templates/GetFreeNonKippinLoyalityTemplatesList")
    public void cloudgalleryImages(Callback<JsonElement> response);


//    //SUBSCRIBE MERCHANT
//    @POST("/Account/SubscribeMerchant/{customerId}/{merchantId}")
//    public void SubscribeMerchant(@Path("customerId") String customerId, @Path("merchantId") String merchantId, @Body String emptyString, Callback<JsonElement> response);


    @POST("/Account/SubscribeMerchant/{userId}/{merchantId}")
    public void SubscribeMerchant(@Path("userId") String userId, @Path("merchantId") String merchantId, @Body String emptyString, Callback<JsonElement> response);

//    @POST("/Account/SubscribeMerchant/{userId}/{merchantId}")
//    public void SubscribeMerchant(@Path("userId") String userId, @Path("merchantId") String merchantId, @Body String emptyString, Callback<JsonElement> response);


    //    Type: POST
//    Request Body :
//            [DataMember]
//    public int Itemcost { get; set; }
//    [DataMember]
//    public string InvoiceNumber { get; set; }
//    [DataMember]
//    public string Barcode { get; set; }
//    [DataMember]
//    public int EmployeeId { get; set; }
//    [DataMember]
//    public int ReedemPoint { get; set; }
//    [DataMember]
//    public string LoyalityBarCode { get; set; }
//    [DataMember]
//    public int IsReedemPoint { get; set; }

    @POST("/MobilePointAllotmentManagement/Employee/ManagePoint")
    public void ManagePoint(@Body TypedInput emptyString, Callback<JsonElement> response);


    @GET("/Account/User/GetFriendList/{userId}")
    public void GetFriendList(@Path("userId") int userId, Callback<JsonElement> response);


    @POST("/Account/User/InviteFriendForTradePoints/{InviterId}/{InvitedId}")
    public void InviteFriendForTradePoints(@Path("InviterId") int InviterId, @Path("InvitedId") int InvitedId, @Body String body, Callback<JsonElement> response);


    //GET - GetCharityMerchantListByUserId
    @POST("/GiftCard/CharityGiftCard/{userid}/{merchantId}/{barcode}")
    void CharityGiftCard(@Path("userid") String userid, @Path("merchantId") String merchantId, @Path("barcode") String barcode, Callback<JsonElement> callback);


    //GET - GetCharityMerchantListByUserId
    @POST("/GiftCard/CharityGiftCard/{userid}/{merchantId}/{barcode}")
    void CharityGiftCard_(@Path("userid") String userid, @Path("merchantId") String merchantId, @Path("barcode") String barcode, @Body String s, Callback<JsonElement> callback);

    @POST("/Account/User/UpdateProfile")
    public void UpdateProfileCharity(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);


    @GET("/PunchCard/GetPunchcardListByMerchantId/{merchant_id}/{customer_id}")
    public void GetPunchcardListByMerchantId(@Path("merchant_id") String merchant_id, @Path("customer_id") String customer_id, Callback<JsonElement> response);


    @POST("/PunchCard/IssuePunchCard/{userId}/{punchBarCode}/{merchantId}")
    public void IssuePunchCard(@Path("userId") String userId, @Path("punchBarCode") String punchBarCode,
                               @Path("merchantId") String merchantId, @Body String s, Callback<JsonElement> response);

    @POST("/MobilePointAllotmentManagement/Employee/RedeemPointsByLoyaltyCard")
    public void RedeemPointsByLoyaltyCard(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);

    @POST("/MobilePointAllotmentManagement/Employee/PayByPoints")
    public void PayByPoints(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);

    @GET("/MobilePointAllotmentManagement/GetRedeemPointAvailable/{loyalty_card}/{merchant_id}")
    public void RedeemLoyaltyCard(@Path("loyalty_card") String loyaltyCard,@Path("merchant_id") String merchant,Callback<JsonElement> response);

    @POST("/MobilePointAllotmentManagement/Employee/Retrack")
    public void retractPOint(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);

    //    Merchant/GetMerchantListByCountryForPunchCard + country name
//    Type:GET

    @GET("/Merchant/GetMerchantListByCountryForPunchCard/{country_name}")
    public void GetMerchantListByCountryForPunchCard(@Path("country_name") String country_name, Callback<JsonElement> response);


//        PunchCard/GetMyPurchasedPunchcardListByMerchantId/ + merchant id + customer id
//    Type :GET

    @GET("/PunchCard/GetMyPurchasedPunchcardListByMerchantId/{merchant_id}/{customer_id}")
    public void GetMyPurchasedPunchcardListByMerchantId(@Path("merchant_id") String merchant_id, @Path("customer_id") String customer_id, Callback<JsonElement> response);


    @POST("/PunchCard/CreatePurchasePunchCardBarcode")
    public void CreatePurchasePunchCardBarcode(@Body TypedInput data, Callback<JsonElement> response);


    @GET("/Merchant/GetMerchantListForRecievedUser/{customer_id}")
    public void GetMerchantListForRecievedUser(@Path("customer_id") String data, Callback<JsonElement> response);


    @GET("/PunchCard/MyPunchcardMerchantList/{customer_id}")
    public void MyPunchcardMerchantList(@Path("customer_id") String data, Callback<JsonElement> response);


//    [  "UserId"
//            "PunchCardId"
//            "PunchCardBarCode"]
//            ]
//    Type : POST

//    4.My Punch card screen
//    PunchCard/MyPunchcardMerchantList/ + customer id
//    Type : GET

//    5.My Punch card detail screen
//    PunchCard/GetMyPurchasedPunchcardListByMerchantId/ + merchant id + customer id
//    Type :GET

//    6.Punch card Template list FOR MERCHANT
//    Templates/GetFreePunchCardTemplatesList

//    7.Create punch card  FOR MERCHANT
//    PunchCard/CreatePunchCard/
//            [
//            "TemplateId"
//            "Country"
//            "Punches"
//            "FreePunches"
//            ]


//            Account/User/SendPrivateKey
//        "Email":(self.TextFieldEmail.text)! as String,
//                "merchantId":"\(MerchantID)",
//                "EmployeeUniqueNumber":(self.TextFieldEmployeeID.text)! as String,


    @POST("/Account/User/SendPrivateKey")
    public void SendPrivateKey(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);

    @POST("/Account/User/AuthenticateUserForEmployee/{MerchantID}/{CustomerID}/{PrivateKey}")
    public void AuthenticateUserForEmployee(@Path("MerchantID") String MerchantID, @Path("CustomerID") String CustomerID, @Path("PrivateKey") String PrivateKey, @Body String body, Callback<JsonElement> response);


    @GET("/MobilePointAllotmentManagement/CheckGiftCardBarCodeExist/{customerNumber}")
    public void CheckGiftCardBarCodeExist(@Path("customerNumber") String customerNumber, Callback<JsonElement> response);

    // get all employee under a merchant
    @GET("/Account/EmployeeListToMerchant/{merchantid}")
    public void getEmployeeList(@Path("merchantid") String merchantid, Callback<JsonElement> response);


    @GET("/Analytic/Merchant/PointsIssued/{StartDate}/{EndDate}/{MerchantId}")
    public void getPointAnalysis(@Path("StartDate") String StartDate, @Path("EndDate") String EndDate,
                                 @Path("MerchantId") String merchantid, Callback<JsonElement> response);

    @GET("/Analytic/Merchant/GiftCardHistory/{StartDate}/{EndDate}/{MerchantId}")
    public void getGiftCardAnalysis(@Path("StartDate") String StartDate, @Path("EndDate") String EndDate,
                                    @Path("MerchantId") String merchantid, Callback<JsonElement> response);


    @GET("/Analytic/Merchant/GiftCardUsage/{StartDate}/{EndDate}/{MerchantId}")
    public void getGiftCardUsageAnalysis(@Path("StartDate") String StartDate, @Path("EndDate") String EndDate,
                                         @Path("MerchantId") String merchantid, Callback<JsonElement> response);


    @POST("/Analytic/GiftCardRevenue")
    public void GiftCardRevenue(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);
//    @Path("StartDate")String StartDate,@Path("EndDate")String EndDate,
//    @Path("MerchantId")String merchantid

    @GET("/Analytic/LocationAnalytics/{merchantId}")
    public void getGiftCardLocationAnalysis(@Path("merchantId") String merchantid, Callback<JsonElement> response);

    @GET("/Analytic/WeekDaysGiftcard/{merchantId}")
    public void getGiftCardWeekDaysAnalysis(@Path("merchantId") String merchantId, Callback<JsonElement> response);

    @GET("/Analytic/Merchant/GiftCardSoldComparision/{month1}/{year1}/{month2}/{year2}/{month3}/{year3}/{merchantid}")
    public void getSoldGiftCardAnalysis(@Path("month1") String month1, @Path("year1") String year1,
                                        @Path("month2") String month2, @Path("year2") String year2,
                                        @Path("month3") String month3, @Path("year3") String year3,
                                        @Path("merchantid") String merchantid, Callback<JsonElement> response);

    // GET ALL NOTIFICATION

    @GET("/Analytic/User/Analytics/ {userId}")
    public void getUserAnaytics(@Path("userId") String userId, Callback<JsonElement> response);


    //STAR API
    // NOTIFICATION FOR mERCHANT
    @GET("/Account/GetNewMerchant/{userId}")
    public void getAllNotification(@Path("userId") String userId, Callback<JsonElement> response);

    // NOTIFICATION FOR lOYALTY , PUNCH CARD , VOUCHER

    @GET("/Account/GetNotification/{userId}")
    public void GetNotification(@Path("userId") String userId, Callback<JsonElement> response);


    // GET NOTIFICATION FOR VOUCHER
    @GET("/Voucher/AddedNewByMerchant/{userId}")
    public void AddedNewVoucherByMerchant(@Path("userId") String userId, Callback<JsonElement> response);


    //REMOVE STAR API

    @GET("/Voucher/RemoveIsRead/{CustomerID}/{MerchantID}/voucherId")
    public void RemoveIsRead(@Path("userId") String userId, Callback<JsonElement> response);

    @POST("/MobilePointAllotmentManagement/RemoveIsReadInvite/{friendid}/{userid}")
    public void RemoveIsReadInvite(@Path("friendid") String friendid, @Path("userid") String userid, @Body String body, Callback<JsonElement> response);

    // Point Received
    @POST("/MobilePointAllotmentManagement/RemoveIsReadTradePoints/{userid}/{barcode}")
    public void RemoveIsReadTradePoints(@Path("userid") String userid, @Path("barcode") String barcode, @Body String emptybody, Callback<JsonElement> response);

    // Is Transfer gift card
    // Remove Notification for Gift Card
    @GET("/GiftCard/RemoveIsReadTransfer/{Giftcardid}/{barcode}")
    public void RemoveIsReadTransfer(@Path("Giftcardid") String Giftcardid, @Path("barcode") String barcode, Callback<JsonElement> response);


    @POST("/GiftCard/RemoveIsReadTransfer/{Giftcardid}/{barcode}")
    public void RemoveIsReadTransfer1(@Path("Giftcardid") String Giftcardid,
                                      @Path("barcode") String barcode,
                                      @Body String emptyString,
                                      Callback<JsonElement> response);

    // Remove Notification for NOn Kippin Physical Card
    @POST("/GiftCard/RemoveIsNonPhysical/{SenderId}/{ReceiverId}/{PhysicalCardId}")
    public void RemoveIsNonPhysical(@Path("SenderId") String SenderId,
                                    @Path("ReceiverId") String ReceiverId,
                                    @Path("PhysicalCardId") String PhysicalCardId,
                                    @Body String emptyBody,
                                    Callback<JsonElement> response);


    @GET("/Account/TermsAndConditions")
    public void TermsAndConditions(Callback<JsonElement> response);
    // @GET("User/Analytics/{UserId}")
    // public void getGiftCardAnalysis( @Path("UserId")String merchantid,  Callback<JsonElement> response);


    // TO GET ALL KIPPIN USER
    @GET("/Account/User/GetFriendSuggestionListByUserId/{userId}/{searchText}")
    public void GetFriendSuggestionListByUserId(@Path("userId") String userId, @Path("searchText") String searchText, Callback<JsonElement> response);

    @GET("/Account/User/PendingFriendListForTradePoint/{customerNumber}")
    public void PendingFriendListForTradePoint(@Path("customerNumber") int customerNumber, Callback<JsonElement> response);

    // GET LIST OF non kippin LOYALTY CARD
    @GET("/GiftCard/GetPhysicalLoyalityCardByUserId/{userId}/{merchantId}")
    public void GetPhysicalLoyalityCardByUserId(@Path("userId") String userId, @Path("merchantId") String merchantId, Callback<JsonElement> response);


    // GET LIST OF non kippin share and unshare LOYALTY CARD
    @GET("/GiftCard/GetSharedPhysicalLoyalityCardByUserId/{userId}/{ReceiverId}")
    public void GetSharedPhysicalLoyalityCardByUserId(@Path("userId") String userId, @Path("ReceiverId") String ReceiverId, Callback<JsonElement> response);

    // GET LIST OF non kippin Gift CARD
    @GET("/GiftCard/GetPhysicalCardByUserId/{userId}/{merchantId}")
    public void GetPhysicalCardByUserId(@Path("userId") String userId, @Path("merchantId") String merchantId, Callback<JsonElement> response);

    // Delete employee under a merchant
    @POST("/Account/DeleteEmployee/{merchantid}/{employeedd}")
    public void deleteEmployeeFromList(@Path("merchantid") String merchantid, @Path("employeedd") String employeedd, @Body String empptyBody, Callback<JsonElement> response);

    @POST("/Account/User/AcceptFriendRequestForTradePoints/{FriendId}/{customerId}")
    void AcceptFriendRequestForTradePoints(@Path("FriendId") int id, @Path("customerId") int id1, @Body String s, Callback<JsonElement> callback);


    @POST("/Account/User/RejectFriendRequestForTradePoints/{FriendId}/{customerId}")
    void RejectFriendRequestForTradePoints(@Path("FriendId") int id, @Path("customerId") int id1, @Body String s, Callback<JsonElement> callback);

    @POST("/Account/User/SendPrivateKey")
    void SendPrivateKey(@Body TypedInput typedInput, @Body String s, Callback<JsonElement> callback);

    // DELETE ALL NOTIFICATION
    @POST("/Account/RemoveAllNotifictaion/{userId}")
    public void deleteAllNotification(@Path("userId") String userId, @Body String emptyBody, Callback<JsonElement> response);

    // DELETE single NOTIFICATION
    @POST("/Account/RemoveIsReadMerchant/{userId}/{merchantId}/{toDelete}")
    public void deleteNotification(@Path("userId") String userId,
                                   @Path("merchantId") String merchantId, @Path("toDelete") String toDelete,
                                   @Body String emptyBody, Callback<JsonElement> response);

    @POST("/MobilePointAllotmentManagement/Employee/ManagePointGiftcardBarCode")
    public void chargeGiftCard(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);


    @POST("/GiftCard/DeleteLoyaltyCard")
    public void DeleteLoyaltyCard(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);

    @POST("/GiftCard/UpdateLoyaltyCard")
    public void UpdateLoyaltyCard(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);

    @POST("/GiftCard/UpdatePhysicalGiftCard")
    public void UpdatePhysicalGiftCard(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);

    @POST("/GiftCard/DeletePhysicalGiftCard")
    public void DeletePhysicalGiftCard(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);


    //==========================================PUT =============================================================
    @PUT("/Account/User/UpdateIsFirstLogin/{userId}")
    public void UpdateIsFirstLogint(@Path("userId") String userId, @Body String empty, Callback<JsonElement> response);


    // ================================   KAMAL ============================================================
    @Headers("Content-Type: application/json")
    @POST("/InvoiceUserApi/Registration/UpdateInvoice")
    void UpdateInvoice(@Body TypedInput body, Callback<JsonElement> callback);

    @GET("/InvoiceUserApi/Registration/GetItemsDatabyInvoiceId/{userid}")
    public void getInvoiceItems(@Path("userid") String userid, Callback<JsonElement> response);

    @GET("/InvoiceUserApi/Registration/GetEditSentInvoice/{invoiceId}")
    public void getEditSentInvoice(@Path("invoiceId") String userid, Callback<JsonElement> response);

    @GET("/InvoiceUserApi/Registration/GetEditSentInvoice/{invoiceId}")
    public void getEditInvoiceData(@Path("invoiceId") String userid, Callback<JsonObject> response);

    @GET("/InvoiceUserApi/Registration/GetEditReceivedInvoice/{invoiceId}")
    public void getInvoiceData(@Path("invoiceId") String userid, Callback<JsonObject> response);


    @POST("/InvoiceUserApi/Registration/CreateInvoiceNumber/{UserId}")
    public void getInvoiceNumberr(@Path("UserId") String UserId, @Body String empptyBody, Callback<JsonElement> response);

    @GET("/InvoiceUserApi/Registration/RevenueList/{UserId}/{num}")
    public void getRevenueList(@Path("UserId") String UserId, @Path("num") String num, Callback<JsonElement> response);

    @Headers("Content-Type: application/json")
    @POST("/InvoiceUserApi/Registration/CreateInvoice")
    void createInvoice(@Body TypedInput body, Callback<JsonElement> callback);

    @Headers("Content-Type: application/json")
    @POST("/InvoiceUserApi/Registration/UpdateInvoice")
    void updateInvoice(@Body TypedInput body, Callback<JsonElement> callback);

    @Headers("Content-Type: application/json")
    @POST("/InvoiceUserApi/Registration/EditInvoice")
    void EditInvoice(@Body TypedInput body, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST("/InvoiceUserApi/Registration/User/InvoiceRegistration")
    void InvoiceRegistration1(@Field("Username") String Username, @Field("CompanyLogo") String CompanyLogo, @Field("CompanyName") String CompanyName, @Field("ContactPerson") String ContactPerson,
                              @Field("MobileNumber") String MobileNumber,
                              @Field("CorporateAptNo") String CorporateAptNo, @Field("CorporateHouseNo") String CorporateHouseNo,
                              @Field("CorporateStreet") String CorporateStreet, @Field("CorporateCity") String CorporateCity,
                              @Field("CorporateState") String CorporateState, @Field("CorporatePostalCode") String CorporatePostalCode,
                              @Field("ShippingAptNo") String ShippingAptNo, @Field("ShippingHouseNo") String ShippingHouseNo,
                              @Field("ShippingStreet") String ShippingStreet, @Field("ShippingCity") String ShippingCity,
                              @Field("ShippingState") String ShippingState, @Field("ShippingPostalCode") String ShippingPostalCode,
                              @Field("GoodsType") String GoodsType,

                              @Field("BusinessNumber") String BusinessNumber, @Field("TradingCurrency") String TradingCurrency, @Field("EmailTo") String EmailTo,
                              @Field("EmailCc") String EmailCc, @Field("Website") String Website, @Field("FromInvoiceOrFinance") String FromInvoiceOrFinance,
                              @Field("Password") String Password, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST("/InvoiceUserApi/Registration/User/InvoiceRegistration")
    void InvoiceRegistrationViaFinance(@Field("Username") String Username, @Field("CompanyLogo") String CompanyLogo, @Field("CompanyName") String CompanyName, @Field("ContactPerson") String ContactPerson,
                                       @Field("MobileNumber") String MobileNumber,
                                       @Field("CorporateAptNo") String CorporateAptNo, @Field("CorporateHouseNo") String CorporateHouseNo,
                                       @Field("CorporateStreet") String CorporateStreet, @Field("CorporateCity") String CorporateCity,
                                       @Field("CorporateState") String CorporateState, @Field("CorporatePostalCode") String CorporatePostalCode,
                                       @Field("ShippingAptNo") String ShippingAptNo, @Field("ShippingHouseNo") String ShippingHouseNo,
                                       @Field("ShippingStreet") String ShippingStreet, @Field("ShippingCity") String ShippingCity,
                                       @Field("ShippingState") String ShippingState, @Field("ShippingPostalCode") String ShippingPostalCode,
                                       @Field("GoodsType") String GoodsType,
                                       @Field("BusinessNumber") String BusinessNumber, @Field("TradingCurrency") String TradingCurrency, @Field("EmailTo") String EmailTo,
                                       @Field("EmailCc") String EmailCc, @Field("Website") String Website, @Field("FromInvoiceOrFinance") String FromInvoiceOrFinance,
                                       @Field("Username") String Username1, @Field("Password") String Password,
                                       Callback<JsonElement> callback);


    @FormUrlEncoded
    @POST("/InvoiceUserApi/Registration/RegisterInvoiceCustomerSupplier")
    void InvoiceCustomerSupplierRegistration(@Field("CompanyName") String CompanyName, @Field("ContactPerson") String ContactPerson,
                                             @Field("CorporateAptNo") String CorporateAptNo, @Field("CorporateHouseNo") String CorporateHouseNo,
                                             @Field("CorporateStreet") String CorporateStreet, @Field("CorporateCity") String CorporateCity,
                                             @Field("CorporateState") String CorporateState, @Field("CorporatePostalCode") String CorporatePostalCode,
                                             @Field("ShippingAptNo") String ShippingAptNo, @Field("ShippingHouseNo") String ShippingHouseNo,
                                             @Field("ShippingStreet") String ShippingStreet, @Field("ShippingCity") String ShippingCity,
                                             @Field("ShippingState") String ShippingState, @Field("ShippingPostalCode") String ShippingPostalCode,
                                             @Field("Mobile") String MobileNumber, @Field("Telephone") String Telephone, @Field("EmailTo") String EmailTo,
                                             @Field("EmailCc") String EmailCc, @Field("Website") String Website,
                                             Callback<JsonElement> callback);

    @Headers("Content-Type: application/json")
    @POST("/InvoiceUserApi/Registration/RegisterInvoiceCustomer")
    void customerRegistration(@Body TypedInput body, Callback<JsonElement> callback);

    @Headers("Content-Type: application/json")
    @POST("/InvoiceUserApi/Registration/RegisterInvoiceSupplier")
    void supplierRegistration(@Body TypedInput body, Callback<JsonElement> callback);

    //    @FormUrlEncoded
//    @POST("/InvoiceUserApi/Registration/RegisterInvoiceCustomer")
//    void customerRegistration(@Field("CreatedByUserID") String userId
//            , @Field("CompanyName") String CompanyName,
//                              @Field("ContactPerson") String ContactPerson
//            , @Field("AptNo") String AptNo
//            , @Field("HouseNo") String HouseNo
//            , @Field("Street") String Street
//            , @Field("City") String City
//            , @Field("State") String State
//            , @Field("PostalCode") String PostalCode
//            , @Field("ShippingAptNo") String ShippingAptNo
//            , @Field("ShippingHouseNo") String ShippingHouseNo
//            , @Field("ShippingStreet") String ShippingStreet
//            , @Field("ShippingCity") String ShippingCity
//            , @Field("ShippingState") String ShippingState
//            , @Field("ShippingPostalCode") String ShippingPostalCode
//            , @Field("AreaCode") String AreaCode
//            , @Field("Mobile") String Mobile
//            , @Field("Email") String Email
//            , @Field("Website") String Website
//            , @Field("AdditionalEmail") String AdditionalEmail, Callback<JsonElement> callback);
    @Headers("Content-Type: application/json")
    @POST("/InvoiceUserApi/Registration/UpdateInvoiceCustomer")
    void editCustomerDetail(@Body TypedInput body, Callback<JsonElement> callback);

    @Headers("Content-Type: application/json")
    @POST("/InvoiceUserApi/Registration/UpdateInvoiceSupplier")
    void editSupplierDetail(@Body TypedInput body, Callback<JsonElement> callback);

    @GET("/InvoiceUserApi/Registration/GetCustomerInfo/{Id}")
    void getCustomerDetail(@Path("Id") int id, Callback<JsonElement> callback);


    @PUT("/InvoiceUserApi/Registration/InvoiceUser/ForgotPassword/{email}")
    void ForgotPasswordInvoice(@Path("email") String email, @Body String emptyBody, Callback<JsonElement> callback);


    // MAKE PAYMENT
    @POST("/Account/Payment/CharityUserStripePayment")
    public void DonatePayment(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);


    @FormUrlEncoded
    @POST("/InvoiceUserApi/Registration/User/InvoiceRegistration")
    void CheckInvoiceExist(@Field("Username") String Username, @Field("CompanyLogo") String CompanyLogo, @Field("CompanyName") String CompanyName, @Field("ContactPerson") String ContactPerson,
                           @Field("MobileNumber") String MobileNumber,
                           @Field("CorporateAptNo") String CorporateAptNo, @Field("CorporateHouseNo") String CorporateHouseNo,
                           @Field("CorporateStreet") String CorporateStreet, @Field("CorporateCity") String CorporateCity,
                           @Field("CorporateState") String CorporateState, @Field("CorporatePostalCode") String CorporatePostalCode,
                           @Field("ShippingAptNo") String ShippingAptNo, @Field("ShippingHouseNo") String ShippingHouseNo,
                           @Field("ShippingStreet") String ShippingStreet, @Field("ShippingCity") String ShippingCity,
                           @Field("ShippingState") String ShippingState, @Field("ShippingPostalCode") String ShippingPostalCode,
                           @Field("GoodsType") String GoodsType,
                           @Field("BusinessNumber") String BusinessNumber, @Field("TradingCurrency") String TradingCurrency, @Field("EmailTo") String EmailTo,
                           @Field("EmailCc") String EmailCc, @Field("Website") String Website, @Field("FromInvoiceOrFinance") String FromInvoiceOrFinance,
                           Callback<JsonElement> callback);

    @GET("/InvoiceUserApi/Registration/InvoiceCustomerSupplierList/{UserId}/{RoleId}")
    public void getCustomerList(@Path("UserId") String UserId, @Path("RoleId") String RoleId, Callback<JsonElement> response);


    @GET("/Account/User/CheckUserAddress/{UserId}")
    public void CheckUserAddress(@Path("UserId") String UserId, Callback<JsonElement> response);

    @POST("/Account/User/UpdateAddress")
    void UpdateAddress(@Body HashMap<String, String> jsonBody, Callback<JsonElement> response);
//@POST("/InvoiceUserApi/Registration/CheckInvoiceExistOrNot")
//    void CheckInvoiceExist(@Body TypedInput body, Callback<JsonElement> response);

    @GET("/InvoiceUserApi/Registration/LinkCustomer/{userId}/{searchEmail}")
    void CheckInvoiceExist(@Path("userId") String body, @Path("searchEmail") String searchEmail, Callback<JsonElement> response);

    @GET("/InvoiceUserApi/Registration/LinkSupplier/{userId}/{searchEmail}")
    void CheckInvoiceSupplierExist(@Path("userId") String body, @Path("searchEmail") String searchEmail, Callback<JsonElement> response);


    @POST("/InvoiceUserApi/Registration/InvoiceUser/Login")
        // void InvoiceLogin(@Field("username") String username, @Field("password") String password, Callback<JsonElement> callback);
    void InvoiceLogin(@Body TypedInput body, Callback<InvoiceLoginData> response);

    @GET("/InvoiceUserApi/Registration/ReceivedProformaList/{userid}")
    public void getPerformaList(@Path("userid") String UserId, Callback<JsonElement> response);

    @GET("/InvoiceUserApi/Registration/ReceivedInvoiceList/{userid}")
    public void getReceivedList(@Path("userid") String UserId, Callback<JsonElement> response);

    // Get Report

    @GET("/InvoiceUserApi/Registration/ReportingList/{UserId}/{Type}")
    public void getReportList(@Path("UserId") int UserId, @Path("Type") int Type, Callback<JsonElement> response);

    // Send invoice List
    @GET("/InvoiceUserApi/Registration/SendInvoiceList/{userid}")
    public void getSendList(@Path("userid") String UserId, Callback<JsonElement> response);

    // Send performa List
    @GET("/InvoiceUserApi/Registration/SendProformaList/{userid}")
    public void getSendPerformaList(@Path("userid") String UserId, Callback<JsonElement> response);

    // Get Industry List
    @GET("/KippinFinanceApi/MobileDropdownListing/Industry")
    public void getIndustryList(Callback<JsonElement> response);

//    @GET("/InvoiceUserApi/Registration/generateInvoicepdf/{userid}/{isCustomer}")
//    public void getInvoicePDF(@Path("userid") String UserId, @Path("isCustomer") String isCustomer, Callback<JsonElement> response);

    @GET("/Invoice/ViewPDFAPI/{userid}")
    public void getNewInvoicePDF(@Path("userid") String UserId, Callback<JsonElement> response);

    // Update Password
    @POST("/InvoiceUserApi/Registration/InvoiceUser/Changepassword")
    void InvoiceUpdatePassword(@Body TypedInput body, Callback<JsonElement> response);


    @FormUrlEncoded
    @POST("/InvoiceUserApi/Registration/StripePayment")
    void StripePayment(@Field("CardNumber") String CardNumber, @Field("CvcsEncrypt") String Cvcs,
                       @Field("ExpiryMonth") String ExpiryMonth, @Field("ExpiryYear") String ExpiryYear,
                       @Field("InvoiceID") String InvoiceId, @Field("PaidAmount") String PaidAmount,
                       /*@Field("PaidJVID") String PaidJVID,*/ /*@Field("SupplierID") String SupplierID, */Callback<JsonElement> callback);


    @GET("/InvoiceUserApi/Registration/ExpenseAssetList/{UserId}/{num}")
    public void getExpenseList(@Path("UserId") String UserId, @Path("num") String num, Callback<JsonElement> response);

    // Manual Payment
    @POST("/InvoiceUserApi/Registration/ManualPayment")
    void ManualPayment(@Body TypedInput body, Callback<JsonElement> callback);

    @GET("/InvoiceUserApi/Registration/GetInvoiceDetail/{invoiceId}")
    public void getEditInvoiceData1(@Path("invoiceId") String userid, Callback<JsonObject> response);
}