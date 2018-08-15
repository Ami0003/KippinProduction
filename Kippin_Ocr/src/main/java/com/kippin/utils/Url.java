package com.kippin.utils;

import android.util.Log;

import java.io.File;

/**
 * Created by gaganpreet.singh on 1/22/2016.
 */
public class Url {

    /**
     * Primary Base Url
     */
//    public static final String BASE_URL = "http://kippin_api.web1.anzleads.com";
    //  public static final String BASE_URL = "http://kippin_apipreproduction.web1.anzleads.com";
//  public static final String BASE_URL_F = "http://kippin_finance.web1.anzleads.com/MobileApi";
//  public static final String BASE_URL_F = "http://kippin_finance_api.web1.anzleads.com/MobileApi";

    // public static final String BASE_URL_WITHOUT_MOBILE_API = "http://kippin1.web1.anzleads.com/finance";
    public static final String BASE_URL_WITHOUT_MOBILE_API1 = "http://kippinfinance.web1.anzleads.com";
    // public static final String BASE_URL_WITHOUT_MOBILE_API = "http://35.165.88.20/Finance";
    //   public static final String BASE_URL_WITHOUT_MOBILE_API_LIVE = "http://35.165.88.20/Finance";
    //production
    public static final String BASE_URL_WITHOUT_MOBILE_API_LIVE = "https://www1.kippinitsimple.com/Kippin/Finance";
    //production
 //   public static final String BASE_URL_WITHOUT_MOBILE_API_LIVE = "https://www.kippinitsimple.com/Kippin/Finance";
    public static final String BASE_URL_WITH_DOMAIN_NAME = "http://www.Kippinitsimple.com:8080";
    public static final String BASE_URL_F = BASE_URL_WITHOUT_MOBILE_API_LIVE;
    public static final String URL_ACTIVITY_MANUAL_CONNECTION = BASE_URL_WITH_DOMAIN_NAME + "/Account/Web?";
    public static final String URL_ACTIVITY_MANUAL_CONNECTION1 = BASE_URL_WITHOUT_MOBILE_API_LIVE + "/Account/Web?";
    public static final String URL_ACTIVITY_AUTOATIC_CONNECTION = BASE_URL_WITHOUT_MOBILE_API_LIVE + "/AutomaticUploadProcess/AddAccountwithKippin?userId=";
    public static final String URL_CURRENCY_INVOICE = BASE_URL_F + "/InvoiceUserApi/Registration/InvoiceCurrency";
    /**
     * Common Components
     */
    private static final String COMPANY = "/Company";


