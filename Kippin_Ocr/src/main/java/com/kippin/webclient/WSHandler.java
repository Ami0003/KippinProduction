package com.kippin.webclient;

import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kippin.app.Kippin;
import com.kippin.kippin.R;
import com.kippin.selectdate.ModelBankStatement;
import com.kippin.selectdate.ModelBankStatements;
import com.kippin.selectdate.ModelProvinceArrayData;
import com.kippin.selectmonthyear.TemplateMonth;
import com.kippin.selectmonthyear.TemplateMonths;
import com.kippin.utils.CustomProgressDialog;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModalAutomaticStatement;
import com.kippin.webclient.model.ModalAutomaticStatements;
import com.kippin.webclient.model.ModelBankAccount;
import com.kippin.webclient.model.ModelBankAccounts;
import com.kippin.webclient.model.ModelCategoryList;
import com.kippin.webclient.model.ModelCategoryLists;
import com.kippin.webclient.model.ModelClassification;
import com.kippin.webclient.model.ModelClassifications;
import com.kippin.webclient.model.ModelCloudImage;
import com.kippin.webclient.model.ModelCloudImages;
import com.kippin.webclient.model.ModelCurrency;
import com.kippin.webclient.model.ModelCurrencys;
import com.kippin.webclient.model.ModelExpense;
import com.kippin.webclient.model.ModelExpenses;
import com.kippin.webclient.model.ModelIndustry;
import com.kippin.webclient.model.ModelIndustrys;
import com.kippin.webclient.model.ModelOwnership;
import com.kippin.webclient.model.ModelOwnerships;
import com.kippin.webclient.model.ModelProvince;
import com.kippin.webclient.model.TemplateData;
import com.kippin.webclient.model.automatic.ModalAutomaticStatementAccountList;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WSHandler extends AsyncTask<String, String, String> {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Dialog progressDialog;
    CustomProgressDialog customProgressDialog;
    DialogBox dialogBox;
    boolean no_internet_connection;
    private WSTemplate template;
    private OkHttpClient httpClient = new OkHttpClient();

    public WSHandler(WSTemplate wsTemplate) {
        this.template = wsTemplate;

        progressDialog = new Dialog(wsTemplate.context, R.style.NewDialog);

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... params) {

        if (no_internet_connection) return null;
        try {
            switch (template.methods) {
                case GET:
                    return doGetRequest(template.url);
                case POST:
                    if (template.templatePosts == null)
                        return doPostRequest(template.url);
                    else
                        return doPostRequest(template.url, template.templatePosts);
                case PUT:
                    if (template.templatePosts == null)
                        return doPutRequest(template.url);
                    else
                        return doPutRequest(template.url, template.templatePosts);
                case XML_PARSING:
                    return doGetXMLRequest(template.url, template.templatePosts);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method use for Get Request
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String doGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Method use for Get Request (for XML Parsing)
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String doGetXMLRequest(String url, ArrayListPost templatePosts) throws IOException {
        XMLParser parser = new XMLParser();

        return parser.getXmlFromUrl(url);
    }

    /**
     * Method use for Post Request
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String doPostRequest(String url, ArrayListPost templatePosts) throws IOException {

        RequestBody body = null;
        Log.e("Valuerw:", "" + template.isFormData);
        if (template.isFormData) {
            FormBody.Builder builder = new FormBody.Builder();
            for (int i = 0; i < templatePosts.size(); i++) {
                if (templatePosts.get(i).key != null && templatePosts.get(i).value != null)
                    builder.add(templatePosts.get(i).key, templatePosts.get(i).value);
            }
            body = builder.build();
            // body =  RequestBody.create(JSON, builder.build().toString());
        } else {
            body = RequestBody.create(JSON, templatePosts.getJson());
        }

        Log.e("body:", "" + body);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Method use for Post Request with out perameter
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String doPostRequest(String url) throws IOException {

        FormBody.Builder builder = new FormBody.Builder();

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }


    /**
     * Method use for Put Request
     *
     * @param url
     * @param templatePosts
     * @return
     * @throws IOException
     */
    private String doPutRequest(String url, ArrayList<TemplatePost> templatePosts) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();

        for (int i = 0; i < templatePosts.size(); i++) {

            builder.add(templatePosts.get(i).key, templatePosts.get(i).value);
        }

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * Method use for Put Request with out perameter
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String doPutRequest(String url) throws IOException {

        FormBody.Builder builder = new FormBody.Builder();

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }


    @Override
    protected void onPreExecute() {
        System.out.println("\n");
        System.out.println("URL:" + template.url);
        if (template.templatePosts != null)
            System.out.println("body:" + template.templatePosts.getJson());

        super.onPreExecute();


        if (!Utility.checkInternetConnection(template.context)) {
            no_internet_connection = true;
            dialogBox = new DialogBox(template.context, template.context.getResources().getString(R.string.no_internet_connection), new DialogBoxListener() {
                @Override
                public void onDialogOkPressed() {


                }
            });
        } else {
            customProgressDialog = new CustomProgressDialog(template.context, progressDialog);
        }


    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (no_internet_connection)
            return;


        try {

            System.out.println(result);
            Log.e("result","POST-----"+result);
            Utility.printLog("data", result);
            Gson gson = new Gson();
            TemplateData data = new TemplateData();


            if (template.aClass == ModelBankAccounts.class) {
                Type listType = new TypeToken<List<ModelBankAccount>>() {
                }.getType();
                ModelBankAccounts modelBankAccounts = new ModelBankAccounts();
                modelBankAccounts.accounts = new Gson().fromJson(result, listType);
                data = modelBankAccounts;
            } else if (template.aClass == ModelBankStatements.class) {
                Type listType = new TypeToken<List<ModelBankStatement>>() {
                }.getType();
                ModelBankStatements modelBankAccounts = new ModelBankStatements();
                modelBankAccounts.modelBankStatements = new Gson().fromJson(result, listType);
                data = modelBankAccounts;
            } else if (template.aClass == ModelIndustrys.class) {
                Type listType = new TypeToken<List<ModelIndustry>>() {
                }.getType();
                ModelIndustrys modelBankAccounts = new ModelIndustrys();
                modelBankAccounts.modelIndustries = new Gson().fromJson(result, listType);
                data = modelBankAccounts;
            } else if (template.aClass == ModelOwnerships.class) {
                Type listType = new TypeToken<List<ModelOwnership>>() {
                }.getType();
                ModelOwnerships modelBankAccounts = new ModelOwnerships();
                modelBankAccounts.modelOwnerships = new Gson().fromJson(result, listType);
                data = modelBankAccounts;
            } else if (template.aClass == ModelCurrencys.class) {
                Type listType = new TypeToken<List<ModelCurrency>>() {
                }.getType();
                ModelCurrencys modelBankAccounts = new ModelCurrencys();
                modelBankAccounts.modelCurrencies = new Gson().fromJson(result, listType);
                data = modelBankAccounts;
            } else if (template.aClass == ModelExpenses.class) {
                Type listType = new TypeToken<List<ModelExpense>>() {
                }.getType();
                ModelExpenses modelBankAccounts = new ModelExpenses();
                modelBankAccounts.expenses = new Gson().fromJson(result, listType);
                data = modelBankAccounts;
            } else if (template.aClass == ModelCategoryLists.class) {
                Type listType = new TypeToken<List<ModelCategoryList>>() {
                }.getType();
                ModelCategoryLists modelProvinceArrayData = ModelCategoryLists.getInstance();
                modelProvinceArrayData.modelCategoryLists = new Gson().fromJson(result, listType);
                data = modelProvinceArrayData;
            } else if (template.aClass == ModelProvinceArrayData.class) {
                Type listType = new TypeToken<List<ModelProvince>>() {
                }.getType();
                ModelProvinceArrayData modelProvinceArrayData = new ModelProvinceArrayData();
                modelProvinceArrayData.modelProvinces = new Gson().fromJson(result, listType);
                data = modelProvinceArrayData;
            } else if (template.aClass == ModelClassifications.class) {
                Type listType = new TypeToken<List<ModelClassification>>() {
                }.getType();
                ModelClassifications modelProvinceArrayData = new ModelClassifications();
                modelProvinceArrayData.modelClassifications = new Gson().fromJson(result, listType);
                data = modelProvinceArrayData;
            } else if (template.aClass == TemplateMonths.class) {
                Type listType = new TypeToken<List<TemplateMonth>>() {
                }.getType();
                TemplateMonths modelProvinceArrayData = new TemplateMonths();
                modelProvinceArrayData.months = new Gson().fromJson(result, listType);
                data = modelProvinceArrayData;
            } else if (template.aClass == ModelCloudImages.class) {
                Type listType = new TypeToken<List<ModelCloudImage>>() {
                }.getType();
                ModelCloudImages modelProvinceArrayData = new ModelCloudImages();
                modelProvinceArrayData.images = new Gson().fromJson(result, listType);
                data = modelProvinceArrayData;
            } /*else if (template.aClass == ModalAutomaticStatements.class) {
                Type listType = new TypeToken<List<ModalAutomaticStatement>>() {
                }.getType();
                ModalAutomaticStatements modalAutomaticStatements = new ModalAutomaticStatements();
                modalAutomaticStatements.modalAutomaticStatementArrayList = new Gson().fromJson(result, listType);
                data = modalAutomaticStatements;
            }*/
            else if (template.aClass == ModalAutomaticStatements.class) {
                Log.e("POST-----MYYY","POST------MY");
                ModalAutomaticStatements modalAutomaticStatements = new ModalAutomaticStatements();
                Gson gson_ = new Gson();
                  ModalAutomaticStatementAccountList modalAutomaticStatement_AccountList = gson_.fromJson(result, new TypeToken<ModalAutomaticStatementAccountList>() {
                    }.getType());
                modalAutomaticStatements.modalAutomaticStatementAccountList=modalAutomaticStatement_AccountList;

               /* Type listType = new TypeToken<List<ModalAutomaticStatementAccountList>>() {
                }.getType();

                modalAutomaticStatements.modalAutomaticStatementAccountList = new Gson().fromJson(result, listType);*/
                Log.e("POST-----MYYY","POST------MYdata---"+modalAutomaticStatements.modalAutomaticStatementAccountList.getResponseCode());

                template.ws_interface.on_Result(modalAutomaticStatements.modalAutomaticStatementAccountList.getResponseCode(), modalAutomaticStatement_AccountList);
                return;
                //data = modalAutomaticStatements;
            }

            else {
                try {
                    data = (TemplateData) gson.fromJson(result, template.aClass);
                } catch (com.google.gson.JsonSyntaxException e) {
                    try {
                        data = (TemplateData) gson.fromJson(result, template.aClass);
                    } catch (com.google.gson.JsonSyntaxException e2) {
                        e2.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(result);
                }

            }

            data.data = result;
            template.wsInterface.onResult(template.requestCode, data);

        } catch (Exception e) {
            e.printStackTrace();
            Utility.dismissProgressDialog();
//            System.err.println(result);
            new DialogBox(template.context, Kippin.kippin.getString(R.string.failure_internet_lost_pls_try_again), (DialogBoxListener) null);
        }

    }

}
