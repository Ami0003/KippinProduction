package com.kippinretail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.squareup.picasso.Picasso;

public class ActivityMyPunchCards extends SuperActivity {
    LinearLayout layout_punches, layout_heading, layout_text;
    ImageView ivPunchCard, ivBarCode;
    ImageView ivPunch1, ivPunch2, ivPunch3, ivPunch4, ivPunch5,
            ivPunch6, ivPunch7, ivPunch8, ivPunch9, ivPunch10;
    TextView tvBarcode, tvDescription, txtMerchantName, txtName, tvpoint;
    BarcodeUtil barcodeUtil;
    ImageView ivPunches[] = null;
    private Merchant cardDetail;
    View underLine;
    int hei, wei;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subitem_mypunch_card);
        initialiseUI();
        updateToolBar();
        updateUI();
        ;
        addListener();
    }


    @Override
    public void initialiseUI() {
        super.initialiseUI();
        underLine = findViewById(R.id.underLine);
        ivPunches = new ImageView[10];
        barcodeUtil = new BarcodeUtil(this);
        ivPunchCard = (ImageView) findViewById(R.id.ivPunchCard);
        ivBarCode = (ImageView) findViewById(R.id.ivBarCode);
        tvBarcode = (TextView) findViewById(R.id.tvBarcode);
        tvpoint = (TextView) findViewById(R.id.tvpoint);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        txtMerchantName = (TextView) findViewById(R.id.txtMerchantName);
        txtName = (TextView) findViewById(R.id.txtName);
        layout_punches = (LinearLayout) findViewById(R.id.layout_punches);
        layout_text = (LinearLayout) findViewById(R.id.layout_text);
        layout_heading = (LinearLayout) findViewById(R.id.layout_heading);
        ivPunches[0] = ivPunch1 = (ImageView) findViewById(R.id.ivPunch1);
        ivPunches[1] = ivPunch2 = (ImageView) findViewById(R.id.ivPunch2);
        ivPunches[2] = ivPunch3 = (ImageView) findViewById(R.id.ivPunch3);
        ivPunches[3] = ivPunch4 = (ImageView) findViewById(R.id.ivPunch4);
        ivPunches[4] = ivPunch5 = (ImageView) findViewById(R.id.ivPunch5);
        ivPunches[5] = ivPunch6 = (ImageView) findViewById(R.id.ivPunch6);
        ivPunches[6] = ivPunch7 = (ImageView) findViewById(R.id.ivPunch7);
        ivPunches[7] = ivPunch8 = (ImageView) findViewById(R.id.ivPunch8);
        ivPunches[8] = ivPunch9 = (ImageView) findViewById(R.id.ivPunch9);
        ivPunches[9] = ivPunch10 = (ImageView) findViewById(R.id.ivPunch10);
        hei = CommonUtility.dpToPx(ActivityMyPunchCards.this, 60);
        wei = CommonUtility.dpToPx(ActivityMyPunchCards.this, 60);
    }

    public void updateToolBar() {

    }


    public void updateUI() {
        layout_heading.setVisibility(View.VISIBLE);
        Intent i = getIntent();
        // cardDetail =  (Merchant)i.getSerializableExtra("cardDetail");

        if (CommonUtility.DataDetail != null && !CommonUtility.DataDetail.getIsPunchCard()) {
            generateActionBar(R.string.title_myloyalty_card, true, true, false);
            makeLoayaltyCard();
        } else {
            generateActionBar(R.string.title_mypunch_card, true, true, false);
            underLine.setVisibility(View.GONE);
            makePunchCard();
        }

    }

    private void makePunchCard() {
        try {
            layout_punches.setVisibility(View.VISIBLE);
            layout_text.setVisibility(View.GONE);

            arrangePunchCard(Integer.parseInt(CommonUtility.DataDetail.getObjPunchCard().getTotalPunches()), Integer.parseInt(CommonUtility.DataDetail.getObjPunchCard().getPunches()));
            txtName.setVisibility(View.VISIBLE);
            txtMerchantName.setText(CommonUtility.DataDetail.getBusinessName());
            Log.e("HERERERE:", "" + CommonUtility.DataDetail.getObjPunchCard().getPunchcardImage());
            if (CommonUtility.DataDetail.getObjPunchCard().getPunchcardImage() != null && !CommonUtility.DataDetail.getObjPunchCard().getPunchcardImage().equals("")) {

                if (CommonUtility.DataDetail.getObjPunchCard().getPunchcardImage().startsWith("http://")) {
                    Picasso.with(this).load(CommonUtility.DataDetail.getObjPunchCard().getPunchcardImage())
                            .resize(CommonUtility.getWidthOfScreen(this), CommonUtility.dpToPx(this, 250))
                            .into(ivPunchCard);
                } else {
                  /*  MyDecoder d =     new MyDecoder(ivPunchCard,wei,hei);
                    d.execute(CommonUtility.DataDetail.getObjPunchCard().getPunchcardImage());*/

                    byte[] decodedFirstString = Base64.decode(CommonUtility.DataDetail.getObjPunchCard().getPunchcardImage(), Base64.DEFAULT);
                    Bitmap decodedByteForFront = BitmapFactory.decodeByteArray(decodedFirstString, 0, decodedFirstString.length);
                    String pathForFront = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, decodedByteForFront));
                    ivPunchCard.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(pathForFront, CommonUtility.dpToPx(this, CommonUtility.getWidthOfScreen(this) - CommonUtility.dpToPx(this, 100)),
                            CommonUtility.dpToPx(this, 130)));

                }
            }

            ivBarCode.setImageDrawable(barcodeUtil.generateBarcode(CommonUtility.DataDetail.getObjPunchCard().getPunchBarcode()));
            tvBarcode.setText("BARCODE: " + CommonUtility.DataDetail.getObjPunchCard().getPunchBarcode());
            tvDescription.setText(CommonUtility.DataDetail.getObjPunchCard().getDescription());
           /* TODO here is the change 6 may 2018*/
            // txtName.setText(CommonUtility.DataDetail.getBusinessName());

            txtName.setText(CommonUtility.DataDetail.getObjPunchCard().getPunchCardName());
            Log.e("104", ""+CommonUtility.DataDetail.getObjPunchCard().getPunchCardName());
            CommonUtility.DataDetail = null;
        } catch (Exception ex) {
            Log.e("104", "ActivityMyPunchCards");
        }
    }

    class MyDecoder extends AsyncTask<String, Void, Bitmap> {

        ImageView img;
        int w, h;

        public MyDecoder(ImageView img, int w, int h) {
            this.img = img;
            this.w = w;
            this.h = h;
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                android.util.Log.e("params[0]: ", "" + params[0]);
                Bitmap b = Bitmap.createScaledBitmap(SuperActivity.Decodebase64Image(params[0]), this.w, this.h, false);
                return b;

            } catch (NullPointerException e) {
                android.util.Log.e("NullPointerException: ", "" + e);
            } catch (Exception e) {
                android.util.Log.e("Exception: ", "" + e);
            }

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                this.img.setImageBitmap(bitmap);
            } else {
                //this.img.setImageDrawable(mContext.getDrawable(R.drawable.icon_circle_card));
            }

        }
    }

    private void arrangePunchCard(int totalPunch, int freePunches) {
        int usePunches = totalPunch - freePunches;
        switch (totalPunch) {
            case 1:
                ivPunch5.setVisibility(View.VISIBLE);
                makeGreen(usePunches, 4);
                break;
            case 2:
                ivPunch5.setVisibility(View.VISIBLE);
                ivPunch6.setVisibility(View.VISIBLE);
                makeGreen(usePunches, 4);
                break;
            case 3:
                ivPunch4.setVisibility(View.VISIBLE);
                ivPunch5.setVisibility(View.VISIBLE);
                ivPunch6.setVisibility(View.VISIBLE);
                makeGreen(usePunches, 3);
                break;
            case 4:
                ivPunch4.setVisibility(View.VISIBLE);
                ivPunch5.setVisibility(View.VISIBLE);
                ivPunch6.setVisibility(View.VISIBLE);
                ivPunch7.setVisibility(View.VISIBLE);
                makeGreen(usePunches, 3);
                break;
            case 5:
                ivPunch3.setVisibility(View.VISIBLE);
                ivPunch4.setVisibility(View.VISIBLE);
                ivPunch5.setVisibility(View.VISIBLE);
                ivPunch6.setVisibility(View.VISIBLE);
                ivPunch7.setVisibility(View.VISIBLE);
                makeGreen(usePunches, 2);
                break;
            case 6:
                ivPunch3.setVisibility(View.VISIBLE);
                ivPunch4.setVisibility(View.VISIBLE);
                ivPunch5.setVisibility(View.VISIBLE);
                ivPunch6.setVisibility(View.VISIBLE);
                ivPunch7.setVisibility(View.VISIBLE);
                ivPunch8.setVisibility(View.VISIBLE);
                makeGreen(usePunches, 2);
                break;
            case 7:
                ivPunch2.setVisibility(View.VISIBLE);
                ivPunch3.setVisibility(View.VISIBLE);
                ivPunch4.setVisibility(View.VISIBLE);
                ivPunch5.setVisibility(View.VISIBLE);
                ivPunch6.setVisibility(View.VISIBLE);
                ivPunch7.setVisibility(View.VISIBLE);
                ivPunch8.setVisibility(View.VISIBLE);
                makeGreen(usePunches, 1);
                break;
            case 8:
                ivPunch2.setVisibility(View.VISIBLE);
                ivPunch3.setVisibility(View.VISIBLE);
                ivPunch4.setVisibility(View.VISIBLE);
                ivPunch5.setVisibility(View.VISIBLE);
                ivPunch6.setVisibility(View.VISIBLE);
                ivPunch7.setVisibility(View.VISIBLE);
                ivPunch8.setVisibility(View.VISIBLE);
                ivPunch9.setVisibility(View.VISIBLE);
                makeGreen(usePunches, 1);
                break;
            case 9:
                ivPunch1.setVisibility(View.VISIBLE);
                ivPunch2.setVisibility(View.VISIBLE);
                ivPunch3.setVisibility(View.VISIBLE);
                ivPunch4.setVisibility(View.VISIBLE);
                ivPunch5.setVisibility(View.VISIBLE);
                ivPunch6.setVisibility(View.VISIBLE);
                ivPunch7.setVisibility(View.VISIBLE);
                ivPunch8.setVisibility(View.VISIBLE);
                ivPunch9.setVisibility(View.VISIBLE);
                makeGreen(usePunches, 0);
                break;
            case 10:
                ivPunch1.setVisibility(View.VISIBLE);
                ivPunch2.setVisibility(View.VISIBLE);
                ivPunch3.setVisibility(View.VISIBLE);
                ivPunch4.setVisibility(View.VISIBLE);
                ivPunch5.setVisibility(View.VISIBLE);
                ivPunch6.setVisibility(View.VISIBLE);
                ivPunch7.setVisibility(View.VISIBLE);
                ivPunch8.setVisibility(View.VISIBLE);
                ivPunch9.setVisibility(View.VISIBLE);
                ivPunch10.setVisibility(View.VISIBLE);
                makeGreen(usePunches, 0);
                break;

        }
    }


    private void makeGreen(int usesPunches, int startingpoint) {
        for (int i = 0; i < usesPunches; i++) {
            Picasso.with(this).load(R.drawable.icon_green)
                    .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                    .into(ivPunches[startingpoint]);
            startingpoint++;
        }
    }

    private void makeGreen(int usePunches) {
        switch (usePunches) {
            case 1:
                ivPunch5.setVisibility(View.VISIBLE);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);

                break;
            case 2:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);


                break;
            case 3:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);

                break;
            case 4:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch7);

                break;
            case 5:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch7);

                break;
            case 6:

                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch7);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch8);

                break;
            case 7:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch7);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch8);


                break;
            case 8:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch7);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch8);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch9);

                break;
            case 9:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch7);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch8);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch9);

                break;
            case 10:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch7);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch8);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch9);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch10);
                break;

        }
    }


    /*private void makeGreen(int usePunches){
        switch(usePunches){
            case 1:
                ivPunch5.setVisibility(View.VISIBLE);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);

                break;
            case 2:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);



                break;
            case 3:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);

                break;
            case 4:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);

                break;
            case 5:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);

                break;
            case 6:

                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);

                break;
            case 7:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch7);



                break;
            case 8:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch7);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch8);

                break;
            case 9:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch7);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch8);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch9);

                break;
            case 10:
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch1);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch2);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch3);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch4);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch5);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch6);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch7);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch8);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch9);
                Picasso.with(this).load(R.drawable.icon_green)
                        .resize(CommonUtility.dpToPx(this, 25), CommonUtility.dpToPx(this, 25))
                        .into(ivPunch10);
                break;

        }
    }*/
    private void makeLoayaltyCard() {
       // Log.e("makeLoayaltyCard:", "" + CommonUtility.DataDetail.getLoyaltyTemplatePath());
        layout_punches.setVisibility(View.GONE);
        layout_text.setVisibility(View.VISIBLE);
        tvpoint.setText(CommonUtility.DataDetail.getLoyalityTotalPoints() + " POINTS");
        txtName.setVisibility(View.VISIBLE);
        txtMerchantName.setText(CommonUtility.DataDetail.getBusinessName());
        if (CommonUtility.DataDetail != null) {
            if(CommonUtility.DataDetail.getLoyaltyTemplatePath()!=null){
                if ((CommonUtility.DataDetail.getLoyaltyTemplatePath().startsWith("http://"))) {
                    Picasso.with(this).load(CommonUtility.DataDetail.getLoyaltyTemplatePath())
                            .into(ivPunchCard);
                } else if ((CommonUtility.DataDetail.getLoyaltyTemplatePath().startsWith("https://"))) {
                    Picasso.with(this).load(CommonUtility.DataDetail.getLoyaltyTemplatePath())
                            .into(ivPunchCard);
                } else {
                    hei = CommonUtility.dpToPx(this, 200);
                    wei = CommonUtility.dpToPx(this, 200);
                    MyDecoder d = new MyDecoder(ivPunchCard, wei, hei);
                    d.execute(CommonUtility.DataDetail.getProfileImage());
                }
            }
            ivBarCode.setImageDrawable(barcodeUtil.generateBarcode(CommonUtility.DataDetail.getLoyalityBarcode()));
            tvBarcode.setText("BARCODE: " + CommonUtility.DataDetail.getLoyalityBarcode());
            txtName.setText(CommonUtility.DataDetail.getLoyalityCardName());
        }
        CommonUtility.DataDetail = null;
    }


    public void addListener() {

    }


}
