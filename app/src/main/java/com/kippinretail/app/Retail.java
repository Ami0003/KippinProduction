package com.kippinretail.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import com.kippin.app.Kippin;
import com.kippin.dashboard.ActivityDashboard;
import com.kippinretail.KippinInvoice.InvoiceDashBoard;
import com.kippinretail.KippinInvoice.InvoiceViaFinanceRegistration;
import com.kippinretail.RegistrationActivity;

/**
 * Created by gaganpreet.singh on 4/8/2016.
 */
public class Retail extends Application {

    public static Resources res;

    public static Retail app;
    private double lat=0;
    private double lng=0;
    private static String country;
    private static String city;

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        Retail.city = city;
    }

    public static void setCountry(String country) {
        Retail.country = country;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        res = getResources();

        Kippin.onCreate(this, RegistrationActivity.class, InvoiceDashBoard.class, InvoiceViaFinanceRegistration.class, RegistrationActivity.class, ActivityDashboard.class);

      MultiDex.install(this);
    }


   
    public void setLocationParams(double lat, double lng, String country,String city) {
        this.lat = lat;
        this.lng = lng;
        this.country = country;
        this.city = city;
    }

    public double getLat() {
        return lat;
    }


    public double getLng() {
        return lng;
    }

    public static  String getCountry() {
        return country;
    }

    public boolean isLocationAvailable(){
        return lat>0 && lng>0 && country!=null;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
     //   MultiDex.install(this);
    }
}

