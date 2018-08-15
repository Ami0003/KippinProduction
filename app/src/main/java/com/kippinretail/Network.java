package com.kippinretail;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.Modal.UserAddress.MyGeoCoder;
import com.kippinretail.Modal.UserAddress.Results;
import com.kippinretail.app.Retail;
import com.kippinretail.interfaces.OnLocationGet;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.retrofit.RestClientAdavanced;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kamaljeet.singh on 4/1/2016.
 */
public class Network {
    private Activity activity;
    private String address;
    private boolean networkProvider = false;
    private boolean gpsProvider = false;
    private LocationManager locationmanager = null;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    double lattitude, longitude;
    private String mCity;
    private String mCountry;
    private String mLocation;

    public Network(Activity act) {
        activity = act;
    }

    public static Network With(Activity activity) {
        return new Network(activity);
    }

    private void processLocation(final OnLocationGet onLocationGet){

        LoadingBox.showLoadingDialog(activity, "");
        final String[] ar = new String[1];
        final double[] lat = new double[2];

        MyLocationListener locationCallback = new MyLocationListener(onLocationGet);

        locationmanager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        try {
            networkProvider = locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            gpsProvider = locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_ACCESS_FINE_LOCATION);
            }
            if (networkProvider) {
                Log.e("Network Provider ", "Network Provider ");
                locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationCallback );
            }
            if (gpsProvider) {
                Log.e("GPS Provider ", "GPS Provider ");
                locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,locationCallback);
            }
        } catch (Exception ex) {
            Log.e("Prob Location Param", ex.getMessage() + "");
        }
    }

    // Getting Location
    public void getLocationParam(final OnLocationGet onLocationGet) {

        if(Retail.app.isLocationAvailable()){
            onLocationGet.onLocationGet(Retail.app.getLat() , Retail.app.getLng() ,  Retail.app.getCountry(),Retail.app.getCity());
        }else{
            processLocation(onLocationGet);
        }

    }



    class MyLocationListener implements LocationListener{

        OnLocationGet onLocationGet;

        public MyLocationListener(OnLocationGet onLocationGet){
            this.onLocationGet = onLocationGet;
        }

        @Override
        public void onLocationChanged(Location location) {

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_ACCESS_FINE_LOCATION);
            }
            locationmanager.removeUpdates(this);


            lattitude = location.getLatitude();
            longitude = location.getLongitude();
             getCurrentLocation(new OnLocationGet() {
                @Override
                public void onLocationGet(double lattitude, double longitude, String mCountry,String mcity) {
                    onLocationGet.onLocationGet(lattitude ,longitude , mCountry,mCity);
                }
            });

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };


    public void getCurrentLocation(final OnLocationGet onLocationGet) {
        String latlng;
        latlng = lattitude + "," + longitude;
        String sensor = "true";
        final String[] ar = new String[1];
        RestClientAdavanced.getMyApiService().getCurrentLocation(latlng, sensor, CommonUtility.getAndroidKey(),RestClientAdavanced.getCallback( new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                String jsonoutput = jsonElement.toString();
                Type listtype = new TypeToken<MyGeoCoder>() {
                }.getType();
                Gson gson = new Gson();
                MyGeoCoder geoCoder = gson.fromJson(jsonoutput, listtype);
                List<Results> results = geoCoder.getResults();
                Log.e("Total Result :: ", results.size() + "");
                if (results.size() > 0) {
                    Results result = results.get(0);

                    for (int x = 0; x < result.getAddress_components().size(); x++) {

                        if (result.getAddress_components().get(x).getTypes().get(0).equals("locality")) {

                            mCity = result.getAddress_components().get(x).getLong_name();
                            mLocation = mCity;

                        }
                        if (result.getAddress_components().get(x).getTypes().get(0).equals("country")) {
                         Log.e("country", "===" + result.getAddress_components().get(x).getLong_name());
                            mCountry = result.getAddress_components().get(x).getLong_name();
                            mLocation = mCity + "," + mCountry;

                        }
                    }

                }


                onLocationGet.onLocationGet(lattitude ,longitude  , mCountry,mCity);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Google Api Error", error.getMessage());
            }
        }));
    }

}
