package com.kippinretail.Modal.webclient;

import android.app.Dialog;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.dialogbox.DialogBoxListener;
import com.kippinretail.Modal.webclient.model.ArrayListPost;
import com.kippinretail.Modal.webclient.model.TemplateData;
import com.kippinretail.R;
import com.kippinretail.app.Retail;

//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

//import okhttp3.FormBody;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;


public class WSHandler extends AsyncTask<String, String, String> {


    public WSHandler(WSTemplate wsTemplate) {

    }

    @Override
    protected String doInBackground(String... params) {
        return null;
    }


//
//    private WSTemplate template;
//    Dialog progressDialog;
//    CustomProgressDialog customProgressDialog;
//    MessageDialog dialogBox;
//    public WSHandler(WSTemplate wsTemplate) {
//        this.template = wsTemplate;
//
//        progressDialog = new Dialog(wsTemplate.context, R.style.NewDialog);
//
//    }
//
//
//
//    boolean no_internet_connection;
//
//    @Override
//    protected void onProgressUpdate(String... values) {
//        super.onProgressUpdate(values);
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//        if(no_internet_connection) return null;
//        try {
//            switch (template.methods) {
//                case GET:
//                    return doGetRequest(template.url);
//                case POST:
//                    if(template.body!=null){
//                        return doPostRequest(template.url, template.templatePosts);
//                    }
//                    else if(template.templatePosts==null)
//                        return doPostRequest(template.url);
//                    else
//                        return doPostRequest(template.url, template.templatePosts);
//                case PUT:
//                    if(template.templatePosts==null)
//                        return doPutRequest(template.url);
//                    else {
//                        return doPutRequest(template.url, template.templatePosts);
//                    }
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private OkHttpClient httpClient = new OkHttpClient();
//
//    /**
//     * Method use for Get Request
//     *
//     * @param url
//     * @return
//     * @throws IOException
//     */
//    private String doGetRequest(String url) throws IOException {
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        Response response = httpClient.newCall(request).execute();
//
//
//
//        return response.body().string();
//    }
//
//    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//
//    /**
//     * Method use for Post Request
//     *
//     * @param url
//     * @return
//     * @throws IOException
//     */
//    private String doPostRequest(String url, ArrayListPost templatePosts) throws IOException {
//
//        RequestBody body=null;
//
//        if(template.isFormData){
//
//            FormBody.Builder builder = new FormBody.Builder();
//            for (int i = 0; i < templatePosts.size(); i++) {
//                builder.add(templatePosts.get(i).key, templatePosts.get(i).value);
//            }
//
//            body = builder.build();
//
//        }else{
//
//            body =  RequestBody.create(JSON, templatePosts.getJson());
//
//        }
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//        Response response = httpClient.newCall(request).execute();
//        return response.body().string();
//    }
//
//    /**
//     * Method use for Post Request with out perameter
//     * @param url
//     * @return
//     * @throws IOException
//     */
//    private String doPostRequest(String url) throws IOException {
//
//        FormBody.Builder builder = new FormBody.Builder();
//
//        RequestBody body = builder.build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//        Response response = httpClient.newCall(request).execute();
//        return response.body().string();
//    }
//
//
//    /**
//     * Method use for Put Request
//     * @param url
//     * @param templatePosts
//     * @return
//     * @throws IOException
//     */
//    private String doPutRequest(String url,ArrayListPost templatePosts) throws IOException{
//
//        FormBody.Builder builder = new FormBody.Builder();
//
//        for (int i = 0; i < templatePosts.size(); i++) {
//
//            builder.add(templatePosts.get(i).key, templatePosts.get(i).value);
//        }
//
//        RequestBody body = builder.build();
//        Request request = new Request.Builder()
//                .url(url)
//                .put(body)
//                .build();
//        Response response = httpClient.newCall(request).execute();
//        return response.body().string();
//    }
//
//    /**
//     * Method use for Put Request with out perameter
//     * @param url
//     * @return
//     * @throws IOException
//     */
//    private String doPutRequest(String url) throws IOException {
//
//        FormBody.Builder builder = new FormBody.Builder();
//
//        RequestBody body = builder.build();
//        Request request = new Request.Builder()
//                .url(url)
//                .put(body)
//                .build();
//        Response response = httpClient.newCall(request).execute();
//        return response.body().string();
//    }
//
//
//
//    @Override
//    protected void onPreExecute() {
//        System.out.println("\n");
//        System.out.println("URL:" + template.url);
//        if(template.templatePosts!=null)
//        System.out.println("body:" + template.templatePosts.getJson());
//
//        super.onPreExecute();
//
//
//
//        if(!Utility.checkInternetConnection(template.context)){
//            no_internet_connection = true;
//             MessageDialog.showDialog(template.context, template.context.getResources().getString(R.string.no_internet_connection), (DialogBoxListener) null);
//
//
//        }else{
//            customProgressDialog = new CustomProgressDialog(template.context,progressDialog);
//        }
//
//
//
//    }
//
//
//    @Override
//    protected void onPostExecute(String result) {
//        super.onPostExecute(result);
//        try {
//            progressDialog.dismiss();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        if(no_internet_connection)
//        return;
//
//
//        try{
//
//            System.out.println(result);
//
//            Utility.printLog("data",result);
//            Gson gson = new Gson();
//            TemplateData data = new TemplateData();
//
//
//           /* else if(template.aClass== ModelCloudImages.class){
//                Type listType = new TypeToken<List<ModelCloudImage>>() {
//                }.getType();
//                ModelCloudImages modelProvinceArrayData = new ModelCloudImages();
//                modelProvinceArrayData. images = new Gson().fromJson(result, listType);
//                data = modelProvinceArrayData;
//            }
//            else*/
//            {
//                try {
//                    data = (TemplateData) gson.fromJson(result, template.aClass);
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                    System.err.println(result);
//                }
//
//            }
//
//            data.data = result;
//            template.wsInterface.onResult(template.requestCode, data);
//
//        }catch(Exception e){
//            e.printStackTrace();
//            Utility.dismissProgressDialog();
////            System.err.println(result);
//            MessageDialog.showDialog(template.context, Retail.res.getString(R.string.failure_internet_lost_pls_try_again), (DialogBoxListener) null);
//        }
//
//    }



}
