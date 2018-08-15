package com.kippinretail;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.ComparePriceListAdapter;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.ComparePriceList.PriceDetail;
import com.kippinretail.Modal.webclient.model.ArrayListPost;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class ComparePriceActivity extends SuperActivity implements View.OnClickListener , AdapterView.OnItemClickListener
{

    private LinearLayout layout_listHeader;
    private Button btnSearch;
    private EditText txtSearch;
    private ListView priceList;
    List<PriceDetail> priceDetails;
    private ComparePriceListAdapter adapter = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_price);
        initilization();
    }

    private void initilization()
    {
        generateActionBar(R.string.tile_compare_price,true,true,false);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        txtSearch = (EditText)findViewById(R.id.txtSearch);
        priceList = (ListView)findViewById(R.id.priceList);
        layout_listHeader = (LinearLayout)findViewById(R.id.layout_listHeader);
        btnSearch.setOnClickListener(this);
        priceList.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout_listHeader.setVisibility(View.GONE);
        Utils.hideKeyboard(this);
    }
    private  void makeComparePrizeList()
    {
        String searchTxt = txtSearch.getText().toString();
        ArrayListPost templatePosts = new ArrayListPost();

        templatePosts.add("keyword", searchTxt) ;


        TypedInput in = null;
        try {
            in = new TypedByteArray("application/json", templatePosts.getJson().toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(!searchTxt.isEmpty())
        {

            LoadingBox.showLoadingDialog(ComparePriceActivity.this,"Loading...");
            RestClient.getApiServiceForPojo().GetAllPrices(in, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("Output ==>", jsonElement.toString());
                    Type listType = new TypeToken<List<PriceDetail>>() {
                    }.getType();
                    Gson gson = new Gson();
                    priceDetails = (List<PriceDetail>) gson.fromJson(jsonElement.toString(), listType);
                    if(priceDetails!=null){
                        if(priceDetails.size()==1 && !priceDetails.get(0).getResponseMessage().equals("Success")){
                            MessageDialog.showDialog(ComparePriceActivity.this, "No item available.",false);
                        }
                        else{
                            layout_listHeader.setVisibility(View.VISIBLE);
                            adapter = new ComparePriceListAdapter(ComparePriceActivity.this,priceDetails);
                            priceList.setAdapter(adapter);
                        }


                    }
                    LoadingBox.dismissLoadingDialog();
                }

                @Override
                public void failure(RetrofitError error) {
                    System.out.println("Error Comes " + error.getMessage() + " " + error.getUrl());

                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showFailureDialog(ComparePriceActivity.this);

                }
            });
        }
        else {
            MessageDialog.showDialog(ComparePriceActivity.this, "Please enter the item name");
        }



    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSearch :
                makeComparePrizeList();
                if(priceDetails!=null && adapter!=null) {
                    priceDetails.clear();
                    adapter.notifyDataSetChanged();
                    layout_listHeader.setVisibility(View.INVISIBLE);
                    CommonUtility.removeFocus(this);
                }
                else{
                    CommonUtility.removeFocus(this);
                }
                break;

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        layout_listHeader.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    }
}
