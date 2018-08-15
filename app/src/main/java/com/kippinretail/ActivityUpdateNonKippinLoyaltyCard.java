package com.kippinretail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.AppStatus;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.DbNew;
import com.kippinretail.ApplicationuUlity.NonKippinCardType;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.ResponseToGetLoyaltyCardLocalModel;
import com.kippinretail.callbacks.DialogListener;
import com.kippinretail.interfaces.OnLocationGet;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityUpdateNonKippinLoyaltyCard extends SuperActivity {

    CircleImageView iv_logoImage;
    ImageView iv_frontImage, iv_backImage;
    TextView txt_folderName, txt_barcode, tvTopbarTitle;
    NonKippinCardType nonKippinCardType;
    RelativeLayout lalayout_ivBack;
    ProgressBar progressBar_icon, progressBar_front, progressBar_back;
    ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCardLocalModel;
    private ShareType shareType;
    private ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCard;
    boolean editCheck = false;
    TextView edit;
    TextView btn_upload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_update_non_kippin_loyalty_card);
        Log.e("onCreate", "onCreate");
        initialiseUI();
        ;
        setUpUI();
        setUpListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Remove Notification for IsNonKippinPhysicalCard
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        edit = (TextView) findViewById(R.id.edit);
        lalayout_ivBack = (RelativeLayout) findViewById(R.id.lalayout_ivBack);
        btn_upload = (TextView) findViewById(R.id.btn_upload);
        iv_logoImage = (CircleImageView) findViewById(R.id.iv_logoImage);
        iv_frontImage = (ImageView) findViewById(R.id.iv_frontImage);
        iv_backImage = (ImageView) findViewById(R.id.iv_backImage);
        txt_folderName = (TextView) findViewById(R.id.txt_folderName);
        txt_barcode = (TextView) findViewById(R.id.txt_barcode);
        tvTopbarTitle = (TextView) findViewById(R.id.tvTopbarTitle);
        progressBar_icon = (ProgressBar) findViewById(R.id.progressBar_icon);
        progressBar_front = (ProgressBar) findViewById(R.id.progressBar_front);
        progressBar_back = (ProgressBar) findViewById(R.id.progressBar_back);
        try {
            progressBar_icon.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
            progressBar_front.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
            progressBar_back.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }


        Gson gson = new Gson();
        responseToGetLoyaltyCard = gson.fromJson(CommonUtility.NonKippinGiftCardDatra, ResponseToGetLoyaltyCardLocalModel.class);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCard();
            }
        });


    }

    @Override
    public void setUpUI() {
        super.setUpUI();
        nonKippinCardType = (NonKippinCardType) getIntent().getSerializableExtra("NonKippinCardType");
        shareType = (ShareType) getIntent().getSerializableExtra("shareType");
        if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
            tvTopbarTitle.setText("User Loyalty Card");

        } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
            tvTopbarTitle.setText("User Gift Card");

        } else if (shareType == ShareType.DONATEGIFTCARD) {
            tvTopbarTitle.setText("User Gift Card");
        } else if (shareType == ShareType.SHAREGIFTCARD) {
            tvTopbarTitle.setText("User Gift Card");
        } else {
            tvTopbarTitle.setText("User Gift Card");
        }


        boolean isFromDb = getIntent().getBooleanExtra("isFromDb", false);
        boolean isLocal = getIntent().getBooleanExtra("isFromLocal", false);
        editCheck = getIntent().getBooleanExtra("edit", false);
        if (editCheck) {
            edit.setVisibility(View.GONE);
            btn_upload.setVisibility(View.VISIBLE);
        }
        Log.e("isFromDb:", "" + isFromDb);
        Log.e("isLocal:", "" + isLocal);
        if (!isFromDb) {
            progressBar_icon.setVisibility(View.VISIBLE);
            progressBar_front.setVisibility(View.VISIBLE);
            progressBar_back.setVisibility(View.VISIBLE);
            if (responseToGetLoyaltyCard.getLogoImage() != null) {
                Log.e("Orignal:", "" + responseToGetLoyaltyCard.getOrignalLogoImage());
            } else {
                Log.e("Orignal:", "" + responseToGetLoyaltyCard.getOrignalLogoImage());
            }

            if (responseToGetLoyaltyCard.getLogoImage().equals("")) {
                progressBar_icon.setVisibility(View.GONE);
            } else {

                Picasso.with(this).load(responseToGetLoyaltyCard.getLogoImage().replace("//", "/")).
                        into(iv_logoImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar_icon.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
            Log.e("getFrontImage:", "" + responseToGetLoyaltyCard.getFrontImage());
            Picasso.with(this).load(responseToGetLoyaltyCard.getFrontImage().replace("//", "/")).
                    into(iv_frontImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar_front.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            Log.e("getBackImage:", "" + responseToGetLoyaltyCard.getBackImage());
            Picasso.with(this).load(responseToGetLoyaltyCard.getBackImage().replace("//", "/")).
                    into(iv_backImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar_back.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } else {
            try {
                if (isLocal) {
                    Log.e("nonKippinCardType:", "" + nonKippinCardType);
                    if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
                        responseToGetLoyaltyCardLocalModel = DbNew.getInstance(ActivityUpdateNonKippinLoyaltyCard.this).getParticularRecord(getIntent().getStringExtra("LoyaltyCardId"));
                    } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                        responseToGetLoyaltyCardLocalModel = DbNew.getInstance(ActivityUpdateNonKippinLoyaltyCard.this).getParticularRecordGiftCard(getIntent().getStringExtra("LoyaltyCardId"));
                    }
                    Log.e("getId():", "" + responseToGetLoyaltyCardLocalModel.getId());
                    String FrontImage = responseToGetLoyaltyCardLocalModel.getFrontImage();
                    String BackImage = responseToGetLoyaltyCardLocalModel.getBackImage();
                    String LogoImage = responseToGetLoyaltyCardLocalModel.getLogoImage();
                    Log.e("BackImage:", "" + BackImage);
                    Log.e("LogoImage:", "" + LogoImage);
                    Log.e("FrontImage:", "" + FrontImage);
                    new MyDecoder(iv_logoImage).execute(LogoImage);
                    new MyDecoder(iv_frontImage).execute(FrontImage);
                    new MyDecoder(iv_backImage).execute(BackImage);





/*
                    if (responseToGetLoyaltyCard.getOrignalLogoImage().equals("")) {
                        progressBar_icon.setVisibility(View.GONE);
                    } else {
                        Picasso.with(this).load(responseToGetLoyaltyCard.getOrignalLogoImage().replace("//", "/")).
                                into(iv_logoImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressBar_icon.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }
                    Log.e("getFrontImage:", "" + responseToGetLoyaltyCard.getOrignalFrontImage());
                    Picasso.with(this).load(responseToGetLoyaltyCard.getOrignalFrontImage().replace("//", "/")).
                            into(iv_frontImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar_front.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                    Log.e("getBackImage:", "" + responseToGetLoyaltyCard.getOrignalBackImage());
                    Picasso.with(this).load(responseToGetLoyaltyCard.getOrignalBackImage().replace("//", "/")).
                            into(iv_backImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar_back.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });*/


                } else {
                    byte[] decodedLogoString = Base64.decode(responseToGetLoyaltyCard.getLogoImage(), Base64.DEFAULT);
                    Bitmap decodedByteForLogo = BitmapFactory.decodeByteArray(decodedLogoString, 0, decodedLogoString.length);
                    String pathForLogo = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, decodedByteForLogo));
                    iv_logoImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(pathForLogo, CommonUtility.dpToPx(this, 90),
                            CommonUtility.dpToPx(this, 90)));

                    byte[] decodedFirstString = Base64.decode(responseToGetLoyaltyCard.getFrontImage(), Base64.DEFAULT);
                    Bitmap decodedByteForFront = BitmapFactory.decodeByteArray(decodedFirstString, 0, decodedFirstString.length);
                    String pathForFront = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, decodedByteForFront));
                    iv_frontImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(pathForFront, CommonUtility.dpToPx(this, CommonUtility.getWidthOfScreen(this) - CommonUtility.dpToPx(this, 100)),
                            CommonUtility.dpToPx(this, 130)));

                    byte[] decodedBackString = Base64.decode(responseToGetLoyaltyCard.getBackImage(), Base64.DEFAULT);
                    Bitmap decodedByteForBack = BitmapFactory.decodeByteArray(decodedBackString, 0, decodedBackString.length);
                    String pathForBack = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, decodedByteForBack));
                    iv_backImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(pathForBack, CommonUtility.dpToPx(this, CommonUtility.getWidthOfScreen(this) - CommonUtility.dpToPx(this, 70)),
                            CommonUtility.dpToPx(this, 130)));
                }

            } catch (Exception ex) {

            } catch (Error ex) {

            }
        }

        txt_folderName.setText(getIntent().getStringExtra("FolderName"));
        txt_barcode.setText(getIntent().getStringExtra("Barcode"));

    }

    public void deleteLoyaltyCard() {
        try {
            MessageDialog.showVerificationDialog(this, "Are you sure you want to delete this card ?", new DialogListener() {
                @Override
                public void handleYesButton() {
                    /*Network.With(EditLoyaltyCardActivity.this).getLocationParam(new OnLocationGet() {
                        @Override
                        public void onLocationGet(double lattitude, double longitude, String mCountry, String city) {*/
                    HashMap<String, String> jsonBody = new HashMap<String, String>();
                    Log.e("LoyaltyCardId:", "" + responseToGetLoyaltyCard.getId());
                    Log.e("FolderName:", "" + txt_folderName.getText().toString());
                    Log.e("ActualUserId:", "" + String.valueOf(CommonData.getUserData(ActivityUpdateNonKippinLoyaltyCard.this).getId()));

                    jsonBody.put("Id", responseToGetLoyaltyCard.getId());
                    //     jsonBody.put("LogoImage", CommonUtility.encodeTobase64(iv_logoImage_drawable.getBitmap()));
                    jsonBody.put("FolderName", txt_folderName.getText().toString());
                    //    jsonBody.put("FrontImage", CommonUtility.encodeTobase64(frontImage));
                    //    jsonBody.put("BackImage", CommonUtility.encodeTobase64(backImage));
                    //    jsonBody.put("Country", mCountry);
                    jsonBody.put("ActualUserId", String.valueOf(CommonData.getUserData(ActivityUpdateNonKippinLoyaltyCard.this).getId()));
                    jsonBody.put("MerchantId", "0");
                    //      jsonBody.put("Barcode", barcode);

                    LoadingBox.showLoadingDialog(ActivityUpdateNonKippinLoyaltyCard.this, "Loading...");
                    RestClient.getApiServiceForPojo().DeleteLoyaltyCard(jsonBody, new retrofit.Callback<JsonElement>() {
                        @Override
                        public void success(JsonElement jsonElement, Response response) {
                            Log.e("output ==>", jsonElement.toString());
                            Log.e("URL -->", response.getUrl());
                            Type listType = new TypeToken<List<Merchant>>() {
                            }.getType();
                            Gson gson = new Gson();
                            LoadingBox.dismissLoadingDialog();
                            Boolean serverresponse = gson.fromJson(jsonElement.toString(), Boolean.class);
                            if (serverresponse.booleanValue()) {
                                DbNew.getInstance(ActivityUpdateNonKippinLoyaltyCard.this).deleteParticularNonLoyalitycard(getIntent().getStringExtra("LoyaltyCardId"));

                                MessageDialog.showDialog(ActivityUpdateNonKippinLoyaltyCard.this,
                                        "Physical card successfully Deleted", MyLoayaltyCardListActivity.class);
                                    /*CommonUtility.moveToTarget(EditLoyaltyCardActivity.this, MyLoayaltyCardListActivity.class);*/
                            } else {
                                MessageDialog.showDialog(ActivityUpdateNonKippinLoyaltyCard.this,
                                        "Some error in deleting card.Please try again later", false);
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(error.getUrl(), error.getMessage());
                            LoadingBox.dismissLoadingDialog();
                            MessageDialog.showFailureDialog(ActivityUpdateNonKippinLoyaltyCard.this);
                        }
                    });
                      /*  }


                    });*/
                }
            });
        } catch (Exception ex) {

        }

    }

    public void deleteCard() {
        if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
            deleteLoyaltyCard();
        } else if (nonKippinCardType == NonKippinCardType.GIFTCARD || shareType == ShareType.DONATEGIFTCARD) {
            deleteGiftCard();
        } else {
            deleteGiftCard();
        }

    }

    public void deleteGiftCard() {
        try {
            MessageDialog.showVerificationDialog(this, "Are you sure you want to delete this card?", new DialogListener() {
                @Override
                public void handleYesButton() {
                    Network.With(ActivityUpdateNonKippinLoyaltyCard.this).getLocationParam(new OnLocationGet() {
                        @Override
                        public void onLocationGet(double lattitude, double longitude, String mCountry, String city) {
                            HashMap<String, String> jsonBody = new HashMap<String, String>();
                            jsonBody.put("Id", responseToGetLoyaltyCard.getId());
                            jsonBody.put("FolderName", txt_folderName.getText().toString());
                            jsonBody.put("ActualUserId", String.valueOf(CommonData.getUserData(ActivityUpdateNonKippinLoyaltyCard.this).getId()));
                            jsonBody.put("MerchantId", "0");
                            Log.e("jsonBody:", "" + jsonBody);

                            LoadingBox.showLoadingDialog(ActivityUpdateNonKippinLoyaltyCard.this, "Loading...");
                            RestClient.getApiServiceForPojo().DeletePhysicalGiftCard(jsonBody, new retrofit.Callback<JsonElement>() {
                                @Override
                                public void success(JsonElement jsonElement, Response response) {
                                    Log.e("output ==>", jsonElement.toString());
                                    Log.e("URL -->", response.getUrl());
                                    Type listType = new TypeToken<List<Merchant>>() {
                                    }.getType();
                                    Gson gson = new Gson();
                                    LoadingBox.dismissLoadingDialog();
                                    Boolean serverresponse = gson.fromJson(jsonElement.toString(), Boolean.class);
                                    if (serverresponse.booleanValue()) {
                                        if (shareType == ShareType.DONATEGIFTCARD) {
                                            DbNew.getInstance(ActivityUpdateNonKippinLoyaltyCard.this).deleteParticularNonKippinGiftcard(getIntent().getStringExtra("LoyaltyCardId"));

                                            MessageDialog.showDialog(ActivityUpdateNonKippinLoyaltyCard.this,
                                                    "Physical card successfully Deleted", GiftCardListActivity.class, shareType);

                                        } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                                            DbNew.getInstance(ActivityUpdateNonKippinLoyaltyCard.this).deleteParticularNonKippinGiftcard(getIntent().getStringExtra("LoyaltyCardId"));
                                            MessageDialog.showDialog(ActivityUpdateNonKippinLoyaltyCard.this,
                                                    "Physical card successfully Deleted", GiftCardListActivity.class);
                                        }
                                        else{
                                            MessageDialog.showDialog(ActivityUpdateNonKippinLoyaltyCard.this,
                                                    "Physical card successfully Deleted", ActivityMerchantGiftCardList.class);
                                        }

                                    } else {
                                        MessageDialog.showDialog(ActivityUpdateNonKippinLoyaltyCard.this,
                                                "Some error in deleting card.Please try again later", false);
                                        //CommonUtility.moveToTarget(ActivityUpdateNonKippinLoyaltyCard.this, GiftCardListActivity.class);
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.e(error.getUrl(), error.getMessage());
                                    LoadingBox.dismissLoadingDialog();
                                    MessageDialog.showFailureDialog(ActivityUpdateNonKippinLoyaltyCard.this);
                                }
                            });
                        }

                    });
                }
            });
        } catch (Exception ex) {

        }

    }

    @Override
    public void setUpListeners() {
        super.setUpListeners();
        lalayout_ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

            }
        });
    }

    public void editLoyaltyCard(View v) {
        if (AppStatus.getInstance(this).isOnline(this)) {
            Intent i = new Intent();
            i.setClass(this, EditLoyaltyCardActivity.class);
            if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
                i.putExtra("NonKippinCardType", nonKippinCardType);
            } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                i.putExtra("NonKippinCardType", nonKippinCardType);
            } else if (shareType == ShareType.DONATEGIFTCARD) {
                i.putExtra("shareType", shareType);
            } else if (shareType == ShareType.SHAREGIFTCARD) {
                i.putExtra("shareType", shareType);
            }
            String FrontImage = getIntent().getStringExtra("FrontImage");
            String BackImage = getIntent().getStringExtra("BackImage");
            String LogoImage = getIntent().getStringExtra("LogoImage");
            i.putExtra("LoyaltyCardId", getIntent().getStringExtra("LoyaltyCardId"));
            i.putExtra("LogoImage", LogoImage);
            i.putExtra("FolderName", getIntent().getStringExtra("FolderName"));
            i.putExtra("FrontImage", FrontImage);
            i.putExtra("BackImage", BackImage);
            i.putExtra("Barcode", getIntent().getStringExtra("Barcode"));
            i.putExtra("merchantId", getIntent().getStringExtra("merchantId"));
            //finish();
            startActivity(i);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        } else {
            MessageDialog.showDialog(this, "Please check your internet connection,we are unable to edit card without internet", false);
        }

    }

    class MyDecoder extends AsyncTask<String, Void, Bitmap> {

        ImageView img;
        int hei, wei;

        public MyDecoder(ImageView img) {
            this.img = img;
            hei = CommonUtility.dpToPx(ActivityUpdateNonKippinLoyaltyCard.this, 60);
            wei = CommonUtility.dpToPx(ActivityUpdateNonKippinLoyaltyCard.this, 60);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap b = null;
            Log.e("BITMAP URL --", "== " + params[0]);
            if (params[0] != null && !params[0].equals("")) {
                b = SuperActivity.Decodebase64Image(params[0]);
                // b = Bitmap.createScaledBitmap(SuperActivity.Decodebase64Image(params[0]), wei, hei, false);
                //b = Bitmap.createScaledBitmap(SuperActivity.Decodebase64Image(params[0]));
            }

            return b;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                this.img.setImageBitmap(bitmap);
            } else {
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.icon_circle_card);
                this.img.setImageBitmap(image);
                //this.img.setImageDrawable(mContext.getDrawable(R.drawable.icon_circle_card));
            }

        }
    }


}
