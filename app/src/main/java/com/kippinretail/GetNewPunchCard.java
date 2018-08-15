package com.kippinretail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.AdapterPunchCards;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnSelectionChanged;
import com.kippinretail.Interface.SuperModal;
import com.kippinretail.Modal.ModalResponse;
import com.kippinretail.Modal.punchcard.ModalPunchCard;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.Modal.webclient.model.ArrayListPost;
import com.kippinretail.R;
import com.kippinretail.retrofit.ApiService;
import com.kippinretail.retrofit.RestClientAdavanced;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GetNewPunchCard extends SuperActivity implements View.OnClickListener{

//    i.putExtra("merchantId", modal.getMerchantid());
//    i.putExtra("merchantName", text);

    GridView gvPunchCards;
    Button btnGetCard;

    ArrayList<ModalPunchCard> modalPunchCards;

    int position = -1;

    String merchantId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_new_punch_card);

        generateActionBar(R.id.punch_cards, true, true,false);

        initialiseUI();
        setUpListeners();
        setUpUI();

    }

    @Override
    public void setUpUI() {
        super.setUpUI();

        RestClientAdavanced.getApiServiceForPojo(this).GetPunchcardListByMerchantId(merchantId, CommonData.getUserData(this).getId() + "", getCallback(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.d("GetPunchcardListByMerchantId",jsonElement.toString());
                Type listtype = new TypeToken<List<ModalPunchCard>>() {
                }.getType();
                Gson gson = new Gson();

                modalPunchCards = gson.fromJson(jsonElement,listtype) ;

                if(Utility.isResponseValid(modalPunchCards)){
                    AdapterPunchCards  adapterPunchCards = new AdapterPunchCards(activity, modalPunchCards, AdapterPunchCards.Selection.SINGLESELECTION, new OnSelectionChanged() {
                        @Override
                        public void onSelectionChanged(int position, SuperModal superAdapter, boolean isChecked) {
                            if(isChecked)GetNewPunchCard.this.position = position;
                            else GetNewPunchCard.this.position = -1;
                        }
                    });
                    gvPunchCards.setAdapter(adapterPunchCards) ;
                }else {
                    MessageDialog.showDialog(activity, modalPunchCards.get(0).getResponseMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                MessageDialog.showFailureDialog(GetNewPunchCard.this);
            }
        }));
    }


    @Override
    public void initialiseUI() {

        super.initialiseUI();
        gvPunchCards = (GridView) findViewById(R.id.gvPunchCards);
        btnGetCard  = (Button) findViewById(R.id.btnGetPunchCard);

        merchantId =getIntent().getStringExtra("merchantId") ;
    }

    @Override
    public void setUpListeners() {
        super.setUpListeners();
        btnGetCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnGetPunchCard:

                if(position!=-1){

                   String barcode = CommonData.getUserData(this).getId()+getIntent().getStringExtra("merchantId")+modalPunchCards.get(position)+ Utility.getRandom(6);

                    ArrayListPost templatePosts = new ArrayListPost();
                    templatePosts.add("UserId" , CommonData.getUserData(this).getId()+"") ;
                    templatePosts.add( "PunchCardId" , modalPunchCards.get(position).getId()) ;
                    templatePosts.add( "PunchCardBarCode" , barcode) ;

                    RestClientAdavanced.getApiServiceForPojo(activity).CreatePurchasePunchCardBarcode(getTypedInput(templatePosts.getJson()) ,getCallback(new Callback<JsonElement>() {
                        @Override
                        public void success(JsonElement jsonElement, Response response) {
                            ModalResponse modalResponse = new Gson().fromJson(jsonElement , ModalResponse.class) ;
                            MessageDialog.showDialog(activity ,modalResponse.getResponseMessage() );
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            MessageDialog.showFailureDialog(GetNewPunchCard.this);
                        }
                    }));


                }else {
                    MessageDialog.showDialog(activity ,"Please select atleast one",false);
                }

                break;

        }

    }
}
