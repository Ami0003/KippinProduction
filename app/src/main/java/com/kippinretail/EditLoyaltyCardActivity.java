package com.kippinretail;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.ApplicationuUlity.BarcodeUtil;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.DbNew;
import com.kippinretail.ApplicationuUlity.NonKippinCardType;
import com.kippinretail.ApplicationuUlity.ShareType;
import com.kippinretail.CommonDialog.MessageDialog;
import com.kippinretail.Interface.OnBarcodeGetListener;
import com.kippinretail.Modal.MerchantList.Merchant;
import com.kippinretail.Modal.NonKippincard;
import com.kippinretail.Modal.ResponseModal;
import com.kippinretail.Modal.ResponseToGetLoyaltyCard;
import com.kippinretail.Modal.ResponseToGetLoyaltyCardLocalModel;
import com.kippinretail.callbacks.DialogListener;
import com.kippinretail.config.Config;
import com.kippinretail.interfaces.OnLocationGet;
import com.kippinretail.kippingallerypreview.KippinCloudGalleryActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.scanning.ScannerActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EditLoyaltyCardActivity extends SuperActivity {
    private final static int REQUEST_SCANNER = 1;
    boolean is_iv_cardIcon_cliked;
    CircleImageView iv_logoImage;
    private Uri imageUri;
    ImageView iv_frontImage, iv_backImage;
    TextView txt_folderName, txt_barcode, txt_save, tvTopbarTitle;
    NonKippinCardType nonKippinCardType;
    private boolean is_iv_logoImage_cliked, is_btnFrontImage_cliked;
    BarcodeUtil barcodeUtil;
    private Bitmap photo, frontImage, backImage, logoImage;
    private String barcode;
    private RelativeLayout lalayout_ivBack;
    private String lImage, fImage, bImage;
    private ShareType shareType;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final String TAG = "NONKIPPINGIFTCARD";
    public static final int REQUEST_CODE_GALLERY = 2;
    public static final int REQUEST_CODE_TAKE_PICTURE = 3;

    private File mFileTemp;
    private static int CROP_IMAGE = 111;
    private Bitmap logoImageBitmap;
    private Dialog lDialog;
    private String imagePath;
    private Uri u1;
    private Uri mImageUri;
    ProgressBar progressBar_icon, progressBar_front, progressBar_back;
    boolean isCamerClicked, isGalleryClicked;
    private ResponseToGetLoyaltyCard responseToGetLoyaltyCard;
    ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCardLocalModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_loyalty_card);
        initialiseUI();
        NonKippinGiftCardListActivity.kippinGallery = false;
        setUpUI();
        setUpListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NonKippinGiftCardListActivity.kippinGallery) {
            // NonKippinGiftCardListActivity.LogoTemplate;
            Log.e("LogoTemplate:", "" + NonKippinGiftCardListActivity.LogoTemplate);
            txt_folderName.setText(NonKippinGiftCardListActivity.CardName);
            Picasso.with(this).load(NonKippinGiftCardListActivity.LogoTemplate).
                    into(iv_logoImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar_icon.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            // txtcardName.setText(CardName);
            // Picasso.with(this).load(NonKippinGiftCardListActivity.LogoTemplate).fit().centerCrop().into(iv_cardIcon);
        }
        if (Config.scanner) {
            android.util.Log.e("----CONTENT:----:", "" + Config.CONTENT);
            android.util.Log.e("---FORMAT:-------", "" + Config.FORMAT);

            Config.scanner = false;
            if (Config.CONTENT.equals("")) {

            } else {
                String cardNo = Config.CONTENT;
                String card_No = Config.FORMAT;
                android.util.Log.e("----cardNo----:", "" + cardNo);
                android.util.Log.e("---card_No:-------", "" + card_No);

                barcode = cardNo;
                txt_barcode.setText(cardNo);
                BitmapDrawable d = (BitmapDrawable) barcodeUtil.generateBarcode(cardNo);
                backImage = d.getBitmap();
                try {
                    bImage = CommonUtility.encodeTobase64(backImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Config.FORMAT = "";
                Config.CONTENT = "";
            }


        }
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        barcodeUtil = new BarcodeUtil(this);
        lalayout_ivBack = (RelativeLayout) findViewById(R.id.lalayout_ivBack);
        iv_logoImage = (CircleImageView) findViewById(R.id.iv_logoImage);
        iv_frontImage = (ImageView) findViewById(R.id.iv_frontImage);
        iv_backImage = (ImageView) findViewById(R.id.iv_backImage);
        txt_folderName = (TextView) findViewById(R.id.txt_folderName);
        txt_barcode = (TextView) findViewById(R.id.txt_barcode);
        txt_save = (TextView) findViewById(R.id.txt_save);
        tvTopbarTitle = (TextView) findViewById(R.id.tvTopbarTitle);
        String state = Environment.getExternalStorageState();
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
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(),
                    TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }

        Gson gson = new Gson();
        responseToGetLoyaltyCard = gson.fromJson(CommonUtility.NonKippinGiftCardDatra, ResponseToGetLoyaltyCard.class);
    }

    // logo
    class MyDecoderLogo extends AsyncTask<String, Void, Bitmap> {

        ImageView img;
        int hei, wei;

        public MyDecoderLogo(ImageView img) {
            this.img = img;
            //  hei = CommonUtility.dpToPx(ActivityUpdateNonKippinLoyaltyCard.this, 60);
            //  wei = CommonUtility.dpToPx(ActivityUpdateNonKippinLoyaltyCard.this, 60);
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
                logoImageBitmap = bitmap;
            } else {
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.icon_circle_card);
                this.img.setImageBitmap(image);
                //this.img.setImageDrawable(mContext.getDrawable(R.drawable.icon_circle_card));
            }

        }
    }


    //front image
    class MyDecoderFront extends AsyncTask<String, Void, Bitmap> {

        ImageView img;
        int hei, wei;

        public MyDecoderFront(ImageView img) {
            this.img = img;
            //  hei = CommonUtility.dpToPx(ActivityUpdateNonKippinLoyaltyCard.this, 60);
            //  wei = CommonUtility.dpToPx(ActivityUpdateNonKippinLoyaltyCard.this, 60);
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
                frontImage = bitmap;
            } else {
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.icon_circle_card);
                this.img.setImageBitmap(image);

                //this.img.setImageDrawable(mContext.getDrawable(R.drawable.icon_circle_card));
            }

        }
    }

    // backimage
    class MyDecoderBackImage extends AsyncTask<String, Void, Bitmap> {

        ImageView img;
        int hei, wei;

        public MyDecoderBackImage(ImageView img) {
            this.img = img;
            //  hei = CommonUtility.dpToPx(ActivityUpdateNonKippinLoyaltyCard.this, 60);
            //  wei = CommonUtility.dpToPx(ActivityUpdateNonKippinLoyaltyCard.this, 60);
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
                try {
                    bImage = CommonUtility.encodeTobase64(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.icon_circle_card);
                this.img.setImageBitmap(image);
                //this.img.setImageDrawable(mContext.getDrawable(R.drawable.icon_circle_card));
            }

        }
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
        }
        try {

            //String FrontImage = getIntent().getStringExtra("FrontImage");
            // String BackImage = getIntent().getStringExtra("BackImage");
            // String LogoImage = getIntent().getStringExtra("LogoImage");
            if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
                responseToGetLoyaltyCardLocalModel = DbNew.getInstance(EditLoyaltyCardActivity.this).getParticularRecord(getIntent().getStringExtra("LoyaltyCardId"));


            } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {

                responseToGetLoyaltyCardLocalModel = DbNew.getInstance(EditLoyaltyCardActivity.this).getParticularRecordGiftCard(getIntent().getStringExtra("LoyaltyCardId"));

            }

            //responseToGetLoyaltyCardLocalModel = DbNew.getInstance(EditLoyaltyCardActivity.this).getParticularRecord(getIntent().getStringExtra("LoyaltyCardId"));
            Log.e("getId():", "" + responseToGetLoyaltyCardLocalModel.getId());
            String FrontImage = responseToGetLoyaltyCardLocalModel.getFrontImage();
            String BackImage = responseToGetLoyaltyCardLocalModel.getBackImage();
            String LogoImage = responseToGetLoyaltyCardLocalModel.getLogoImage();
            new MyDecoderLogo(iv_logoImage).execute(LogoImage);
            new MyDecoderFront(iv_frontImage).execute(FrontImage);
            new MyDecoderBackImage(iv_backImage).execute(BackImage);
            // iv_logoImage.setImageBitmap(SuperActivity.Decodebase64Image(LogoImage));
            // iv_frontImage.setImageBitmap(SuperActivity.Decodebase64Image(FrontImage));
            // iv_backImage.setImageBitmap(SuperActivity.Decodebase64Image(BackImage));

           /* if (LogoImage.equals("http://52.27.249.143/Kippin/wallet/") || LogoImage.equals("")) {
                Log.e("IMAGEIMAGEIMAGE:", "IMAGEIMAGEIMAGE");
            } else {
                Picasso.with(this).load(LogoImage).
                        into(iv_logoImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar_icon.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }

            Picasso.with(this).load(FrontImage.replace("//", "/")).
                    into(iv_frontImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar_front.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
            Log.e("BackImage:", "" + BackImage);
            Log.e("FrontImage:", "" + FrontImage);
            Log.e("LogoImage:", "" + LogoImage);
            Picasso.with(this).load(BackImage.replace("//", "/")).
                    into(iv_backImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar_back.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });*/


           /* byte[] decodedLogoString = Base64.decode(responseToGetLoyaltyCard.getLogoImage(), Base64.DEFAULT);
            Bitmap decodedByteForLogo = BitmapFactory.decodeByteArray(decodedLogoString, 0, decodedLogoString.length);
            String pathForLogo = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, decodedByteForLogo));
            iv_logoImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(pathForLogo, CommonUtility.dpToPx(this, 90),
                    CommonUtility.dpToPx(this, 90)));

            byte[] decodedFirstString = Base64.decode(responseToGetLoyaltyCard.getFrontImage(), Base64.DEFAULT);
            Bitmap decodedByteForFront = BitmapFactory.decodeByteArray(decodedFirstString, 0, decodedFirstString.length);
            String pathForFront = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, decodedByteForFront));
            iv_frontImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(pathForFront, CommonUtility.dpToPx(this, CommonUtility.getWidthOfScreen(this)-CommonUtility.dpToPx(this,100)),
                    CommonUtility.dpToPx(this, 130)));

            byte[] decodedBackString = Base64.decode(responseToGetLoyaltyCard.getBackImage(), Base64.DEFAULT);
            Bitmap decodedByteForBack = BitmapFactory.decodeByteArray(decodedBackString, 0, decodedBackString.length);
            String pathForBack = CommonUtility.getRealPathFromURI(this, CommonUtility.getImageUri(this, decodedByteForBack));
            iv_backImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(pathForBack, CommonUtility.dpToPx(this, CommonUtility.getWidthOfScreen(this)-CommonUtility.dpToPx(this,70)),
                    CommonUtility.dpToPx(this, 130)));*/
        } catch (Exception ex) {

        } catch (Error ex) {

        }
        txt_folderName.setText(getIntent().getStringExtra("FolderName"));
        txt_barcode.setText(getIntent().getStringExtra("Barcode"));
        /*Picasso.with(this).load(getIntent().getStringExtra("LogoImage").replace("//", "/")).
                into(iv_logoImage, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                progressBar_icon.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });*/

        /*Picasso.with(this).load(getIntent().getStringExtra("FrontImage").replace("//", "/")).
                resize(CommonUtility.getWidthOfScreen(this), CommonUtility.dpToPx(this, 160)).into(iv_frontImage, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                progressBar_front.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });*/
        /*Picasso.with(this).load(getIntent().getStringExtra("BackImage").replace("//", "/")).
                into(iv_backImage, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                progressBar_back.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });*/


    }

    @Override
    public void setUpListeners() {
        super.setUpListeners();
        lalayout_ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NonKippinGiftCardListActivity.kippinGallery = false;
                finish();
                overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

            }
        });
        iv_logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_iv_cardIcon_cliked = true;
                CROP_IMAGE = 0;
                dialogForGalleryImage();
                //dialogForImage();
                // UploadImageDialog.showUploadImageDialog(EditLoyaltyCardActivity.this);
            }
        });
        iv_frontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_iv_cardIcon_cliked = false;
                CROP_IMAGE = 0;
                // dialogForImage();
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("KIPPIN CROP")
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setCropMenuCropButtonTitle("Done")
                        .setRequestedSize(400, 400)
                        .setCropMenuCropButtonIcon(R.drawable.ic_tick)
                        .start(EditLoyaltyCardActivity.this);


               /* is_btnFrontImage_cliked = true;
                UploadImageDialog.showUploadImageDialog(EditLoyaltyCardActivity.this);*/
            }
        });
        iv_backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  CROP_IMAGE = 111;
                Intent i = new Intent();
                i.setClass(EditLoyaltyCardActivity.this, ScannerActivity.class);
                //startActivity(i);
                startActivityForResult(i, REQUEST_SCANNER);
                /*barcodeUtil.launchScanner(CommonUtility.REQUEST_CODE_BARCODE, new HandleManualInput() {

                    @Override
                    public void onManualClick() {
                        Intent i = new Intent();
                        i.setClass(EditLoyaltyCardActivity.this, ActivityINputManualBarcode.class);
                        startActivityForResult(i, CommonUtility.REQUEST_MANUAL_BARCODE);

                    }
                });*/
                //;
            }
        });
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "onClick");
                Log.e("nonKippinCardType:", "" + nonKippinCardType);
                Log.e("LOYALTYCARD:", "" + NonKippinCardType.LOYALTYCARD);
                // Log.e("nonKippinCardType:",""+nonKippinCardType);
                if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {

                    updateLoyaltyCard();
                } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {

                    updateGiftCard();
                } else if (shareType == ShareType.DONATEGIFTCARD) {
                    updateGiftCard();
                }

            }
        });
    }


    // funcation to pickImage from Gallery and Camera
    protected void dialogForImage() {
        // TODO Auto-generated method stub

        try {
            lDialog = new Dialog(EditLoyaltyCardActivity.this);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_choose_photo);
            lDialog.show();

            Button buttonGallery = (Button) lDialog.findViewById(R.id.gallery_library);
            buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setActivityTitle("KIPPIN CROP")
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setCropMenuCropButtonTitle("Done")
                                .setRequestedSize(400, 400)
                                .setCropMenuCropButtonIcon(R.drawable.ic_tick)
                                .start(EditLoyaltyCardActivity.this);
                       /* if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(EditLoyaltyCardActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    CommonUtility.MY_PERMISSION_ACCESS_STORAGE);

                        } else {
                            lDialog.dismiss();
                            openGallery();
                        }*/

                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }

                }
            });

            Button buttonCamera = (Button) lDialog.findViewById(R.id.camera);
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setActivityTitle("KIPPIN CROP")
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setCropMenuCropButtonTitle("Done")
                                .setRequestedSize(400, 400)
                                .setCropMenuCropButtonIcon(R.drawable.ic_tick)
                                .start(EditLoyaltyCardActivity.this);

                        //
                       /* if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(EditLoyaltyCardActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    CommonUtility.MY_PERMISSION_ACCESS_CAMERA);
                            // DIALOG CLOSE SO AFTER ACCEPTING USER AGAIN OPEN THE DIALOG
                        } else {
                            lDialog.dismiss();
                            takePicture();
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v("kippin", "Can't create file to take picture!");
                        Toast.makeText(EditLoyaltyCardActivity.this, "Please check SD card!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            Button buttonCancel = (Button) lDialog.findViewById(R.id.Cancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                }
            });
        } catch (Exception ex) {

        }


    }


    public void deleteCard(View v) {
        if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {

            deleteLoyaltyCard();
        } else if (nonKippinCardType == NonKippinCardType.GIFTCARD || shareType == ShareType.DONATEGIFTCARD) {

            deleteGiftCard();
        }

    }

    public void updateLoyaltyCard() {
        try {
           /* Network.With(EditLoyaltyCardActivity.this).getLocationParam(new OnLocationGet() {
                @Override
                public void onLocationGet(double lattitude, double longitude, String mCountry, String city) {*/
            final String mCountry = CommonData.getUserData(this).getCountry();
            HashMap<String, String> jsonBody = new HashMap<String, String>();
            jsonBody.put("Id", getIntent().getStringExtra("LoyaltyCardId"));
            if (NonKippinGiftCardListActivity.kippinGallery == true) {
                jsonBody.put("LogoTemplate", NonKippinGiftCardListActivity.LogoTemplate);
                jsonBody.put("FolderName", txt_folderName.getText().toString());
                /*String profileImage = NonKippinGiftCardListActivity.LogoTemplate.replace(" ", "%20");
                String result = profileImage.replaceAll("[\\~]", "");
                String profileImage1 = result.replaceAll("(?<!(http:|https:))[//]+", "/");
                responseToGetLoyaltyCardLocalModel.setLogoImage(Base64Image(profile_Bitmap));
                logoImageBitmap=*/
            } else {
                jsonBody.put("LogoImage", CommonUtility.encodeTobase64(logoImageBitmap));
                jsonBody.put("FolderName", txt_folderName.getText().toString());
            }
            Log.e("front--------Image:", "" + frontImage);
            Log.e("bImage--------Image:", "" + bImage);
            jsonBody.put("FrontImage", CommonUtility.encodeTobase64(frontImage));
            if (bImage != null) {
                jsonBody.put("BackImage", bImage);
            }

            jsonBody.put("Country", mCountry);
            jsonBody.put("ActualUserId", String.valueOf(CommonData.getUserData(EditLoyaltyCardActivity.this).getId()));
            jsonBody.put("MerchantId", getIntent().getStringExtra("merchantId"));
            jsonBody.put("Barcode", barcode);

            Log.e("jsonBody:", "" + jsonBody);
            LoadingBox.showLoadingDialog(EditLoyaltyCardActivity.this, "Loading...");
            RestClient.getApiServiceForPojo().UpdateLoyaltyCard(jsonBody, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("output ==>", jsonElement.toString());
                    Log.e("URL -->", response.getUrl());
                    Type listType = new TypeToken<List<Merchant>>() {
                    }.getType();
                    Gson gson = new Gson();
                    LoadingBox.dismissLoadingDialog();
                    ResponseModal serverresponse1 = gson.fromJson(jsonElement.toString(), ResponseModal.class);
                    NonKippincard nonKippincard = gson.fromJson(jsonElement.toString(), NonKippincard.class);
                    if (serverresponse1.getResponseCode().equals("1")) {

                        ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCardLocalModel = new ResponseToGetLoyaltyCardLocalModel();
                        if (NonKippinGiftCardListActivity.kippinGallery) {
                            responseToGetLoyaltyCardLocalModel.setBarcode(nonKippincard.getBarcode());
                            responseToGetLoyaltyCardLocalModel.setActualUserId("" + nonKippincard.getActualUserId());
                            responseToGetLoyaltyCardLocalModel.setCountry(nonKippincard.getCountry());
                            responseToGetLoyaltyCardLocalModel.setFolderName(txt_folderName.getText().toString());
                            responseToGetLoyaltyCardLocalModel.setUserId("" + nonKippincard.getUserId());
                            responseToGetLoyaltyCardLocalModel.setMerchantId("" + nonKippincard.getMerchantId());
                            responseToGetLoyaltyCardLocalModel.setId("" + nonKippincard.getId());
                            responseToGetLoyaltyCardLocalModel.setLogoImage(nonKippincard.getLogoImage());
                            responseToGetLoyaltyCardLocalModel.setFrontImage(nonKippincard.getFrontImage());
                            responseToGetLoyaltyCardLocalModel.setBackImage(nonKippincard.getBackImage());
                            responseToGetLoyaltyCardLocalModel.setSenderId("" + nonKippincard.getSenderId());
                            //   responseToGetLoyaltyCardLocalModel.setIsRead(""+nonKippincard.get());


                        } else {
                            responseToGetLoyaltyCardLocalModel.setLogoImage(nonKippincard.getLogoImage());
                            responseToGetLoyaltyCardLocalModel.setFrontImage(nonKippincard.getFrontImage());
                            responseToGetLoyaltyCardLocalModel.setBackImage(nonKippincard.getBackImage());
                            //responseToGetLoyaltyCardLocalModel.setFrontImage(Base64Image(frontImage));
                            // responseToGetLoyaltyCardLocalModel.setBackImage(Base64Image(backImage));
                            //responseToGetLoyaltyCardLocalModel.setLogoImage(Base64Image(logoImageBitmap));
                            responseToGetLoyaltyCardLocalModel.setBarcode(barcode);
                            responseToGetLoyaltyCardLocalModel.setActualUserId("" + nonKippincard.getActualUserId());
                            responseToGetLoyaltyCardLocalModel.setCountry(nonKippincard.getCountry());
                            responseToGetLoyaltyCardLocalModel.setFolderName(txt_folderName.getText().toString());
                            responseToGetLoyaltyCardLocalModel.setUserId("" + nonKippincard.getUserId());
                            responseToGetLoyaltyCardLocalModel.setMerchantId("" + nonKippincard.getMerchantId());
                            responseToGetLoyaltyCardLocalModel.setId("" + nonKippincard.getId());
                            responseToGetLoyaltyCardLocalModel.setSenderId("" + nonKippincard.getSenderId());
                        }


                        new LoyalityConnection().execute(responseToGetLoyaltyCardLocalModel);


                        MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                "Physical card successfully updated", MyLoayaltyCardListActivity.class);

                    } else {
                        NonKippinGiftCardListActivity.kippinGallery = false;
                        MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                serverresponse1.getResponseMessage(), MyLoayaltyCardListActivity.class);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(error.getUrl(), error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showFailureDialog(EditLoyaltyCardActivity.this);

                }
            });
              /*  }


            });*/
        } catch (Exception ex) {

        }

    }

    public void updateGiftCard() {
        try {
            /*Network.With(EditLoyaltyCardActivity.this).getLocationParam(new OnLocationGet() {
                @Override
                public void onLocationGet(double lattitude, double longitude, String mCountry, String city) {*/
            HashMap<String, String> jsonBody = new HashMap<String, String>();
            final String mCountry = CommonData.getUserData(this).getCountry();
            jsonBody.put("Id", getIntent().getStringExtra("LoyaltyCardId"));
            if (NonKippinGiftCardListActivity.kippinGallery) {
                jsonBody.put("LogoTemplate", NonKippinGiftCardListActivity.LogoTemplate);
                jsonBody.put("FolderName", txt_folderName.getText().toString());
            } else {
                jsonBody.put("LogoImage", CommonUtility.encodeTobase64(logoImageBitmap));
                jsonBody.put("FolderName", txt_folderName.getText().toString());
            }
            //  jsonBody.put("LogoImage", CommonUtility.encodeTobase64(logoImageBitmap));
            // jsonBody.put("FolderName", getIntent().getStringExtra("FolderName"));
            jsonBody.put("FrontImage", CommonUtility.encodeTobase64(frontImage));
            jsonBody.put("BackImage", bImage);
            jsonBody.put("Country", mCountry);
            jsonBody.put("ActualUserId", String.valueOf(CommonData.getUserData(EditLoyaltyCardActivity.this).getId()));
            jsonBody.put("MerchantId", getIntent().getStringExtra("merchantId"));
            jsonBody.put("Barcode", barcode);
            LoadingBox.showLoadingDialog(EditLoyaltyCardActivity.this, "Loading...");
            RestClient.getApiServiceForPojo().UpdatePhysicalGiftCard(jsonBody, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("output ==>", jsonElement.toString());
                    Log.e("URL -->", response.getUrl());
                    Type listType = new TypeToken<List<Merchant>>() {
                    }.getType();
                    Gson gson = new Gson();
                    LoadingBox.dismissLoadingDialog();
                    ResponseModal serverresponse1 = gson.fromJson(jsonElement.toString(), ResponseModal.class);
                    NonKippincard nonKippincard = gson.fromJson(jsonElement.toString(), NonKippincard.class);
                    if (serverresponse1.getResponseCode().equals("1")) {
                        ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCardLocalModel = new ResponseToGetLoyaltyCardLocalModel();
                        if (NonKippinGiftCardListActivity.kippinGallery) {
                            responseToGetLoyaltyCardLocalModel.setBarcode(nonKippincard.getBarcode());
                            responseToGetLoyaltyCardLocalModel.setActualUserId("" + nonKippincard.getActualUserId());
                            responseToGetLoyaltyCardLocalModel.setCountry(nonKippincard.getCountry());
                            responseToGetLoyaltyCardLocalModel.setFolderName(txt_folderName.getText().toString());
                            responseToGetLoyaltyCardLocalModel.setUserId("" + nonKippincard.getUserId());
                            responseToGetLoyaltyCardLocalModel.setMerchantId("" + nonKippincard.getMerchantId());
                            responseToGetLoyaltyCardLocalModel.setId("" + nonKippincard.getId());
                            responseToGetLoyaltyCardLocalModel.setLogoImage(nonKippincard.getLogoImage());
                            responseToGetLoyaltyCardLocalModel.setFrontImage(nonKippincard.getFrontImage());
                            responseToGetLoyaltyCardLocalModel.setBackImage(nonKippincard.getBackImage());
                            responseToGetLoyaltyCardLocalModel.setSenderId("" + nonKippincard.getSenderId());
                        } else {
                            responseToGetLoyaltyCardLocalModel.setLogoImage(nonKippincard.getLogoImage());
                            responseToGetLoyaltyCardLocalModel.setFrontImage(nonKippincard.getFrontImage());
                            responseToGetLoyaltyCardLocalModel.setBackImage(nonKippincard.getBackImage());

                            //responseToGetLoyaltyCardLocalModel.setFrontImage(Base64Image(frontImage));
                            //responseToGetLoyaltyCardLocalModel.setBackImage(Base64Image(backImage));
                            //responseToGetLoyaltyCardLocalModel.setLogoImage(Base64Image(logoImageBitmap));
                            responseToGetLoyaltyCardLocalModel.setBarcode(nonKippincard.getBarcode());
                            responseToGetLoyaltyCardLocalModel.setActualUserId("" + nonKippincard.getActualUserId());
                            responseToGetLoyaltyCardLocalModel.setCountry(nonKippincard.getCountry());
                            responseToGetLoyaltyCardLocalModel.setFolderName(txt_folderName.getText().toString());
                            responseToGetLoyaltyCardLocalModel.setUserId("" + nonKippincard.getUserId());
                            responseToGetLoyaltyCardLocalModel.setMerchantId("" + nonKippincard.getMerchantId());
                            responseToGetLoyaltyCardLocalModel.setId("" + nonKippincard.getId());
                            responseToGetLoyaltyCardLocalModel.setSenderId("" + nonKippincard.getSenderId());
                        }
                        new GiftConnection().execute(responseToGetLoyaltyCardLocalModel);

                        if (shareType == ShareType.DONATEGIFTCARD) {
                            MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                    serverresponse1.getResponseMessage(), NonKippinGiftCardListActivity.class, shareType);

                        } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                            MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                    serverresponse1.getResponseMessage(), GiftCardListActivity.class);
                        }

                    } else {

                        if (shareType == ShareType.DONATEGIFTCARD) {
                            MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                    serverresponse1.getResponseMessage(), NonKippinGiftCardListActivity.class);

                        } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                            MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                    serverresponse1.getResponseMessage(), GiftCardListActivity.class);
                        } else {
                            MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                    "Physical card successfully updated", GiftCardListActivity.class);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(error.getUrl(), error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showFailureDialog(EditLoyaltyCardActivity.this);
                }
            });

        } catch (Exception ex) {

        }
    }

    class GiftConnection extends AsyncTask<ResponseToGetLoyaltyCardLocalModel, Void, ResponseToGetLoyaltyCardLocalModel> {
        @Override
        protected ResponseToGetLoyaltyCardLocalModel doInBackground(ResponseToGetLoyaltyCardLocalModel... nonKippincards) {
            ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCardLocalModel = new ResponseToGetLoyaltyCardLocalModel();
            try {
                ResponseToGetLoyaltyCardLocalModel nonKippincard = nonKippincards[0];
                if (NonKippinGiftCardListActivity.kippinGallery) {
                    String profileImage = nonKippincard.getLogoImage().replace(" ", "%20");
                    String result = profileImage.replaceAll("[\\~]", "");
                    String profileImage1 = result.replaceAll("(?<!(http:|https:))[//]+", "/");
                    Bitmap profile_Bitmap = BitmapFactory.decodeStream((InputStream) new URL(profileImage1).getContent());


                    String frontImage = nonKippincard.getFrontImage().replace(" ", "%20");
                    String result1 = frontImage.replaceAll("[\\~]", "");
                    String frontImage1 = result1.replaceAll("(?<!(http:|https:))[//]+", "/");
                    Bitmap front_Image = BitmapFactory.decodeStream((InputStream) new URL(frontImage1).getContent());

                    String backImage = nonKippincard.getBackImage().replace(" ", "%20");
                    String result2 = backImage.replaceAll("[\\~]", "");
                    String backImage1 = result2.replaceAll("(?<!(http:|https:))[//]+", "/");

                    Bitmap back_Image = BitmapFactory.decodeStream((InputStream) new URL(backImage1).getContent());
                    responseToGetLoyaltyCardLocalModel.setFrontImage(Base64Image(front_Image));
                    responseToGetLoyaltyCardLocalModel.setBackImage(Base64Image(back_Image));
                    responseToGetLoyaltyCardLocalModel.setLogoImage(Base64Image(profile_Bitmap));

                    responseToGetLoyaltyCardLocalModel.setBarcode(nonKippincard.getBarcode());
                    responseToGetLoyaltyCardLocalModel.setActualUserId(String.valueOf(CommonData.getUserData(EditLoyaltyCardActivity.this).getId()));
                    responseToGetLoyaltyCardLocalModel.setCountry(nonKippincard.getCountry());
                    responseToGetLoyaltyCardLocalModel.setFolderName(txt_folderName.getText().toString());
                    responseToGetLoyaltyCardLocalModel.setUserId("" + nonKippincard.getUserId());
                    responseToGetLoyaltyCardLocalModel.setMerchantId(getIntent().getStringExtra("merchantId"));
                    responseToGetLoyaltyCardLocalModel.setId(getIntent().getStringExtra("LoyaltyCardId"));
                    responseToGetLoyaltyCardLocalModel.setSenderId("" + nonKippincard.getSenderId());
                } else {
                    String profileImage = nonKippincard.getLogoImage().replace(" ", "%20");
                    String result = profileImage.replaceAll("[\\~]", "");
                    String profileImage1 = result.replaceAll("(?<!(http:|https:))[//]+", "/");
                    Bitmap profile_Bitmap = BitmapFactory.decodeStream((InputStream) new URL(profileImage1).getContent());


                    String frontImage = nonKippincard.getFrontImage().replace(" ", "%20");
                    String result1 = frontImage.replaceAll("[\\~]", "");
                    String frontImage1 = result1.replaceAll("(?<!(http:|https:))[//]+", "/");
                    Bitmap front_Image = BitmapFactory.decodeStream((InputStream) new URL(frontImage1).getContent());

                    String backImage = nonKippincard.getBackImage().replace(" ", "%20");
                    String result2 = backImage.replaceAll("[\\~]", "");
                    String backImage1 = result2.replaceAll("(?<!(http:|https:))[//]+", "/");

                    Bitmap back_Image = BitmapFactory.decodeStream((InputStream) new URL(backImage1).getContent());
                    responseToGetLoyaltyCardLocalModel.setFrontImage(Base64Image(front_Image));
                    responseToGetLoyaltyCardLocalModel.setBackImage(Base64Image(back_Image));
                    responseToGetLoyaltyCardLocalModel.setLogoImage(Base64Image(profile_Bitmap));

                    responseToGetLoyaltyCardLocalModel.setBarcode(barcode);
                    responseToGetLoyaltyCardLocalModel.setActualUserId(String.valueOf(CommonData.getUserData(EditLoyaltyCardActivity.this).getId()));
                    responseToGetLoyaltyCardLocalModel.setCountry(nonKippincard.getCountry());
                    responseToGetLoyaltyCardLocalModel.setFolderName(txt_folderName.getText().toString());
                    responseToGetLoyaltyCardLocalModel.setUserId("" + nonKippincard.getUserId());
                    responseToGetLoyaltyCardLocalModel.setMerchantId(getIntent().getStringExtra("merchantId"));
                    responseToGetLoyaltyCardLocalModel.setId(getIntent().getStringExtra("LoyaltyCardId"));
                    responseToGetLoyaltyCardLocalModel.setSenderId("" + nonKippincard.getSenderId());
                }


                DbNew.getInstance(EditLoyaltyCardActivity.this).updateNonKippinGiftCard(responseToGetLoyaltyCardLocalModel.getId(),
                        new Gson().toJson(responseToGetLoyaltyCardLocalModel));

            } catch (Exception e) {
                e.printStackTrace();
            }

            NonKippinGiftCardListActivity.kippinGallery = false;


            return null;
        }

        @Override
        protected void onPostExecute(ResponseToGetLoyaltyCardLocalModel nonKippincard) {
            super.onPostExecute(nonKippincard);

        }
    }

    class LoyalityConnection extends AsyncTask<ResponseToGetLoyaltyCardLocalModel, Void, ResponseToGetLoyaltyCardLocalModel> {
        @Override
        protected ResponseToGetLoyaltyCardLocalModel doInBackground(ResponseToGetLoyaltyCardLocalModel... nonKippincards) {
            ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCardLocalModel = new ResponseToGetLoyaltyCardLocalModel();
            try {
                ResponseToGetLoyaltyCardLocalModel nonKippincard = nonKippincards[0];
                if (NonKippinGiftCardListActivity.kippinGallery) {
                    String profileImage = nonKippincard.getLogoImage().replace(" ", "%20");
                    String result = profileImage.replaceAll("[\\~]", "");
                    String profileImage1 = result.replaceAll("(?<!(http:|https:))[//]+", "/");
                    Bitmap profile_Bitmap = BitmapFactory.decodeStream((InputStream) new URL(profileImage1).getContent());


                    String frontImage = nonKippincard.getFrontImage().replace(" ", "%20");
                    String result1 = frontImage.replaceAll("[\\~]", "");
                    String frontImage1 = result1.replaceAll("(?<!(http:|https:))[//]+", "/");
                    Bitmap front_Image = BitmapFactory.decodeStream((InputStream) new URL(frontImage1).getContent());

                    String backImage = nonKippincard.getBackImage().replace(" ", "%20");
                    String result2 = backImage.replaceAll("[\\~]", "");
                    String backImage1 = result2.replaceAll("(?<!(http:|https:))[//]+", "/");

                    Bitmap back_Image = BitmapFactory.decodeStream((InputStream) new URL(backImage1).getContent());

                    responseToGetLoyaltyCardLocalModel.setFrontImage(Base64Image(front_Image));
                    responseToGetLoyaltyCardLocalModel.setBackImage(Base64Image(back_Image));
                    responseToGetLoyaltyCardLocalModel.setLogoImage(Base64Image(profile_Bitmap));
                    responseToGetLoyaltyCardLocalModel.setBarcode(nonKippincard.getBarcode());
                    responseToGetLoyaltyCardLocalModel.setActualUserId(String.valueOf(CommonData.getUserData(EditLoyaltyCardActivity.this).getId()));
                    responseToGetLoyaltyCardLocalModel.setCountry(nonKippincard.getCountry());
                    responseToGetLoyaltyCardLocalModel.setFolderName(txt_folderName.getText().toString());
                    responseToGetLoyaltyCardLocalModel.setUserId("" + nonKippincard.getUserId());
                    responseToGetLoyaltyCardLocalModel.setMerchantId(getIntent().getStringExtra("merchantId"));
                    responseToGetLoyaltyCardLocalModel.setId(getIntent().getStringExtra("LoyaltyCardId"));
                    responseToGetLoyaltyCardLocalModel.setSenderId("" + nonKippincard.getSenderId());
                } else {


                    String profileImage = nonKippincard.getLogoImage().replace(" ", "%20");
                    String result = profileImage.replaceAll("[\\~]", "");
                    String profileImage1 = result.replaceAll("(?<!(http:|https:))[//]+", "/");
                    Bitmap profile_Bitmap = BitmapFactory.decodeStream((InputStream) new URL(profileImage1).getContent());


                    String frontImage = nonKippincard.getFrontImage().replace(" ", "%20");
                    String result1 = frontImage.replaceAll("[\\~]", "");
                    String frontImage1 = result1.replaceAll("(?<!(http:|https:))[//]+", "/");
                    Bitmap front_Image = BitmapFactory.decodeStream((InputStream) new URL(frontImage1).getContent());

                    String backImage = nonKippincard.getBackImage().replace(" ", "%20");
                    String result2 = backImage.replaceAll("[\\~]", "");
                    String backImage1 = result2.replaceAll("(?<!(http:|https:))[//]+", "/");

                    Bitmap back_Image = BitmapFactory.decodeStream((InputStream) new URL(backImage1).getContent());

                    responseToGetLoyaltyCardLocalModel.setFrontImage(Base64Image(front_Image));
                    responseToGetLoyaltyCardLocalModel.setBackImage(Base64Image(back_Image));
                    responseToGetLoyaltyCardLocalModel.setLogoImage(Base64Image(profile_Bitmap));

                    responseToGetLoyaltyCardLocalModel.setBarcode(barcode);
                    responseToGetLoyaltyCardLocalModel.setActualUserId(String.valueOf(CommonData.getUserData(EditLoyaltyCardActivity.this).getId()));
                    responseToGetLoyaltyCardLocalModel.setCountry(nonKippincard.getCountry());
                    responseToGetLoyaltyCardLocalModel.setFolderName(txt_folderName.getText().toString());
                    responseToGetLoyaltyCardLocalModel.setUserId("" + nonKippincard.getUserId());
                    responseToGetLoyaltyCardLocalModel.setMerchantId(getIntent().getStringExtra("merchantId"));
                    responseToGetLoyaltyCardLocalModel.setId(getIntent().getStringExtra("LoyaltyCardId"));
                    responseToGetLoyaltyCardLocalModel.setSenderId("" + nonKippincard.getSenderId());
                }


                DbNew.getInstance(EditLoyaltyCardActivity.this).updateNonKippinLoyalityCard(responseToGetLoyaltyCardLocalModel.getId(),
                        new Gson().toJson(responseToGetLoyaltyCardLocalModel));

            } catch (Exception e) {
                e.printStackTrace();
            }

            NonKippinGiftCardListActivity.kippinGallery = false;


            return null;
        }

        @Override
        protected void onPostExecute(ResponseToGetLoyaltyCardLocalModel nonKippincard) {
            super.onPostExecute(nonKippincard);

        }
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
                    Log.e("LoyaltyCardId:", "" + getIntent().getStringExtra("LoyaltyCardId"));
                    Log.e("FolderName:", "" + txt_folderName.getText().toString());
                    Log.e("ActualUserId:", "" + String.valueOf(CommonData.getUserData(EditLoyaltyCardActivity.this).getId()));

                    jsonBody.put("Id", getIntent().getStringExtra("LoyaltyCardId"));
                    //     jsonBody.put("LogoImage", CommonUtility.encodeTobase64(iv_logoImage_drawable.getBitmap()));
                    jsonBody.put("FolderName", txt_folderName.getText().toString());
                    //    jsonBody.put("FrontImage", CommonUtility.encodeTobase64(frontImage));
                    //    jsonBody.put("BackImage", CommonUtility.encodeTobase64(backImage));
                    //    jsonBody.put("Country", mCountry);
                    jsonBody.put("ActualUserId", String.valueOf(CommonData.getUserData(EditLoyaltyCardActivity.this).getId()));
                    jsonBody.put("MerchantId", "0");
                    //      jsonBody.put("Barcode", barcode);

                    LoadingBox.showLoadingDialog(EditLoyaltyCardActivity.this, "Loading...");
                    RestClient.getApiServiceForPojo().DeleteLoyaltyCard(jsonBody, new Callback<JsonElement>() {
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
                                DbNew.getInstance(EditLoyaltyCardActivity.this).deleteParticularNonLoyalitycard(getIntent().getStringExtra("LoyaltyCardId"));

                                MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                        "Physical card successfully Deleted", MyLoayaltyCardListActivity.class);
                                /*CommonUtility.moveToTarget(EditLoyaltyCardActivity.this, MyLoayaltyCardListActivity.class);*/
                            } else {
                                MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                        "Some error in deleting card.Please try again later", false);
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(error.getUrl(), error.getMessage());
                            LoadingBox.dismissLoadingDialog();
                            MessageDialog.showFailureDialog(EditLoyaltyCardActivity.this);
                        }
                    });
                      /*  }


                    });*/
                }
            });
        } catch (Exception ex) {

        }

    }

    public void deleteGiftCard() {
        try {
            MessageDialog.showVerificationDialog(this, "Are you sure you want to delete this card?", new DialogListener() {
                @Override
                public void handleYesButton() {
                    Network.With(EditLoyaltyCardActivity.this).getLocationParam(new OnLocationGet() {
                        @Override
                        public void onLocationGet(double lattitude, double longitude, String mCountry, String city) {
                            HashMap<String, String> jsonBody = new HashMap<String, String>();


                            jsonBody.put("Id", getIntent().getStringExtra("LoyaltyCardId"));
                            //  jsonBody.put("LogoImage", CommonUtility.encodeTobase64(iv_logoImage_drawable.getBitmap()));
                            jsonBody.put("FolderName", txt_folderName.getText().toString());
                            //   jsonBody.put("FrontImage", CommonUtility.encodeTobase64(frontImage));
                            //   jsonBody.put("BackImage", CommonUtility.encodeTobase64(backImage));
                            //   jsonBody.put("Country", mCountry);
                            jsonBody.put("ActualUserId", String.valueOf(CommonData.getUserData(EditLoyaltyCardActivity.this).getId()));
                            jsonBody.put("MerchantId", "0");
                            //   jsonBody.put("Barcode", barcode);
                            LoadingBox.showLoadingDialog(EditLoyaltyCardActivity.this, "Loading...");
                            RestClient.getApiServiceForPojo().DeletePhysicalGiftCard(jsonBody, new Callback<JsonElement>() {
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
                                            DbNew.getInstance(EditLoyaltyCardActivity.this).deleteParticularNonKippinGiftcard(getIntent().getStringExtra("LoyaltyCardId"));

                                            MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                                    "Physical card successfully Deleted", GiftCardListActivity.class, shareType);

                                        } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                                            DbNew.getInstance(EditLoyaltyCardActivity.this).deleteParticularNonKippinGiftcard(getIntent().getStringExtra("LoyaltyCardId"));
                                            MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                                    "Physical card successfully Deleted", GiftCardListActivity.class);
                                        }

                                    } else {
                                        MessageDialog.showDialog(EditLoyaltyCardActivity.this,
                                                "Some error in deleting card.Please try again later", false);
                                        CommonUtility.moveToTarget(EditLoyaltyCardActivity.this, GiftCardListActivity.class);
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.e(error.getUrl(), error.getMessage());
                                    LoadingBox.dismissLoadingDialog();
                                    MessageDialog.showFailureDialog(EditLoyaltyCardActivity.this);
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
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                try {

                    String imgPath = result.getUri().getPath();
                    //Uri resultUri = UCrop.getOutput(data);
                    Log.e("is_iv_cardIcon_cliked:", "" + is_iv_cardIcon_cliked);
                    if (is_iv_cardIcon_cliked) {
                        Bitmap bmp = CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 100),
                                CommonUtility.dpToPx(this, 100));
                        iv_logoImage.setImageBitmap(bmp);
                        logoImageBitmap = bmp;
                    } else {
                        //CommonUtility.getWidthOfScreen(this)-80
                        Bitmap bmp = CommonUtility.decodeSampledBitmapFromResource(imgPath, 160,
                                CommonUtility.dpToPx(this, 160));
                        iv_frontImage.setImageBitmap(bmp);

                        frontImage = bmp;
                        Log.e("front--------Image:", "" + frontImage);

                    }
                    File actualImafe = new File(CommonUtility.getRealPathFromURI(this, Uri.parse(imagePath)));
                    File rotatedImage = new File(CommonUtility.getRealPathFromURI(this, u1));
                    File cropedImage = new File(imgPath);
                    if (actualImafe.exists()) {
                        actualImafe.delete();
                    }
                    if (rotatedImage.exists()) {
                        rotatedImage.delete();
                    }
                    if (cropedImage.exists()) {
                        cropedImage.delete();
                    }
                } catch (Exception e) {

                }
                // ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
              /*  Toast.makeText(
                        this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG)
                        .show();*/
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }

        if (resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_SCANNER:
                        android.util.Log.e("----CONTENT:----:", "" + data.getStringExtra(Config.CONTENT));
                        android.util.Log.e("---FORMAT:-------", "" + data.getStringExtra(Config.FORMAT));
                        if (data != null) {
                            String cardNo = data.getStringExtra(Config.CONTENT);
                            String card_No = data.getStringExtra(Config.FORMAT);
                            android.util.Log.e("----cardNo----:", "" + cardNo);
                            android.util.Log.e("---card_No:-------", "" + card_No);
                            //ed_barcodeNumber.setText(cardNo);
                            barcode = cardNo;
                            txt_barcode.setText(cardNo);
                            BitmapDrawable d = (BitmapDrawable) barcodeUtil.generateBarcode(cardNo);
                            backImage = d.getBitmap();
                            try {
                                bImage = CommonUtility.encodeTobase64(backImage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            Config.FORMAT = "";
                            Config.CONTENT = "";
                        }
                        break;
                }

            }
            if (CROP_IMAGE == 0) {
                switch (requestCode) {
                    case REQUEST_CODE_GALLERY:
                        try {
                            String imagePath = data.getStringExtra("imagePath");
                            Bitmap bm = BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath());
                            Uri u = CommonUtility.getImageUri(EditLoyaltyCardActivity.this, bm);
                            UCrop.Options options = new UCrop.Options();
                            options.setToolbarColor(Color.parseColor("#ffffff"));
                            options.setToolbarTitle("Merchants List");
                            options.setToolbarWidgetColor(Color.parseColor("#000000"));
                            UCrop.of(u, Uri.parse(imagePath))
                                    .withOptions(options)
                                    .withAspectRatio(16, 9)
                                    .withMaxResultSize(CommonUtility.getWidthOfScreen(EditLoyaltyCardActivity.this), CommonUtility.getHeightOfScreen(EditLoyaltyCardActivity.this))
                                    .start(EditLoyaltyCardActivity.this);

                        } catch (Exception e) {
                            Log.e(TAG, "Error while creating temp file" + e);
                        }
                        break;
                    case REQUEST_CODE_TAKE_PICTURE:

                        try {
                            getContentResolver().notifyChange(mImageUri, null);
                            imagePath = new File(mImageUri.getPath()).getAbsolutePath();

                            Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                            ExifInterface exifInterface = new ExifInterface(Uri.parse(imagePath).getPath());
                            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);
                            float angle = 360;
                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    angle = 90;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    angle = 180;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    angle = 270;
                                    break;
                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    break;
                            }
                            Bitmap oriented = CommonUtility.fixOrientation(bm, angle);
                            u1 = CommonUtility.getImageUri(EditLoyaltyCardActivity.this, oriented);
                            UCrop.Options options = new UCrop.Options();
                            options.setToolbarColor(Color.parseColor("#ffffff"));
                            options.setToolbarTitle("Merchants List");
                            options.setToolbarWidgetColor(Color.parseColor("#000000"));
                            UCrop.of(u1, mImageUri)
                                    .withOptions(options)
                                    .withAspectRatio(16, 9)
                                    .withMaxResultSize(CommonUtility.getWidthOfScreen(EditLoyaltyCardActivity.this), CommonUtility.getHeightOfScreen(EditLoyaltyCardActivity.this))
                                    .start(EditLoyaltyCardActivity.this);

                        } catch (Exception ex) {
                            Log.e("", ex.getMessage());
                        }

                        break;
                    case UCrop.REQUEST_CROP:
                        CROP_IMAGE = 111;
                        try {

                            Uri resultUri = UCrop.getOutput(data);
                            Log.e("is_iv_cardIcon_cliked:", "" + is_iv_cardIcon_cliked);
                            if (is_iv_cardIcon_cliked) {
                                Bitmap bmp = CommonUtility.decodeSampledBitmapFromResource(resultUri.getPath(), CommonUtility.dpToPx(this, 100),
                                        CommonUtility.dpToPx(this, 100));
                                iv_logoImage.setImageBitmap(bmp);
                                logoImageBitmap = bmp;
                            } else {
                                //CommonUtility.getWidthOfScreen(this)-80
                                Bitmap bmp = CommonUtility.decodeSampledBitmapFromResource(resultUri.getPath(), 160,
                                        CommonUtility.dpToPx(this, 160));
                                iv_frontImage.setImageBitmap(bmp);

                                frontImage = bmp;
                                Log.e("front--------Image:", "" + frontImage);

                            }
                            File actualImafe = new File(CommonUtility.getRealPathFromURI(this, Uri.parse(imagePath)));
                            File rotatedImage = new File(CommonUtility.getRealPathFromURI(this, u1));
                            File cropedImage = new File(resultUri.getPath());
                            if (actualImafe.exists()) {
                                actualImafe.delete();
                            }
                            if (rotatedImage.exists()) {
                                rotatedImage.delete();
                            }
                            if (cropedImage.exists()) {
                                cropedImage.delete();
                            }
                            break;
                        } catch (Exception ex) {

                        }
                }
            } else {

                if (requestCode == CommonUtility.REQUEST_CODE_TAKEMANUAL_INPUT) {

                    if (data != null) {
                        String cardNo = data.getStringExtra("cardNo");
                        barcode = cardNo;
                        txt_barcode.setText(cardNo);
                        BitmapDrawable d = (BitmapDrawable) barcodeUtil.generateBarcode(cardNo);
                        backImage = d.getBitmap();
                        try {
                            bImage = CommonUtility.encodeTobase64(backImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } // IF CLOSE FOR MANUAL INPUT
                else {
                    barcodeUtil.onActivityResult(requestCode, resultCode, data, new OnBarcodeGetListener() {
                        @Override
                        public void getBarcode(int req, String s) {
                            if (resultCode == RESULT_OK) {
                                switch (req) {

                                    case CommonUtility.REQUEST_CODE_BARCODE:
                                        barcode = s;
                                        txt_barcode.setText(barcode);
                                        BitmapDrawable d = (BitmapDrawable) barcodeUtil.generateBarcode(s);
                                        backImage = d.getBitmap();
                                        try {
                                            bImage = CommonUtility.encodeTobase64(backImage);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                }

                            }
                        }
                    });

                }

            }

        }// IF CLOSE FOR RESULT OK
        else if (resultCode == RESULT_CANCELED) {
            if (data != null) {
                if (data.getStringExtra("clickedButton") != null) {


                    if (data.getStringExtra("clickedButton").equals("manualInput")) {
                        Intent i = new Intent();
                        i.setClass(EditLoyaltyCardActivity.this, ActivityINputManualBarcode.class);
                        startActivityForResult(i, CommonUtility.REQUEST_CODE_TAKEMANUAL_INPUT);
                        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_from_right);
                    }
                }
            }

        }

    }


    private void takePicture() {
        isCamerClicked = true;
        isGalleryClicked = false;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = null;
        try {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        com.kippin.utils.Utility.MY_PERMISSION_ACCESS_CAMERA);

            } else {
                // place where to store camera taken picture
                if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            com.kippin.utils.Utility.MY_PERMISSION_ACCESS_CAMERA);
                    // DIALOG CLOSE SO AFTER ACCEPTING USER AGAIN OPEN THE DIALOG
                } else {
                    photo = CommonUtility.createTemporaryFile("picture", ".jpg");
                    photo.delete();
                    mImageUri = Uri.fromFile(photo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGallery() {
        isGalleryClicked = true;
        isCamerClicked = false;

        try {
            Intent photoPickerIntent = new Intent(this, GalleryActivity.class);
            startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
        } catch (Exception ex) {

        }
    }


    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CommonUtility.MY_PERMISSION_ACCESS_CAMERA:
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(EditLoyaltyCardActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditLoyaltyCardActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                com.kippin.utils.Utility.MY_PERMISSION_ACCESS_STORAGE);


                    } else {
                        takePicture();
                        lDialog.dismiss();
                    }

                } else {
                    Toast.makeText(EditLoyaltyCardActivity.this, "This is a required for the app to function", Toast.LENGTH_LONG).show();
                    lDialog.dismiss();

                }
                break;
            case CommonUtility.MY_PERMISSION_ACCESS_STORAGE:
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isCamerClicked) {
                        takePicture();
                        isCamerClicked = false;
                        lDialog.dismiss();
                    } else if (isGalleryClicked) {
                        openGallery();
                        isGalleryClicked = false;
                        lDialog.dismiss();
                    }


                } else {
                    Toast.makeText(EditLoyaltyCardActivity.this, "This is a required for the app to function", Toast.LENGTH_LONG).show();
                    lDialog.dismiss();
                }
                break;
        }
    }

    protected void dialogForGalleryImage() {
        // TODO Auto-generated method stub

        try {
            lDialog = new Dialog(EditLoyaltyCardActivity.this);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_new_choose_photo);
            lDialog.show();

            Button buttonGallery = (Button) lDialog.findViewById(R.id.gallery_library);
            Button kippin_gallery_library = (Button) lDialog.findViewById(R.id.kippin_gallery_library);

            kippin_gallery_library.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        NonKippinGiftCardListActivity.kippinGallery = false;
                        lDialog.dismiss();
                        Intent intent = new Intent(EditLoyaltyCardActivity.this, KippinCloudGalleryActivity.class);
                        startActivity(intent);

                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }
                }
            });


            buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(EditLoyaltyCardActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    CommonUtility.MY_PERMISSION_ACCESS_STORAGE);

                        } else {
                            lDialog.dismiss();
                            openGallery();
                        }

                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }
                }
            });

            Button buttonCamera = (Button) lDialog.findViewById(R.id.camera);
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(EditLoyaltyCardActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    CommonUtility.MY_PERMISSION_ACCESS_CAMERA);
                            // DIALOG CLOSE SO AFTER ACCEPTING USER AGAIN OPEN THE DIALOG
                        } else {
                            lDialog.dismiss();
                            takePicture();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.v("kippin", "Can't create file to take picture!");
                        Toast.makeText(EditLoyaltyCardActivity.this, "Please check SD card!", Toast.LENGTH_SHORT).show();
                    }


                }
            });
            Button buttonCancel = (Button) lDialog.findViewById(R.id.Cancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                }
            });
        } catch (Exception ex) {

        }


    }

}



