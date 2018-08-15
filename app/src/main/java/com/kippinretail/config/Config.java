package com.kippinretail.config;


/**
 * Created by  kjs on 13/02/15.
 */
public class Config {
    public static String TOKEN_VALUE = "";
    public static String BASE_URL = "https://www1.kippinitsimple.com/Kippin/";
    public static String LIVE_URL = "https://www1.kippinitsimple.com/Kippin/";
    public static String BASE_LOCATION_URL = "";
    static AppMode appMode = AppMode.LIVE;
    static String GCM_PROJECT_NUMBER = "";
    static String api_adder = "/KippinRetailApi";
    public static int SCREEN_OPEN = 0;
    public static int SCREEN_Reg= 0;
    public static String FORMAT="format";
    public static String CONTENT="content";
    public static boolean scanner=false;
    public static boolean screenShow=false;

    //static String FinancebaseURl = "http://kippin1.web1.anzleads.com/finance";
    //  static String FinanceOldUrl = "http://kippinfinance.web1.anzleads.com/MobileAPI";
    //Production
   // static String FinancelivebaseURl = "https://www.kippinitsimple.com/Kippin/Finance";
    //staging
    static String FinancelivebaseURl = "https://www1.kippinitsimple.com/Kippin/Finance";

    public static String getLiveUrl() {
        return LIVE_URL;
    }

    public static void setLiveUrl(String liveUrl) {
        LIVE_URL = liveUrl;
    }

    static public String getFinanceBaseUrl() {
        return FinancelivebaseURl;
    }

    public static String Invoice_PDF_URL = getFinanceBaseUrl() + "/Invoice/ViewPDFAPI/";

    /**
     * Base URL
     *
     * @return
     */

    static public String getBaseURL() {
        init(appMode);
        return BASE_URL;
    }

    static public String getTOkenValue() {
        init(appMode);
        return TOKEN_VALUE;
    }


    /**
     * GCM project number
     *
     * @return
     */
    static public String getGCMProjectNumber() {
        init(appMode);
        return GCM_PROJECT_NUMBER;
    }

    /**
     * Initialize all the variable in this method
     *
     * @param appMode
     */
    public static void init(AppMode appMode) {

        switch (appMode) {

            case LIVE:
//"http://52.39.97.124:8084"+api_adder
                // BASE_URL = "http://kippinretail.web1.anzleads.com"+api_adder;   //development.web1.anzleads.com
                BASE_URL = "https://www1.kippinitsimple.com/Kippin//Wallet" + api_adder;

                //LIVE_URL = "http://kippinretail.web1.anzleads.com"+api_adder;
                LIVE_URL = "https://www1.kippinitsimple.com/Kippin//Wallet" + api_adder;

                BASE_LOCATION_URL = "http://maps.googleapis.com/maps/api/";
                TOKEN_VALUE = "zS-Ryc7CpFY44sw";
                break;
        }


    }

    public enum AppMode {
        LIVE
    }

}