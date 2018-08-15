package com.kippin.retrofit;


import android.util.Log;

import com.kippin.utils.Url;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Rest client
 */
public class RestClient {

    public static ApiService getApiFinanceServiceForPojo() {

        Log.e("Base URL ", " -= " +  Url.BASE_URL_WITHOUT_MOBILE_API_LIVE);
        //    String k  = URLEncoder.encode(Config.getBaseURL());
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Url.BASE_URL_WITHOUT_MOBILE_API_LIVE)
                .setClient(getOkHttpClient())
                .setLogLevel(RestAdapter.LogLevel.FULL).setRequestInterceptor(new MyRetrofitInterceptor())
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
