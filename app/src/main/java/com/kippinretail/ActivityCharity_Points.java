package com.kippinretail;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.PointListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.GiftCardList.GiftCard;
import com.kippinretail.Modal.PointList.PointDetail;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.callbacks.NotificationREveiver;
import com.kippinretail.fragment.FragmentTopbar;
import com.kippinretail.retrofit.RestClientAdavanced;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import notification.NotificationHandler;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Path;

public class ActivityCharity_Points extends SuperActivity implements Callback<JsonElement>{

    ListView lvPointsList;
    ArrayList<PointDetail> pointDetails ;
    ArrayList<PointDetail> point_Details ;
    RelativeLayout layout_container_search,layout_search;
    EditText txtSearch;
    View terms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_and_outgoing_trade_request);
        point_Details=new ArrayList<>();
        pointDetails=new ArrayList<>();
        pointDetails.clear();
        point_Details.clear();
        generateActionBar(R.string.incoing_request , true,true,false);
        initialiseUI();

        RestClientAdavanced.getApiServiceForPojo(this).Getincomingpointsincharity(CommonData.getUserData(this).getId(),getCallback(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Type listType = new TypeToken<ArrayList<PointDetail>>() {}.getType();
                pointDetails =gson.fromJson(jsonElement.toString(), listType);
                point_Details.addAll(pointDetails);
                setAdapter();
            }

            @Override
            public void failure(RetrofitError error) {
                MessageDialog.showFailureDialog(ActivityCharity_Points.this);

                error.printStackTrace();
            }
        }));

    }


    @Override
    protected void onResume() {
        super.onResume();
        NotificationHandler.getInstance().getNotificationForCards(this, new NotificationREveiver() {
            @Override
            public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

            }
        });
    }

    private void setAdapter() {
        if (!Utility.isResponseValid(pointDetails)) {
            MessageDialog.showDialog(ActivityCharity_Points.this, "No records found.",false);

        } else {

            lvPointsList.setAdapter(new PointListAdapter(ActivityCharity_Points.this,pointDetails,false,terms));
        }
    }
    public void addListener() {
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //if (adapterDonateToCharity != null)
                String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
                //adapterDonateToCharity.getFilter().filter(s.toString().toLowerCase(Locale.getDefault()));
            }
        });

    }
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        android.util.Log.e("charText:",""+charText);
        pointDetails.clear();
        if (charText.length() == 0) {
            android.util.Log.e("pointDetails: ",""+pointDetails.size());
            pointDetails.addAll(point_Details);
        }
        else
        {
            for (PointDetail wp : point_Details)
            {
                android.util.Log.e("getFreindName:::",""+wp.getFreindName());
                if(wp.getFreindName()!=null){
                    if (wp.getFreindName().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        pointDetails.add(wp);
                    }
                }

            }
        }
        android.util.Log.e("pointDetails:",""+pointDetails.size());
        setAdapter();


    }
    @Override
    public void initialiseUI() {
        super.initialiseUI();
        lvPointsList =(ListView)findViewById(R.id.listPoints) ;
        terms = findViewById(R.id.terms);
        txtSearch=(EditText)findViewById(R.id.txtSearch);
        layout_container_search=(RelativeLayout)findViewById(R.id.layout_container_search);
        layout_search=(RelativeLayout)findViewById(R.id.layout_search);
        addListener();
    }


    @Override
    public void success(JsonElement jsonElement, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {
        MessageDialog.showFailureDialog(ActivityCharity_Points.this);
    }
}
