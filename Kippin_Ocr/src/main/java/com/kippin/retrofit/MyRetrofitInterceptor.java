package com.kippin.retrofit;



import retrofit.RequestInterceptor;

/**
 * Created by Amit Agnihotri.
 */

public class MyRetrofitInterceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade req) {
        req.addHeader("Accept", "application/json");
        req.addHeader("Content-Type", "application/json; charset=utf-8");
        req.addHeader("content-type", "application/json; charset=utf-8");
    }
}