    //            (10:42) Rini Mittal: http://kippinfinance.web1.anzleads.com/MobileApi
//            (10:43) Rini Mittal: http://kippinfinance.web1.anzleads.com/MobileApi/KippinFinanceApi
    private static final String EXPENSE = "/Expense";
    public static final String URL_GET_EXPENSE = BASE_URL_F + COMPANY + EXPENSE;// /Company/Expense;
    private static final String USER = "/User";
    /**
     * Secondary Base Urls
     */
    public static final String BASE_URL_COMPANY = BASE_URL_F + COMPANY + USER;
    // public static final String BASE_URL_F = BASE_URL_WITHOUT_MOBILE_API+"/MobileApi";
//    public static final String BASE_URL_WITHOUT_MOBILE_API = "http://kippinfinance.web1.anzleads.com";
//  public static final String BASE_URL_F = "http://kippin_finance.web1.anzleads.com/MobileApi";
//  public static final String BASE_URL_F = "http://kippinfinanceapi.web1.anzleads.com/MobileApi";
    public static final String URL_BANK_EXPENSE = BASE_URL_F + EXPENSE + USER + "/BankExpense";
    //    private static final String KIPPIN_FINANCE_API = "/KippinFinance_Api";
    private static final String ACCOUNTS = "/Accounts";
    private static final String ACCOUNT = "/Account";
    private static final String USER_ACC = "/UserAccount";
    private static final String GENERAL_LEDGER = "/GeneralLedger";
    private static final String MOBILE_DROPDOWN_LISTING = "/MobileDropdownListing";
    private static final String CLASSIFICATION = "/Classification";
    public static final String KIPPIN_FINANCE_API = "/KippinFinanceApi";
    //    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/GeneralLedger/UploadBillImage
//    public static final String URL_BANK_STATEMENT_IMAGE = BASE_URL+"/Expense/User/Image";
    public static final String URL_BANK_STATEMENT_IMAGE = BASE_URL_F + KIPPIN_FINANCE_API + GENERAL_LEDGER + "/UploadBillImage";
    //    public static final String URL_UPLOAD = BASE_URL+"/Expense/UploadImage";
//    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/GeneralLedger/UploadImage
    public static final String URL_UPLOAD = BASE_URL_F + KIPPIN_FINANCE_API + GENERAL_LEDGER + "/UploadImage";
    //base+/KippinFinance_Api/GeneralLedger/AddCashEntry
    public static final String URL_ADD_CASH_ENTRY = BASE_URL_F + KIPPIN_FINANCE_API + GENERAL_LEDGER + "/AddCashEntry";
    /**
     * Url for login
     */
//    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/Account/User/Web/{uname}/{password}
//    public static final String URL_ACTIVITY_CONNECTION =   BASE_URL+ACCOUNTS+LOGIN;
    // public static final String URL_ACTIVITY_CONNECTION = BASE_URL_F + KIPPIN_FINANCE_API + ACCOUNT + USER + "/Web";
    public static final String URL_ACTIVITY_CONNECTION = BASE_URL_F + KIPPIN_FINANCE_API + "/Yodlee" + "/CheckUserYodleeAccount";
    //    http://kippin_finance.web1.anzleads.com/MobileApi/KippinFinance_Api/Classification/SaveClassification
    public static final String URL_SAVE_CLASSIFICATION = BASE_URL_F + KIPPIN_FINANCE_API + "/Classification/SaveClassification";
    public static final String URL_DELETE_IMAGE = BASE_URL_WITHOUT_MOBILE_API_LIVE + KIPPIN_FINANCE_API + "/DetachImage/DetachImageFromBill";
    //    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/MobileDropdownListing/GetCategoryList
//    public static final String URL_CATEGORY =  BASE_URL+COMPANY+"/GetCategoryList";
    public static final String URL_CATEGORY = BASE_URL_F + KIPPIN_FINANCE_API + MOBILE_DROPDOWN_LISTING + "/GetCategoryList";
    //    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/MobileDropdownListing/GetCategoryById/{id}
//    public static final String URL_CATEGORY_BY_ID =  BASE_URL+COMPANY+"/GetCategoryById";
    public static final String URL_CATEGORY_BY_ID = BASE_URL_F + KIPPIN_FINANCE_API + MOBILE_DROPDOWN_LISTING + "/GetCategoryById";
    //    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/MobileDropdownListing/ClassificationByUserId/{userId}
//    public static final String URL_CLASSIFICATIONS = BASE_URL+COMPANY+"/Classification";
    public static final String URL_CLASSIFICATIONS = BASE_URL_F + KIPPIN_FINANCE_API + MOBILE_DROPDOWN_LISTING + "/ClassificationByUserId";
    public static final String New_URL_CLASSIFICATIONS = BASE_URL_F + KIPPIN_FINANCE_API + "/MobileDropdownListing/ClassificationListForCashEntryByUserId";
    //  public static final String New_URL_CLASSIFICATIONS = BASE_URL_F + KIPPIN_FINANCE_API + MOBILE_DROPDOWN_LISTING + "/ClassificationListForCashEntryByUserId";
    //    public static final String URL_OCR_EXPENSE = BASE_URL+EXPENSE+USER+"/OcrExpense";
//    public static final String URL_OCR_EXPENSE = BASE_URL+EXPENSE+USER+"/BankExpense";
//    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/GeneralLedger/UserAccount/UpdateBankStatement
    public static final String URL_OCR_EXPENSE = BASE_URL_F + KIPPIN_FINANCE_API + GENERAL_LEDGER + USER_ACC + "/UpdateBankStatement";
    /**
     * UPDATE Account name
     */
//    public static final String URL_ACC_UPDATE = BASE_URL+COMPANY+USER+"/AccountDetails";
//    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/GeneralLedger/UserAccount/AccountDetails
    public static final String URL_ACC_UPDATE = BASE_URL_F + KIPPIN_FINANCE_API + GENERAL_LEDGER + USER_ACC + "/AccountDetails";
    //    public static final String URL_ADD_CARD_DETAILS = BASE_URL_F+KIPPIN_FINANCE_API+ACCOUNT+USER+"/AddCardDetails";]
    public static final String URL_ADD_CARD_DETAILS = BASE_URL_F + KIPPIN_FINANCE_API + ACCOUNT + USER + "/AddCardDetails";
    public static final String URL_CLASSIFICATION_LIST = BASE_URL_F + KIPPIN_FINANCE_API + CLASSIFICATION + "/ClassificationTypeList";
    private static final String BASE_URL_EXPENSE = BASE_URL_F + EXPENSE + USER;
    /**
     * Upload Base 64
     */

