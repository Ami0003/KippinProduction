package com.asynctask;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.kippin.kippin.R;
import com.kippin.utils.CustomProgressDialog;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.Utility;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dilip.singh on 1/19/2016.
 */
public class OkHttpHandlerAsyncTask extends AsyncTask<String, String, String> {

    OkHttpClient httpClient = new OkHttpClient();
    String resPonse="";
    Context context;
    Dialog progressDialog;
    CustomProgressDialog customProgressDialog;
    DialogBox dialogBox;
    String username,password,emailaddress;

    /**
     * Cunstructor
     * @param mContext
     */
    public OkHttpHandlerAsyncTask(Context mContext){

        context = mContext;

        progressDialog = new Dialog(context, R.style.NewDialog);


    }
    @Override
    protected void onPreExecute() {

        customProgressDialog = new CustomProgressDialog(context,progressDialog);

    }

    @Override
    protected String doInBackground(String... params) {
        try {


            resPonse=doPostRequest("http://kippin_api.web1.anzleads.com/Company/User/PrivateKey/", username,password,emailaddress);


        }
        catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        progressDialog.dismiss();
        Utility.printLog("Login data",resPonse);
        try{

            JSONObject jsonObject = new JSONObject(resPonse);
            if (jsonObject.getString("ResponseCode").equalsIgnoreCase("1")){

                dialogBox = new DialogBox(context.getResources().getString(R.string.login_successfully),context);
            }
            else {


                    dialogBox = new DialogBox(jsonObject.getString("ResponseMessage"),context);


            }
        }
        catch (Exception e){
            e.printStackTrace();
        }





    }

    /**
     * Method use for Get Request
     * @param url
     * @return
     * @throws IOException
     */
    String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Method use for Post Request
     * @param url
     * @param userName
     * @param passWord
     * @return
     * @throws IOException
     */
    String doPostRequest(String url,String userName, String passWord,String emailAddress) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("Username", userName)
                .add("Password", passWord)
                .add("Email",emailaddress)
                .add("PrivateKey","null")
                .add("SectorId","1")
                .build();
    //Username,Password,Email,PrivateKey,SectorId
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }



}
