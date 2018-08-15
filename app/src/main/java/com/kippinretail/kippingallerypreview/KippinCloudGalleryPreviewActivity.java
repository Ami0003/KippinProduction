package com.kippinretail.kippingallerypreview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kippinretail.ApplicationuUlity.CommonUtility;

import com.kippinretail.NonKippinGiftCardListActivity;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.UserDashBoardActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by agnihotri on 07/11/17.
 */

public class KippinCloudGalleryPreviewActivity extends SuperActivity {
    public static final String IMAGE = "IMAGE_PREVIEW";
    public static final String NAME = "NAME_NAME";
    public static final String PATH = "PATH_IMAGE";
    public static final String ID = "Id";
    RelativeLayout lalayout_ivBack, layout_ivHome;
    String url = null;
    String name = null;
    String path = null;
    String Id = null;
    ImageView ivImage;
    TextView btnUpload;
    TextView btnCancel;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        lalayout_ivBack = (RelativeLayout) findViewById(R.id.lalayout_ivBack);
        layout_ivHome = (RelativeLayout) findViewById(R.id.layout_ivHome);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        btnUpload = (TextView) findViewById(R.id.btnUpload);
        btnCancel = (TextView) findViewById(R.id.btnCancel);
        lalayout_ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NonKippinGiftCardListActivity.kippinGallery=true;
                Log.e("name:",""+name);
                NonKippinGiftCardListActivity.LogoTemplate=path;
                NonKippinGiftCardListActivity.CardName=name;
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
                //Toast.makeText(KippinCloudGalleryPreviewActivity.this,"In-Progress",Toast.LENGTH_LONG).show();

            }
        });
        layout_ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.moveToTarget(KippinCloudGalleryPreviewActivity.this, UserDashBoardActivity.class);
                //  reSetToNonKippinList();
            }
        });
        processNonOperation();
    }

    private void processNonOperation() {
        url = getIntent().getStringExtra(IMAGE);
        name = getIntent().getStringExtra(NAME);
        path = getIntent().getStringExtra(PATH);
        Id = getIntent().getStringExtra(ID);
       // Log.e("ID:", "" + ID);
        Log.e("name:", "" + name);
      //  Log.e("url:", "" + url);
        Log.e("path:", "" + path);
        Picasso.with(this).load(path).into(ivImage);

    }
}