    public static final String URL_UPLOAD_IMAGE = BASE_URL_EXPENSE + "/Image";
    private static final String BASE_URL_KIPPIN_FINANCE = BASE_URL_F + KIPPIN_FINANCE_API + ACCOUNT + USER;
    //  "http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/Account/User/UserAsAnAccountantRegistration"
    public static final String URL_USER_AS_AN_ACC_REGISTRATION = BASE_URL_KIPPIN_FINANCE + "/UserAsAnAccountantRegistration";//
    private static final String LOGIN = "/Login";
    /**
     * Url for login
     */
//    public static final String URL_LOGIN =   BASE_URL_COMPANY +LOGIN;
    public static final String URL_LOGIN = BASE_URL_KIPPIN_FINANCE + LOGIN;
    //    private static final String PROVINCE = COMPANY+"/Province";
    private static final String PROVINCE = "/Province";
    /**
     * Province call api
     */
//    public static final String URL_PROVINCE = BASE_URL+PROVINCE;
//    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/MobileDropdownListing/Province
    public static final String URL_PROVINCE = BASE_URL_F + KIPPIN_FINANCE_API + MOBILE_DROPDOWN_LISTING + PROVINCE;
    //    private static final String OWNERSHIP = COMPANY+"/Ownership";
    private static final String OWNERSHIP = "/Ownership";
    //    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/MobileDropdownListing/Ownership
//    public static final String URL_OWNERSHIP = BASE_URL+OWNERSHIP;
    public static final String URL_OWNERSHIP = BASE_URL_F + KIPPIN_FINANCE_API + MOBILE_DROPDOWN_LISTING + OWNERSHIP;
    //    private static final String INDUSTRY = COMPANY+"/Industry";
    private static final String INDUSTRY = "/Industry";
    //    public static final String URL_INDUSTRY = BASE_URL+INDUSTRY;
    public static final String URL_INDUSTRY = BASE_URL_F + KIPPIN_FINANCE_API + MOBILE_DROPDOWN_LISTING + INDUSTRY;
    //    private static final String CURRENCY = COMPANY+"/Currency";
    private static final String CURRENCY = "/Currency";
    public static final String URL_CURRENCY = BASE_URL_F + KIPPIN_FINANCE_API + MOBILE_DROPDOWN_LISTING + CURRENCY;
    public static final String YODLEE = "/Yodlee";
    // public static final String URL_GET_CONNECTED_ACC = BASE_URL_F + KIPPIN_FINANCE_API + YODLEE + "/GetAccountList/";
    public static final String URL_GET_CONNECTED_ACC = BASE_URL_F + KIPPIN_FINANCE_API + YODLEE + "/GetAllAccountTypeList/";
    //Get Classification By OwnershipId		http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/MobileDropdownListing/ClassificationByOwnershipId/{ownershipId}
    private static final String URL_CLASSIFICATION_BY_OWNERSHIP_ID = BASE_URL_F + KIPPIN_FINANCE_API + MOBILE_DROPDOWN_LISTING + "/ClassificationByOwnershipId";
    private static final String URL_GET_MOBILE_DRP_DWN_LISTING = BASE_URL_F + KIPPIN_FINANCE_API + "/MobileDropdownListing/BankList/";
    /**
     * Url for Bank Acc
     */
//    private static final String URL_BANK_ACC = BASE_URL_COMPANY +ACCOUNTS;
//    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/GeneralLedger/UserAccount/{userid}/{bankid}
    private static final String URL_BANK_ACC = BASE_URL_F + KIPPIN_FINANCE_API + GENERAL_LEDGER;
    /**
     * Bank Statement
     */

//    public static final String URL_BANK_STATEMENT = BASE_URL_COMPANY+"/BankStatement";
//    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/GeneralLedger/UserAccount/BankStatementDetails
    private static final String URL_BANK_STATEMENT = BASE_URL_F + KIPPIN_FINANCE_API + GENERAL_LEDGER + USER_ACC;
    private static String cloudImage;
    //    http://kippin_finance.web1.anzleads.com/MobileApi/KippinFinance_Api/GeneralLedger/UserAccount/DeleteStatement/83
    private static String URL_DELETE = BASE_URL_F + KIPPIN_FINANCE_API + GENERAL_LEDGER + USER_ACC + "/DeleteStatement";

//    public static final String URL_CURRENCY = BASE_URL+CURRENCY;

