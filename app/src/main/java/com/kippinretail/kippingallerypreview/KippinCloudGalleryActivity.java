package com.kippinretail.kippingallerypreview;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.Adapter.AdapterCloudGalleryImages;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.kippinggallery.KippinCloudgallery;
import com.kippinretail.NonKippinGiftCardListActivity;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.UserDashBoardActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by agnihotri on 07/11/17.
 */

public class KippinCloudGalleryActivity extends SuperActivity {
    GridView gridView;
    TextView tvTopbarTitle;
    EditText txtSearch;
    AdapterCloudGalleryImages adapterCloudImages;
    List<KippinCloudgallery> kippinCloudgalleries;
    ArrayList<KippinCloudgallery> cloudgalleries;

    RelativeLayout  lalayout_ivBack, layout_ivHome;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_cloud_images);
        initialiseUI();
        cloudgalleries=new ArrayList<>();
        cloudgalleries.clear();
        NonKippinGiftCardListActivity.kippinGallery=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(NonKippinGiftCardListActivity.kippinGallery){
            finish();
            overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
        }

    }

    public void initialiseUI() {
        gridView = (GridView) findViewById(R.id.gvImages);
        tvTopbarTitle = (TextView) findViewById(R.id.tvTopbarTitle);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        lalayout_ivBack = (RelativeLayout) findViewById(R.id.lalayout_ivBack);
        layout_ivHome = (RelativeLayout) findViewById(R.id.layout_ivHome);

        // Capture Text in EditText
        txtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
        lalayout_ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

            }
        });
        layout_ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.moveToTarget(KippinCloudGalleryActivity.this, UserDashBoardActivity.class);
                //  reSetToNonKippinList();
            }
        });
        fetchListOfKippinGallery();
    }


    private void fetchListOfKippinGallery() {
        LoadingBox.showLoadingDialog(KippinCloudGalleryActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().cloudgalleryImages(new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                LoadingBox.dismissLoadingDialog();
                Log.e("JsonElement:", "" + jsonElement);

                Type type = new TypeToken<List<KippinCloudgallery>>() {
                }.getType();
                kippinCloudgalleries = new Gson().fromJson(jsonElement.toString(), type);
                if (kippinCloudgalleries.size() == 0) {
                    gridView.setVisibility(View.GONE);
                } else {
                    cloudgalleries.addAll(kippinCloudgalleries);
                    adapterCloudImages = new AdapterCloudGalleryImages(KippinCloudGalleryActivity.this, kippinCloudgalleries);
                    gridView.setAdapter(adapterCloudImages);
                    gridView.setVisibility(View.VISIBLE);
                    adapterCloudImages.notifyDataSetChanged();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(error.getUrl(), error.getMessage());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(KippinCloudGalleryActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });

    }

    // Filter Class
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        Log.e("charText:",""+charText);
        kippinCloudgalleries.clear();
        if (charText.length() == 0) {
            kippinCloudgalleries.addAll(cloudgalleries);
        }
        else
        {
            for (KippinCloudgallery wp : cloudgalleries)
            {
                Log.e("WP:::",""+wp.getTemplateName());
                if (wp.getTemplateName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    kippinCloudgalleries.add(wp);
                }
            }
        }
        Log.e("cloudImages:",""+kippinCloudgalleries.size());
        adapterCloudImages = new AdapterCloudGalleryImages(KippinCloudGalleryActivity.this, kippinCloudgalleries);
        gridView.setAdapter(adapterCloudImages);
        gridView.setVisibility(View.VISIBLE);
        adapterCloudImages.notifyDataSetChanged();

    }
}
