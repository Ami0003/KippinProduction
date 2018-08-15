package com.kippinretail.retrofit;



import android.util.Log;

import com.kippinretail.config.Config;
import com.squareup.okhttp.OkHttpClient;
import java.util.concurrent.TimeUnit;
import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Rest client
 */
public class RestClient {


    public static ApiService getApiService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.getLiveUrl())
                .setClient(getOkHttpClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new StringConverter())    //converter for response type
                .build();
        return restAdapter.create(ApiService.class);

    }

    public static ApiService getApiFinanceServiceForPojo() {


        Log.e("Base URL ", " -= " +  Config.getFinanceBaseUrl());
        //    String k  = URLEncoder.encode(Config.getBaseURL());
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.getFinanceBaseUrl())
                .setClient(getOkHttpClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return restAdapter.create(ApiService.class);
    }



    public static ApiService getApiServiceForPojo() {


        Log.e("Base URL ", " -= " + Config.getLiveUrl());
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.getBaseURL())
                .setClient(getOkHttpClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return restAdapter.create(ApiService.class);

    }
    public static ApiService getMyApiService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com/maps/api/")
                .build();
        return restAdapter.create(ApiService.class);

    }

    private static OkClient getOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(0, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(0, TimeUnit.SECONDS);
        return new OkClient(client);
    }

}
