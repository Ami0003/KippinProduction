package com.kippinretail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.ComparisonGiftAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.ModelToComparePrize.GiftCardDetailWithCountry;
import com.kippinretail.Modal.ModelToComparePrize.ObjFirstMonth;
import com.kippinretail.Modal.ModelToComparePrize.ObjGiftcardDetails;
import com.kippinretail.Modal.ModelToComparePrize.ObjSecondMonth;
import com.kippinretail.Modal.ModelToComparePrize.ObjthirdMonth;
import com.kippinretail.Modal.ModelToComparePrize.ServerResponse;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MerchantActivity_ComparisonGiftCard extends SuperActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    String [] month={"Select Month","Jan","Feb","Mar","Apr","May","Jun","July","Aug","Sep","Oct","Nov","Dec"};
    String [] year = {"Select Year","2016","2017","2018","2019","2020","2021"};
    String selectedData = null;
    ImageView iv_leftArraow , iv_rightArraow;
    Spinner txt_month1, txt_year1,txt_month2, txt_year2,txt_month3, txt_year3;

    Button btn_compare;
    String month1,month2,month3;
    String year1,year2,year3;
    String [] monthAr;
    int index ;
    TextView txt_month;
    ListView list;
    List<GiftCardDetailWithCountry> _month1;
    List<GiftCardDetailWithCountry> _month2;
    List<GiftCardDetailWithCountry> _month3;
    private ComparisonGiftAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_activity__comparison_gift_card);
        initUI();
        updateUI();
        addListener();

    }
    private void initUI(){
        month1 = month2 = month3 = "0";
        year1 = year2 = year3 = "0";
        monthAr = new String[3];
        _month1 = new ArrayList<GiftCardDetailWithCountry>();
        _month2 = new ArrayList<GiftCardDetailWithCountry>();;
        _month3 =  new ArrayList<GiftCardDetailWithCountry>();
        index = 0;
        txt_month1 = (Spinner)findViewById(R.id.txt_month1);
        txt_year1 = (Spinner)findViewById(R.id.txt_year1);
        txt_month2 = (Spinner)findViewById(R.id.txt_month2);
        txt_year2 = (Spinner)findViewById(R.id.txt_year2);
        txt_month3 = (Spinner)findViewById(R.id.txt_month3);
        txt_year3 = (Spinner)findViewById(R.id.txt_year3);

        btn_compare = (Button)findViewById(R.id.btn_compare);
        list = (ListView)findViewById(R.id.list);
        iv_leftArraow = (ImageView)findViewById(R.id.iv_leftArraow);
        iv_rightArraow = (ImageView)findViewById(R.id.iv_rightArraow);
        txt_month = (TextView)findViewById(R.id.txt_month);

    }
    private  void updateUI(){
        generateActionBar(R.string.tile_merchant_ComparisonGiftCard, true, true, false);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,month);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,year);
        txt_month1.setAdapter(monthAdapter);
        txt_month2.setAdapter(monthAdapter);
        txt_month3.setAdapter(monthAdapter);
        txt_year1.setAdapter(yearAdapter);
        txt_year2.setAdapter(yearAdapter);
        txt_year3.setAdapter(yearAdapter);
    }
    private void addListener(){
        txt_month1.setOnItemSelectedListener(this);
        txt_month2.setOnItemSelectedListener(this);
        txt_month3.setOnItemSelectedListener(this);
        txt_year1.setOnItemSelectedListener(this);
        txt_year2.setOnItemSelectedListener(this);
        txt_year3.setOnItemSelectedListener(this);
        btn_compare.setOnClickListener(this);


        iv_leftArraow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (index > 0) {

                    index--;
                    if (index == 2) {
                        adapter = new ComparisonGiftAdapter(_month3, MerchantActivity_ComparisonGiftCard.this);
                        list.setAdapter(adapter);
                        iv_rightArraow.setEnabled(true);
                        iv_rightArraow.setEnabled(true);
                    } else if (index == 1) {
                        adapter = new ComparisonGiftAdapter(_month2, MerchantActivity_ComparisonGiftCard.this);
                        list.setAdapter(adapter);
                        iv_rightArraow.setImageResource(R.drawable.arrow_right);
                        iv_rightArraow.setEnabled(true);
                        iv_leftArraow.setEnabled(true);
                    } else if (index == 0) {
                        adapter = new ComparisonGiftAdapter(_month1, MerchantActivity_ComparisonGiftCard.this);
                        list.setAdapter(adapter);
                        iv_rightArraow.setImageResource(R.drawable.arrow_right);
                        iv_leftArraow.setImageResource(R.drawable.icon_fade_left_arrow);
                        iv_leftArraow.setEnabled(false);
                        iv_rightArraow.setEnabled(true);
                    }
                    if (monthAr[index] != null) {
                        txt_month.setText(monthAr[index]);
                    } else {
                        txt_month.setText("Select Month");
                    }

                }
            }
        });
        iv_rightArraow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (index < monthAr.length - 1) {
                    index++;
                    if (index == 0) {
                        adapter = new ComparisonGiftAdapter(_month1, MerchantActivity_ComparisonGiftCard.this);
                        list.setAdapter(adapter);
                        iv_rightArraow.setEnabled(true);
                        iv_rightArraow.setEnabled(true);
                    } else if (index == 1) {
                        adapter = new ComparisonGiftAdapter(_month2, MerchantActivity_ComparisonGiftCard.this);
                        list.setAdapter(adapter);
                        iv_leftArraow.setImageResource(R.drawable.arrow_left);
                        iv_leftArraow.setEnabled(true);
                        iv_rightArraow.setEnabled(true);
                    } else if (index == 2) {
                        adapter = new ComparisonGiftAdapter(_month3, MerchantActivity_ComparisonGiftCard.this);
                        list.setAdapter(adapter);
                        iv_rightArraow.setImageResource(R.drawable.icon_fade_right_arrow);
                        iv_leftArraow.setEnabled(true);
                        iv_rightArraow.setEnabled(false);
                    }
                    if (monthAr[index] != null) {
                        txt_month.setText(monthAr[index]);
                    } else {
                        txt_month.setText("Select Month");
                    }


                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        makePointAnalytics();
    }

    private void makePointAnalytics() {

    }





    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_compare:
                if((!month1.equals("0") && !year1.equals("0")) ||(!month2.equals("0") && !year2.equals("0")) || (!month3.equals("0") && !year3.equals("0")) ){
                    fetchResult();
                }else{
                    MessageDialog.showDialog(this,"Please select month and year",false);
                }

                break;
        }
    }

    void fetchResult() {
        /*http://kippinretail.web1.anzleads.com.kippinretailApi/Analytic/Merchant/GiftCardSoldComparision/1/2016/1/2016/1/2016/47*/
        index = 0;
        String merchantId =String.valueOf(CommonData.getUserData(this).getId());
        LoadingBox.showLoadingDialog(activity, "Loading ...");
        RestClient.getApiServiceForPojo().getSoldGiftCardAnalysis(month1, year1, month2, year2, month3, year3, merchantId, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("RestClient", jsonElement.toString() + " : " + response.getUrl());

                Type listType = new TypeToken<ServerResponse>(){}.getType();
                ServerResponse response1 = gson.fromJson(jsonElement.toString(), listType);
                if(response!=null && response1.getResponseMessage().equals("Success")){

                    if(response1.getObjFirstMonth()!=null){
                        List<ObjFirstMonth> firstMonth = response1.getObjFirstMonth();
                        for(ObjFirstMonth temp : firstMonth){

                            for(ObjGiftcardDetails giftcardDetails: temp.getObjGiftcardDetails()){
                                GiftCardDetailWithCountry obj = new GiftCardDetailWithCountry();
                                obj.setCountry(temp.getCountry());
                                obj.setGiftcardId(giftcardDetails.getGiftcardId());
                                obj.setGiftcardCount(giftcardDetails.getGiftcardCount());
                                obj.setResponseCode(giftcardDetails.getResponseCode());
                                obj.setResponseMessage(giftcardDetails.getResponseMessage());
                                obj.setTemplateId(giftcardDetails.getTemplateId());
                                obj.setTemplatePath(giftcardDetails.getTemplatePath());
                                obj.setUserId(giftcardDetails.getUserId());
                                _month1.add(obj);
                            }
                        }

                    }


                    if(response1.getObjSecondMonth()!=null) {
                        List<ObjSecondMonth> firstMonth = response1.getObjSecondMonth();
                        for (ObjSecondMonth temp : firstMonth) {

                            for (ObjGiftcardDetails giftcardDetails : temp.getObjGiftcardDetails()) {
                                GiftCardDetailWithCountry obj = new GiftCardDetailWithCountry();
                                obj.setCountry(temp.getCountry());
                                obj.setGiftcardId(giftcardDetails.getGiftcardId());
                                obj.setGiftcardCount(giftcardDetails.getGiftcardCount());
                                obj.setResponseCode(giftcardDetails.getResponseCode());
                                obj.setResponseMessage(giftcardDetails.getResponseMessage());
                                obj.setTemplateId(giftcardDetails.getTemplateId());
                                obj.setTemplatePath(giftcardDetails.getTemplatePath());
                                obj.setUserId(giftcardDetails.getUserId());
                                _month2.add(obj);
                            }
                        }
                    }



                    if(response1.getObjthirdMonth()!=null) {
                        List<ObjthirdMonth> firstMonth = response1.getObjthirdMonth();
                        for (ObjthirdMonth temp : firstMonth) {

                            for (ObjGiftcardDetails giftcardDetails : temp.getObjGiftcardDetails()) {
                                GiftCardDetailWithCountry obj = new GiftCardDetailWithCountry();
                                obj.setCountry(temp.getCountry());
                                obj.setGiftcardId(giftcardDetails.getGiftcardId());
                                obj.setGiftcardCount(giftcardDetails.getGiftcardCount());
                                obj.setResponseCode(giftcardDetails.getResponseCode());
                                obj.setResponseMessage(giftcardDetails.getResponseMessage());
                                obj.setTemplateId(giftcardDetails.getTemplateId());
                                obj.setTemplatePath(giftcardDetails.getTemplatePath());
                                obj.setUserId(giftcardDetails.getUserId());
                                _month3.add(obj);
                            }
                        }
                    }
                    if(monthAr[index]!=null){
                        txt_month.setText(monthAr[index]);
                        adapter = new ComparisonGiftAdapter(_month1,MerchantActivity_ComparisonGiftCard.this);
                        list.setAdapter(adapter);
                    }else{
                        txt_month.setText("Select Month");
                    }


                }
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                Log.e(error.getUrl(), error.getMessage());

                MessageDialog.showFailureDialog(MerchantActivity_ComparisonGiftCard.this);
            }
        });
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedData = parent.getItemAtPosition(position).toString();

        switch(parent.getId()){
            case R.id.txt_month1:
                if(position>0) {
                    month1 = String.valueOf(position + 1);
                    monthAr[0] = txt_month1.getItemAtPosition(position).toString();
                }else{
                    monthAr[0] = "Select Month";
                }
                break;
            case R.id.txt_month2:
                if(position>0) {
                    month2 = String.valueOf(position + 1);
                    monthAr[1] = txt_month2.getItemAtPosition(position).toString();
                }else{
                    monthAr[1] = "Select Month";
                }
                break;
            case R.id.txt_month3:
                if(position>0) {
                    month3 = String.valueOf(position + 1);
                    monthAr[2] = txt_month3.getItemAtPosition(position).toString();
                }else{
                    monthAr[2] = "Select Month";
                }
                break;
            case R.id.txt_year1:
                if(position>0)
                year1 = txt_year1.getItemAtPosition(position).toString();

                break;
            case R.id.txt_year2:
                if(position>0)
                    year2 = txt_year2.getItemAtPosition(position).toString();

                break;
            case R.id.txt_year3:
                if(position>0)
                    year3 = txt_year3.getItemAtPosition(position).toString();

                break;
            case R.id.iv_leftArraow:

                break;
            case R.id.iv_rightArraow:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