    public static String getUrlGetMobileDrpDwnListing(int type) {
        return URL_GET_MOBILE_DRP_DWN_LISTING + Singleton.getUser().getId() + File.separator + type;
    }

//    3.To update account name:
//    PUT:”User/AccountDetails/{userid}/{bankid}/{AccountNumber}/{AccountName}”

    public static String getClassificationsByOwnershi(String ownershipId) {
        return URL_CLASSIFICATION_BY_OWNERSHIP_ID + File.separator + ownershipId;
    }

    public static String getBankStatementListUrl(String bankId) {
        return URL_BANK_ACC + (!Singleton.isCreditCard ? "/UserAccount" : "/UserCreditCardAccount") + File.separator + Singleton.getUser().getId() + File.separator + bankId;

    }

    public static String getUrlBankStatement(boolean isCreditCard) {
        Log.e("isCreditCard:", "" + isCreditCard);
        Log.e("FinalURL:", "" + URL_BANK_STATEMENT + (!isCreditCard ? "/BankStatementDetails" : "/CreditStatementDetails"));
        return URL_BANK_STATEMENT + (!isCreditCard ? "/BankStatementDetails" : "/CreditStatementDetails");
    }

    public static String getUrlCreditBankStatement(boolean isCreditCard) {
        return URL_BANK_STATEMENT + (!isCreditCard ? "/CreditStatementDetails" : "/CreditStatementDetails");
    }
//    public static String getUpdateAccNameUrl(String userId , String bankId, String accountNumber , String accountName) {
//        return URL_ACC_UPDATE
//                + File.separator+userId
//                +File.separator+bankId
//                +File.separator+accountNumber
//                +File.separator+accountName;
//    }

    public static String getSendAgreementUrl() {
        //        return BASE_URL+"/Company/User/ForgotPassword/"+s ;
        //        http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/Account/User/ForgotPassword/{email}
        return BASE_URL_F + KIPPIN_FINANCE_API + ACCOUNT + "/SendTermsAndConditions";
    }


    public static String getForgotPassword(String email) {
        return BASE_URL_F + KIPPIN_FINANCE_API + ACCOUNT + USER + "/ForgotPassword/" + email;
    }

    public static String getPaymentDataUrl(String mUserId, String tokenId/*, String mLicense*/) {
//        return BASE_URL+"/Company/User/Payment/"+"/"+mUserId+"/"+tokenId+"/"+mLicense;

        return BASE_URL_KIPPIN_FINANCE + "/Payment/" + mUserId + "/" + tokenId/*+"/"+mLicense*/;
    }


    public static String getImages_month() {
//        http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/GeneralLedger/GetUploadedImages/{userId}
        return BASE_URL_F + KIPPIN_FINANCE_API + GENERAL_LEDGER + "/GetUploadedImages/" + Singleton.getUser().getId();
//        return  BASE_URL+EXPENSE+"/GetUploadedImages/"+Singleton.user.getId();
    }

    public static String getImagesByyear(String year) {
//        http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/GeneralLedger/GetUploadedImages/{userId}/{year}
        return getImages_month() + File.separator + year;
    }

    public static String getImagesByyearAndMonth(String year, String month) {
        return getImages_month() + File.separator + month + File.separator + year + File.separator + "1" + File.separator + "10";
    }

    public static String getCloudImage(String statementId) {
//        http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/GeneralLedger/GetBillUploadedImages/{userId}/{statementId}
//        return BASE_URL+EXPENSE+"/GetBillUploadedImages/"+Singleton.user.getId()+File.separator+statementId;
        return BASE_URL_F + KIPPIN_FINANCE_API + GENERAL_LEDGER + "/GetBillUploadedImages/" + Singleton.getUser().getId() + File.separator + statementId;
    }


    public static String getStatusUrl() {
        //    http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/Account/User/CheckUserStatus/{UserId}
        return BASE_URL_F + KIPPIN_FINANCE_API + ACCOUNT + USER + "/CheckUserStatus/" + Singleton.getUser().getId();
    }


    public static String getUrlPrivateKey(String privateKey) {
//        http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/Account/User/RegisterWithAnAccountant/{privateKey}

        return BASE_URL_F + KIPPIN_FINANCE_API + ACCOUNT + USER + "/RegisterWithAnAccountant/" + privateKey;
    }

    public static String getDeleteStatementUrl(String statementId) {
        return URL_DELETE + "/" + statementId;
    }

    public static String getStatementApi() {
        return BASE_URL_F + "/Company/User/BankStatement";
    }

    public static String getSelectPeriodUrl(String startUrl, String endUrl) {
        return BASE_URL_F + KIPPIN_FINANCE_API + GENERAL_LEDGER + "/IncomeReport/" + Singleton.getUser().getId() + "/" + startUrl + "/" + endUrl;
    }

//    public static String getSelectPeriodUrl(String startUrl, String endUrl) {
//        return BASE_URL_F+KIPPIN_FINANCE_API+GENERAL_LEDGER+"/Report/"+Singleton.getUser().getId()+"/"+startUrl+"/"+endUrl;
//    }

    public static String getCheckYodleeAccApi() {
        return BASE_URL_F + KIPPIN_FINANCE_API + YODLEE + "/CheckUserYodleeAccount" + File.separator + Singleton.getUser().getId();
    }

    public static String getYodleeLoginApi(String username, String password) {
//        return BASE_URL_F+KIPPIN_FINANCE_API+YODLEE+File.separator+Singleton.getUser().getId()+File.separator +username+File.separator+password;
//        UserId,UserName,Password
//        Url: BaseUrl + KippinFinanceApi/Yodlee/YodleeLogin
        return BASE_URL_F + KIPPIN_FINANCE_API + YODLEE + "/YodleeLogin";
    }

    public static String getYodleeAddAccApi() {
//        BaseUrl + KippinFinanceApi/Yodlee/AddBankAccount
        return BASE_URL_F + KIPPIN_FINANCE_API + YODLEE + "/AddBankAccount";
    }

    public static String getYodleeRemoveAccountApi(String itemAccountId) {
        return BASE_URL_F + KIPPIN_FINANCE_API + YODLEE + "/RemoveAccount" + File.separator + Singleton.getUser().getId() + File.separator + itemAccountId;
    }

    public static String getYodleeRemoveBankAccountApi(String siteAccountId) {
        return BASE_URL_F + KIPPIN_FINANCE_API + YODLEE + "/RemoveBankAccount" + File.separator + Singleton.getUser().getId() + File.separator + siteAccountId;
    }

    public static String getTermsAndConditions() {
        return BASE_URL_F + KIPPIN_FINANCE_API + ACCOUNT + "/TermsAndConditions";
    }

    public static String getUrlAllBankListing() {
        return BASE_URL_F
                + KIPPIN_FINANCE_API
                + GENERAL_LEDGER
                + (!Singleton.isCreditCard ? "/UserBankAccounts" : "/UserCreditAccount")
                + "/" + Singleton.getUser().getId();
    }

    public String getManualURL() {
        return BASE_URL_WITHOUT_MOBILE_API_LIVE + "/Kippin/Dashboard";
    }
}
