package com.kippinretail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kippinretail.Adapter.NonKippinGiftCardListAdapter;
import com.kippinretail.Adapter.NonKippinGiftCardListAdapter1;
import com.kippinretail.Adapter.NonKippinGiftCardListAdapter1_DB;
import com.kippinretail.Adapter.NonKippinGiftCardListAdapterLocal_DB;
import com.kippinretail.ApplicationuUlity.AppStatus;
import com.kippinretail.ApplicationuUlity.BarcodeUtil;
import com.kippinretail.ApplicationuUlity.CheckBoxType;
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
import com.kippinretail.Modal.ResponseToGetLoyaltyCardModel;
import com.kippinretail.Modal.ServerResponseToGetLoyaltyCard;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.callbacks.CheckBoxClickHandler;
import com.kippinretail.callbacks.NotificationREveiver;
import com.kippinretail.config.Config;
import com.kippinretail.interfaces.OnLocationGet;
import com.kippinretail.kippingallerypreview.KippinCloudGalleryActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.kippinretail.scanning.ScannerActivity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import notification.NotificationHandler;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.kippinretail.ApplicationuUlity.CommonUtility.cropedBitmap;

public class NonKippinGiftCardListActivity extends SuperActivity implements View.OnClickListener, AdapterView.OnItemClickListener, NotificationREveiver {
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final String TAG = "NONKIPPINGIFTCARD";
    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    private final static int REQUEST_SCANNER = 1;
    public static boolean kippinGallery = false;
    public static String LogoTemplate = "";
    public static String CardName = "";
    private static int CROP_IMAGE = 111;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    int PERMISSION_ALL = 1;
    EditText txtSearch;
    ArrayList<ResponseToGetLoyaltyCardLocalModel> responseToGetLoyaltyCardModels;
    ListView list_data;
    LinearLayout layout_nonKippin;
    RelativeLayout layout_container_search, layout_dialog, lalayout_ivBack, layout_ivHome, layout_txtMercahntName;
    Button btn_upload;
    TextView tvTopbarTitle, btnFrontImage, btnbackImage, btnUploadImage;
    boolean is_iv_cardIcon_cliked, is_btnFrontImage_cliked, is_iv_btnbackImage_cliked;
    Bitmap fromImage, backImage;
    BarcodeUtil barcodeUtil;
    int index;
    HashMap<Integer, String> data_shareGiftCard;
    ImageView iv_star;
    // UI Used To Show
    // 1) LIST OF NON KIPPIN  Gift CARDS
    // 2) LIST OF NON KIPPIN LOYALTY CARD
    // 3) WE CAN SEND GIFT CARD TO FRIEND
    // 4) WE CAN SEND GIFT CARD TO CHARITY
    boolean isLocal = false;
    boolean isuploadLoyaltyCardClicked = false;
    boolean isUploadGiftcard = false;
    int downloadedRows = 0;
    Bitmap frontBitmap, backBitmap, profileBitmap;
    NonKippinGiftCardListAdapterLocal_DB nonKippinGiftCardListAdapterLocal_db;
    List<ResponseToGetLoyaltyCardLocalModel> responseToGetLoyaltyCardLocalModels;
    ArrayList<ResponseToGetLoyaltyCardLocalModel> getResponseToGetLoyaltyCardLocalModels;
    private CircleImageView iv_cardIcon = null;
    private EditText txtcardName;
    private List<ResponseToGetLoyaltyCard> serverresponse;
    private List<ResponseToGetLoyaltyCardModel> temp;
    private Bitmap photo;
    private NonKippinCardType nonKippinCardType;
    private String friendId = null;
    private String barcode;
    private ShareType shareType;
    private NonKippinGiftCardListAdapter1 nonKippinListAdapter;
    private NonKippinGiftCardListAdapter1_DB nonKippinListAdapter_db;
    private NonKippinGiftCardListAdapter1 nonKippinGiftCardListAddapter;
    private File mFileTemp;
    private NonKippinGiftCardListAdapter adapterforListToshare;
    private Dialog lDialog;
    // ============================ OFFLINE CACHE ===============================
    private Uri imageUri;
    private String imagePath;
    private Uri u1;
    private Uri mImageUri;
    private boolean isCamerClicked, isGalleryClicked;
    // MAKE LIST OF NON KIPPIN GIFT CARD TO FRIEND
    private boolean isListTosendGiftcard = false;
    private List<ResponseToGetLoyaltyCard> serverresponse_db = null;
    //==========================================OFFLINE CACHE ===========================================================================
    private List<ResponseToGetLoyaltyCard> listCards;

    // SINGLE SELECTION CODE
//    new OnSelectionChange() {
//        @Override
//        public void onSelectionChanged(int position, boolean isChecked) {
//            if (isChecked) {
//                index = position;
//            } else {
//
//            }
//        }
//    }));

    // SHARE NON KIPPIN GIFT CARD TO FRIEND

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    // DONATE NON KIPPIN GIFT CARD TO CHARITY

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //  SEE NON KIPPIN LOYALTY AND GIFT CARD AND ALSO UPLOAD
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonkippin_uploadcard);
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        responseToGetLoyaltyCardModels = new ArrayList<>();
        getResponseToGetLoyaltyCardLocalModels = new ArrayList<>();
        responseToGetLoyaltyCardModels.clear();
        getResponseToGetLoyaltyCardLocalModels.clear();
        initialiseUI();
        updateToolBar();
        updateUI();
        addListener();
        kippinGallery = false;

        System.gc();
    }

    @Override
    public void initialiseUI() {
        super.initialiseUI();
        temp = new ArrayList<ResponseToGetLoyaltyCardModel>();
        data_shareGiftCard = new HashMap<Integer, String>();
        barcodeUtil = new BarcodeUtil(this);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        list_data = (ListView) findViewById(R.id.list_data);
        layout_nonKippin = (LinearLayout) findViewById(R.id.layout_nonKippin);
        iv_star = (ImageView) findViewById(R.id.iv_star);
        layout_txtMercahntName = (RelativeLayout) findViewById(R.id.layout_txtMercahntName);
        layout_container_search = (RelativeLayout) findViewById(R.id.layout_container_search);
        lalayout_ivBack = (RelativeLayout) findViewById(R.id.lalayout_ivBack);
        layout_ivHome = (RelativeLayout) findViewById(R.id.layout_ivHome);
        layout_dialog = (RelativeLayout) findViewById(R.id.layout_dialog);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        tvTopbarTitle = (TextView) findViewById(R.id.tvTopbarTitle);
        iv_cardIcon = (CircleImageView) findViewById(R.id.iv_cardIcon);
        btnFrontImage = (TextView) findViewById(R.id.btnFrontImage);
        btnbackImage = (TextView) findViewById(R.id.btnbackImage);
        btnUploadImage = (TextView) findViewById(R.id.btnUploadImage);
        txtcardName = (EditText) findViewById(R.id.txtcardName);

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(),
                    TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }

    }

    public void updateToolBar() {
        //  generateActionBar(R.string.title_user_NonKippinGiftCardListActivity, true, true, false);

    }

    public void updateUI() {
        layout_nonKippin.setVisibility(View.GONE);
        btn_upload.setVisibility(View.VISIBLE);
        //btn_upload.setText("Upload Physical Card");
        btn_upload.setBackground(null);
        btn_upload.setBackgroundResource(R.drawable.x_upload_physical_card_red);
        nonKippinCardType = (NonKippinCardType) getIntent().getSerializableExtra("NonKippinCardType");
        shareType = (ShareType) getIntent().getSerializableExtra("shareType");
        if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
            tvTopbarTitle.setText("Non Kippin Loyalty Card");
            // This boolean is for List click listener and pass data accordingly
            // if (DbNew.getInstance(NonKippinGiftCardListActivity.this).getrows_NonKippin() == 0) {
            if (isNetworkAvailable()) {
                isLocal = false;
                new LoadingNonKippinCardsFromLocalDataBase().execute(nonKippinCardType);

            } else {
                // Log.e("Card-Data", " == " + DbNew.getInstance(NonKippinGiftCardListActivity.this).getNonKippinData());
                isLocal = true;
                new LoadingNonKippinCardsFromLocalDataBase().execute(nonKippinCardType);
                //new LoadingNonKippinCardsFromDataBase().execute(nonKippinCardType);
                isListTosendGiftcard = false;
            }
        } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
            tvTopbarTitle.setText("Non Kippin Gift Card");
            if (isNetworkAvailable()) {
                new LoadingNonKippinCardsFromLocalDataBase().execute(nonKippinCardType);

            } else {
                Log.e("Card-Data", " == " + DbNew.getInstance(NonKippinGiftCardListActivity.this).getNonGiftCard());
                new LoadingNonKippinCardsFromLocalDataBase().execute(nonKippinCardType);
                // new LoadingNonKippinCardsFromDataBase().execute(nonKippinCardType);
                isListTosendGiftcard = false;
            }
        } else if (shareType == ShareType.DONATEGIFTCARD) {
            friendId = getIntent().getStringExtra("friendId");
            btn_upload.setBackgroundResource(0);
            tvTopbarTitle.setText("Non Kippin Gift Card");
            btn_upload.setBackgroundResource(R.drawable.x_red_button);
            btn_upload.setText("Send Gift Card");
            makeListOfNonKippinGiftCardToSend(shareType);    // Data Is Similar But Adapter is Different
        } else if (shareType == ShareType.SHAREGIFTCARD) {
            friendId = getIntent().getStringExtra("friendId");
            tvTopbarTitle.setText("Non Kippin Gift Card");
            btn_upload.setBackgroundResource(0);
            btn_upload.setBackgroundResource(R.drawable.x_red_button);
            btn_upload.setText("Send Gift Card");
            makeListOfNonKippinGiftCardToSend(shareType);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationHandler.getInstance().getNotificationForCards(this, this);
        if (kippinGallery) {
            // LogoTemplate= LogoTemplate;
            Log.e("LogoTemplate:", "" + LogoTemplate);
            txtcardName.setText(CardName);
            Picasso.with(this).load(LogoTemplate).fit().centerCrop().into(iv_cardIcon);
            btnFrontImage.setVisibility(View.GONE);

        }
        if (Config.scanner) {
            Log.e("----CONTENT:----:", "" + Config.CONTENT);
            Log.e("---FORMAT:-------", "" + Config.FORMAT);

            Config.scanner = false;
            if (Config.CONTENT.equals("")) {

            } else {
                String cardNo = Config.CONTENT;
                String card_No = Config.FORMAT;
                Log.e("----cardNo----:", "" + cardNo);
                Log.e("---card_No:-------", "" + card_No);
                barcode = cardNo;
                if (cardNo != "") {
                    BitmapDrawable d = (BitmapDrawable) barcodeUtil.generateBarcode(cardNo);
                    backImage = d.getBitmap();

                }
            }


        }


    }

    private void makeListOfNonKippinGiftCardToSend(final ShareType shareType) {
        String userId = String.valueOf(CommonData.getUserData(this).getId());
        String merchantId = getIntent().getStringExtra("merchantId");
        LoadingBox.showLoadingDialog(NonKippinGiftCardListActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetPhysicalCardByUserId(userId, "0", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output ==>", jsonElement.toString());
                Log.e("URL -->", response.getUrl());
                Type listType = new TypeToken<List<Merchant>>() {
                }.getType();
                Gson gson = new Gson();
                LoadingBox.dismissLoadingDialog();
                serverresponse = gson.fromJson(jsonElement.toString(), new TypeToken<List<ResponseToGetLoyaltyCard>>() {
                }.getType());
                for (ResponseToGetLoyaltyCard r : serverresponse) {
                    ResponseToGetLoyaltyCardModel m = new ResponseToGetLoyaltyCardModel();
                    m.setId(r.getId());
                    m.setBackImage(r.getBackImage());
                    m.setFolderName(r.getFolderName());
                    m.setCountry(r.getCountry());
                    m.setFrontImage(r.getFrontImage());
                    m.setActualUserId(r.getActualUserId());
                    m.setLogoImage(r.getLogoImage());
                    m.setMerchantId(r.getMerchantId());
                    m.setBarcode(r.getBarcode());
                    m.setIsChecked(false);
                    temp.add(m);
                }

                boolean flag = Utility.isResponseValid(serverresponse);
                if (flag) {
                    isListTosendGiftcard = true;

                    adapterforListToshare = new NonKippinGiftCardListAdapter(shareType, temp, NonKippinGiftCardListActivity.this, false, new CheckBoxClickHandler() {

                        @Override
                        public void onChechkBoxClick(int id, String folderName, boolean isChecked, CheckBoxType checkboxtype) {
                            if (isChecked) {
                                data_shareGiftCard.put(new Integer(id), folderName);
                            } else {
                                data_shareGiftCard.remove(new Integer(id));
                            }
                        }
                    });
                    list_data.setAdapter(adapterforListToshare);
                } else {
                    //  MessageDialog.showDialog(NonKippinGiftCardListActivity.this, temp.get(0).getResponseMessage(), false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(error.getUrl(), error.getMessage());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(NonKippinGiftCardListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }

    private void sendNonKippinGiftCard() {
        StringBuilder cardId = new StringBuilder();
        StringBuilder folderName = new StringBuilder();
        Set<Integer> keys = data_shareGiftCard.keySet();
        for (Integer key : keys) {
            cardId.append(String.valueOf(key.intValue()));
            cardId.append(",");
            folderName.append(data_shareGiftCard.get(key));
            folderName.append(",");
        }
        cardId.deleteCharAt(cardId.length() - 1);
        final String cardIds = cardId.toString();
        folderName.deleteCharAt(folderName.length() - 1);
        String userId = String.valueOf(CommonData.getUserData(this).getId());
        try {
            LoadingBox.showLoadingDialog(NonKippinGiftCardListActivity.this, "Loading...");
            RestClient.getApiServiceForPojo().TransferPhysicalGiftCard(userId, friendId, cardId.toString(), folderName.toString(), "", new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    Log.e("output ==>", jsonElement.toString());
                    Log.e("URL -->", response.getUrl());
                    Type listType = new TypeToken<List<Merchant>>() {
                    }.getType();
                    Gson gson = new Gson();
                    LoadingBox.dismissLoadingDialog();
                    ResponseModal model = gson.fromJson(jsonElement.toString(), ResponseModal.class);
                    if (model != null) {
                        MessageDialog.showDialog(NonKippinGiftCardListActivity.this, "Card successfully delivered", ActivityDonateToCharity.class, ShareType.DONATEGIFTCARD);

                        // While user share Gift Card It will be remove from his on Account
                        DbNew.getInstance(NonKippinGiftCardListActivity.this).deleteMultipleGiftCard(cardIds);

                    } else {
                        MessageDialog.showDialog(NonKippinGiftCardListActivity.this, model.getResponseMessage(), false);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(error.getUrl(), error.getMessage());
                    LoadingBox.dismissLoadingDialog();
                    MessageDialog.showDialog(NonKippinGiftCardListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
                }
            });

        } catch (Exception ex) {
            Log.e("608", ex.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void donateNonKippinGiftCard() {
        try {

            Network.With(NonKippinGiftCardListActivity.this).getLocationParam(new OnLocationGet() {
                @Override
                public void onLocationGet(double lattitude, double longitude, String mCountry, String city) {
                    HashMap<String, String> jsonBody = new HashMap<String, String>();
                    BitmapDrawable iv_cardIcon_drawable = (BitmapDrawable) iv_cardIcon.getDrawable();

                    jsonBody.put("Country", mCountry);
                    try {
                        jsonBody.put("LogoImage", CommonUtility.encodeTobase64(iv_cardIcon_drawable.getBitmap()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("FrontImage", CommonUtility.encodeTobase64(fromImage));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonBody.put("BackImage", CommonUtility.encodeTobase64(backImage));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jsonBody.put("ActualUserId", String.valueOf(CommonData.getUserData(NonKippinGiftCardListActivity.this).getId()));
                    jsonBody.put("MerchantId", "0");

                    jsonBody.put("Barcode", barcode);
                    LoadingBox.showLoadingDialog(NonKippinGiftCardListActivity.this, "Loading...");
                    RestClient.getApiServiceForPojo().CreatePhysicalGiftCard(jsonBody, new Callback<JsonElement>() {
                        @Override
                        public void success(JsonElement jsonElement, Response response) {
                            Log.e("output ==>", jsonElement.toString());
                            Log.e("URL -->", response.getUrl());
                            Type listType = new TypeToken<List<Merchant>>() {
                            }.getType();
                            Gson gson = new Gson();
                            LoadingBox.dismissLoadingDialog();
                            ResponseModal serverresponse1 = gson.fromJson(jsonElement.toString(), ResponseModal.class);
                            if (serverresponse1.getResponseCode().equals("1")) {
                                MessageDialog.showDialog(NonKippinGiftCardListActivity.this,
                                        "Physical Card successfully uploaded", true);
//                                reSetToNonKippinList();
                            } else {
                                MessageDialog.showDialog(NonKippinGiftCardListActivity.this,
                                        serverresponse1.getResponseMessage(), true);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(error.getUrl(), error.getMessage());
                            LoadingBox.dismissLoadingDialog();
                            MessageDialog.showDialog(NonKippinGiftCardListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
                        }
                    });
                }


            });
        } catch (Exception ex) {
            Log.e("Error Message", ex.getMessage() + "");
        }
    }

    private void makeListOfNonKippinLoyaltyCard() {
        String userId = null;
        try {
            userId = String.valueOf(CommonData.getUserData(this).getId());
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        LoadingBox.showLoadingDialog(NonKippinGiftCardListActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetPhysicalLoyalityCardByUserId(userId, "0", new Callback<JsonElement>() {

            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output ==>", jsonElement.toString());
                Log.e("URL -->", response.getUrl());
                Type listType = new TypeToken<List<Merchant>>() {
                }.getType();
                Gson gson = new Gson();
                serverresponse = gson.fromJson(jsonElement.toString(), new TypeToken<List<ResponseToGetLoyaltyCard>>() {
                }.getType());
                //   serverresponse_db = new ArrayList<ResponseToGetLoyaltyCard>();
                /*for(ResponseToGetLoyaltyCard card : serverresponse){
                    ResponseToGetLoyaltyCard temp = new ResponseToGetLoyaltyCard(card);
                }*/
                boolean flag = Utility.isResponseValid(serverresponse);
                Log.e("flag -->", "" + flag);
                if (flag) {
                    DbNew.getInstance(NonKippinGiftCardListActivity.this).DeleteNonKippinLocal();
                    downLoadingImages(serverresponse, true);
                    isListTosendGiftcard = false;
                } else {
                    LoadingBox.dismissLoadingDialog();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(error.getUrl(), error.getMessage());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(NonKippinGiftCardListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });

    }

    private void makeListOfNonKippinGiftCard() {
        String userId = String.valueOf(CommonData.getUserData(this).getId());
        String merchantId = getIntent().getStringExtra("merchantId");
        LoadingBox.showLoadingDialog(NonKippinGiftCardListActivity.this, "Loading...");
        RestClient.getApiServiceForPojo().GetPhysicalCardByUserId(userId, "0", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output ==>", jsonElement.toString());
                Log.e("URL -->", response.getUrl());
                Type listType = new TypeToken<List<Merchant>>() {
                }.getType();
                Gson gson = new Gson();
                serverresponse = gson.fromJson(jsonElement.toString(), new TypeToken<List<ResponseToGetLoyaltyCard>>() {
                }.getType());
                boolean flag = Utility.isResponseValid(serverresponse);
                if (flag) {
                    DbNew.getInstance(NonKippinGiftCardListActivity.this).DeleteAllNonKippinGift();
                    downLoadingImages(serverresponse, false);
                    isListTosendGiftcard = false;

                } else {
                    LoadingBox.dismissLoadingDialog();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(error.getUrl(), error.getMessage());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(NonKippinGiftCardListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });

    }

    @SuppressLint("LongLogTag")
    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        Log.e("char-------Text:", "" + charText);
        getResponseToGetLoyaltyCardLocalModels.clear();
        if (charText.length() == 0) {
            Log.e("NONKIPPIN-----: ", "" + responseToGetLoyaltyCardModels.size());
            getResponseToGetLoyaltyCardLocalModels.addAll(responseToGetLoyaltyCardModels);
        } else {
            for (ResponseToGetLoyaltyCardLocalModel wp : responseToGetLoyaltyCardModels) {
                //  Log.e("W-----P:::",""+wp.getFolderName());
                if (wp.getFolderName() != null) {
                    if (wp.getFolderName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        getResponseToGetLoyaltyCardLocalModels.add(wp);
                        Log.e("W-----P:::", "" + wp.getFolderName());
                        Log.e("W---Logo--P:::", "" + wp.getLogoImage());
                    }
                }

            }
        }

        Log.e("getResponseToGetLoyaltyCardLocalModels:", "" + getResponseToGetLoyaltyCardLocalModels.size());
        nonKippinGiftCardListAdapterLocal_db = new NonKippinGiftCardListAdapterLocal_DB(getResponseToGetLoyaltyCardLocalModels, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
        // nonKippinListAdapter_db = new NonKippinGiftCardListAdapter1_DB(aVoid, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
        list_data.setAdapter(nonKippinGiftCardListAdapterLocal_db);
        nonKippinGiftCardListAdapterLocal_db.notifyDataSetChanged();

    }

    public void addListener() {
        list_data.setOnItemClickListener(this);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (nonKippinCardType == NonKippinCardType.LOYALTYCARD || nonKippinCardType == NonKippinCardType.GIFTCARD) {
                    if (nonKippinGiftCardListAdapterLocal_db != null) {
                        String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
                        filter(text);
                    }
                    // nonKippinListAdapter_db.getFilter().filter(s);
                } else if (isListTosendGiftcard) {
                    String text = txtSearch.getText().toString().toLowerCase(Locale.getDefault());
                    filter(text);
                    // adapterforListToshare.getFilter().filter(s);
                }

            }
        });

        iv_cardIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_iv_cardIcon_cliked = true;
               /* CROP_IMAGE = 0;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("KIPPIN D CROP")
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setCropMenuCropButtonTitle("Done")
                        .setRequestedSize(400, 400)
                        .setCropMenuCropButtonIcon(R.drawable.ic_tick)
                        .start(NonKippinGiftCardListActivity.this);
*/

                  dialogForGalleryImage();
                // dialogForImage();
                //UploadImageDialog.showUploadImageDialog(NonKippinGiftCardListActivity.this);
            }
        });
        btnFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_iv_cardIcon_cliked = false;
                CROP_IMAGE = 0;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("KIPPIN D CROP")
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setCropMenuCropButtonTitle("Done")
                        .setRequestedSize(400, 400)
                        .setCropMenuCropButtonIcon(R.drawable.ic_tick)
                        .start(NonKippinGiftCardListActivity.this);

                // dialogForImage();
                // UploadImageDialog.showUploadImageDialog(NonKippinGiftCardListActivity.this);
            }
        });
        btnbackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    is_iv_btnbackImage_cliked = true;
                    Intent i = new Intent();
                    i.setClass(NonKippinGiftCardListActivity.this, ScannerActivity.class);
                    //startActivity(i);
                    startActivityForResult(i, REQUEST_SCANNER);
                    /*is_iv_btnbackImage_cliked = true;
                    barcodeUtil.launchScanner(CommonUtility.REQUEST_CODE_BARCODE, new HandleManualInput() {

                        @Override
                        public void onManualClick() {
                            Intent i = new Intent();
                            i.setClass(NonKippinGiftCardListActivity.this, ActivityINputManualBarcode.class);
                            startActivityForResult(i, CommonUtility.REQUEST_MANUAL_BARCODE);

                        }
                    });*/
                } catch (Exception ex) {
                    Log.e("", ex.getMessage());
                }
            }
        });
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("kippinGallery:", "" + kippinGallery);
                if (kippinGallery) {
                    if (backImage != null) {
                        Log.e("backImage:", "" + backImage);
                        if (txtcardName.getText().toString().length() != 0) {
                            if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
                                isuploadLoyaltyCardClicked = true;
                                uploadLoyaltyCard();
                            } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                                isUploadGiftcard = true;
                                uploadGiftCard();
                            }
                        } // IF CLOSE TO CHECK CARD NAME
                        else {
                            if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
                                MessageDialog.showDialog(NonKippinGiftCardListActivity.this, "Please add company name", false);
                            } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                                MessageDialog.showDialog(NonKippinGiftCardListActivity.this, "Please add company name", false);
                            }
                        }
                    } // IF CLOSE FOR BACK IMAGE
                    else {
                        MessageDialog.showDialog(NonKippinGiftCardListActivity.this, "Please Choose back image of physical card", false);
                    }

                } else {
                    if (fromImage != null) {

                        if (backImage != null) {

                            if (txtcardName.getText().toString().length() != 0) {
                                if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
                                    isuploadLoyaltyCardClicked = true;
                                    uploadLoyaltyCard();

                                } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                                    isUploadGiftcard = true;
                                    uploadGiftCard();
                                }
                            } // IF CLOSE TO CHECK CARD NAME
                            else {
                                if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
                                    MessageDialog.showDialog(NonKippinGiftCardListActivity.this, "Please add company name", false);
                                } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                                    MessageDialog.showDialog(NonKippinGiftCardListActivity.this, "Please add company name", false);
                                }

                            }

                        } // IF CLOSE FOR BACK IMAGE
                        else {
                            MessageDialog.showDialog(NonKippinGiftCardListActivity.this, "Please Choose back image of physical card", false);
                        }
                    } // IF CLOSE FOR FRONT IMAGE
                    else {
                        MessageDialog.showDialog(NonKippinGiftCardListActivity.this, "Please Choose front image of physical card", false);
                    }
                }


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
                CommonUtility.moveToTarget(NonKippinGiftCardListActivity.this, UserDashBoardActivity.class);
                //  reSetToNonKippinList();
            }
        });
        btn_upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload:
                if (shareType == ShareType.DONATEGIFTCARD) {
                    //DONATE GIFT CARD
                    if (data_shareGiftCard.size() > 0) {
                        //donateNonKippinGiftCard();
                        sendNonKippinGiftCard();
                    } else {
                        MessageDialog.showDialog(this, "Please select any physical card", false);
                    }

                } else if (shareType == ShareType.SHAREGIFTCARD) {
                    //SHARE GIFT CARD
                    if (data_shareGiftCard.size() > 0) {
                        sendNonKippinGiftCard();
                    } else {
                        MessageDialog.showDialog(this, "Please select any physical card", false);
                    }

                } else {
                    list_data.setVisibility(View.GONE);
                    layout_container_search.setVisibility(View.GONE);
                    tvTopbarTitle.setText("Upload Physical Card");
                    layout_dialog.setVisibility(View.VISIBLE);
                    btn_upload.setVisibility(View.GONE);
                }
                break;
        }

    }

    // funcation to pickImage from Gallery and Camera
    protected void dialogForImage() {
        // TODO Auto-generated method stub

        try {
            lDialog = new Dialog(NonKippinGiftCardListActivity.this);
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
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(NonKippinGiftCardListActivity.this,
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
                            ActivityCompat.requestPermissions(NonKippinGiftCardListActivity.this,
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
                        Toast.makeText(NonKippinGiftCardListActivity.this, "Please check SD card!", Toast.LENGTH_SHORT).show();
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

    protected void dialogForGalleryImage() {
        // TODO Auto-generated method stub

        try {
            lDialog = new Dialog(NonKippinGiftCardListActivity.this);
            lDialog.setCancelable(false);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_new_photo_lib);
            lDialog.show();

            //Button buttonGallery = (Button) lDialog.findViewById(R.id.gallery_library);
            Button kippin_gallery_library = (Button) lDialog.findViewById(R.id.kippin_gallery_library);

            kippin_gallery_library.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        kippinGallery = false;
                        lDialog.dismiss();
                        Intent intent = new Intent(NonKippinGiftCardListActivity.this, KippinCloudGalleryActivity.class);
                        startActivity(intent);

                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }
                }
            });


           /* buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(NonKippinGiftCardListActivity.this,
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
*/
            Button buttonCamera = (Button) lDialog.findViewById(R.id.camera_gallery);
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CROP_IMAGE = 0;
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("KIPPIN D CROP")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setCropMenuCropButtonTitle("Done")
                            .setRequestedSize(400, 400)
                            .setCropMenuCropButtonIcon(R.drawable.ic_tick)
                            .start(NonKippinGiftCardListActivity.this);
                   /* try {
                        //
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(NonKippinGiftCardListActivity.this,
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
                        Toast.makeText(NonKippinGiftCardListActivity.this, "Please check SD card!", Toast.LENGTH_SHORT).show();
                    }*/


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

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inSampleSize=CommonUtility.calculateInSampleSize(onlyBoundsOptions, 100, 100);
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        //double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        //  bitmapOptions.inSampleSize = 3;
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        bitmapOptions.inSampleSize = CommonUtility.calculateInSampleSize(bitmapOptions, 100, 100);

        // Decode bitmap with inSampleSize set
        //options.inJustDecodeBounds = false;
        // options.inSampleSize = 5;
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {


                    try {
                        String imgPath = result.getUri().getPath();
                        Bitmap bmp1 = BitmapFactory.decodeFile(imgPath);
                        //Toast.makeText(this,"data"+bmp1.getByteCount(),Toast.LENGTH_LONG).show();
                        cropedBitmap = null;
                        if (is_iv_cardIcon_cliked) {
                            Bitmap bmp = CommonUtility.decodeSampledBitmapFromResource(imgPath, CommonUtility.dpToPx(this, 100),
                                    CommonUtility.dpToPx(this, 100));
                            //  Toast.makeText(this,"data"+bmp.getByteCount(),Toast.LENGTH_LONG).show();
                            iv_cardIcon.setImageBitmap(bmp);
                        } else {
                            // profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath,CommonUtility.dpToPx(this,130),
                            //CommonUtility.getWidthOfScreen(this)-80
                            Bitmap bmp = CommonUtility.decodeSampledBitmapFromResource(imgPath, 200,
                                    CommonUtility.dpToPx(this, 200));
                            //   Toast.makeText(this,"data"+bmp.getByteCount(),Toast.LENGTH_LONG).show();
                            fromImage = bmp;
                            Log.e("fromImage:", "" + fromImage);
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
                    } catch (Exception ex) {
                        Log.e(ex.getMessage(), ex.getMessage());
                    }



                    // ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                   /* Toast.makeText(
                            this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG)
                            .show();*/
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
            }

            if (requestCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_SCANNER:
                        Log.e("----CONTENT:----:", "" + data.getStringExtra(Config.CONTENT));
                        Log.e("---FORMAT:-------", "" + data.getStringExtra(Config.FORMAT));
                        if (data != null) {

                            String cardNo = data.getStringExtra(Config.CONTENT);
                            String card_No = data.getStringExtra(Config.FORMAT);
                            Log.e("----cardNo----:", "" + cardNo);
                            Log.e("---card_No:-------", "" + card_No);
                            barcode = cardNo;
                            if (cardNo != "") {
                                BitmapDrawable d = (BitmapDrawable) barcodeUtil.generateBarcode(cardNo);
                                backImage = d.getBitmap();
                                /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                backImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            //    backImage.recycle();
                                byte[] byteArray = stream.toByteArray();
                                backImage_base64 = Base64.encodeToString(byteArray, Base64.URL_SAFE);
                                stream.close();
                                stream = null;*/
                            }

                        }
                        break;
                }

            }

            if (resultCode == RESULT_OK) {
                // IF USER CLICK ON FRONTIMAGE OR CARD ICON FOLLOWING CODE EXECUTE WITH CROP
                if (CROP_IMAGE == 0) {
                    switch (requestCode) {
                        case REQUEST_CODE_GALLERY:
                            try {

                                String imagePath = data.getStringExtra("imagePath");
                                Bitmap bm = BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath());
                                Uri u = CommonUtility.getImageUri(NonKippinGiftCardListActivity.this, bm);
                                UCrop.Options options = new UCrop.Options();
                                options.setToolbarColor(Color.parseColor("#ffffff"));
                                options.setToolbarTitle("Merchants List");
                                options.setToolbarWidgetColor(Color.parseColor("#000000"));

                                UCrop.of(u, Uri.parse(imagePath))
                                        .withOptions(options)
                                        .withAspectRatio(16, 9)
                                        .withMaxResultSize(CommonUtility.getWidthOfScreen(NonKippinGiftCardListActivity.this), CommonUtility.getHeightOfScreen(NonKippinGiftCardListActivity.this))
                                        .start(NonKippinGiftCardListActivity.this);
                            } catch (Exception e) {
                                Log.e(TAG, "Error while creating temp file" + e);
                            }
                            break;
                        case REQUEST_CODE_TAKE_PICTURE:

                            try {
                                //  getContentResolver().notifyChange(mImageUri, null);
                                imagePath = new File(mImageUri.getPath()).getAbsolutePath();

                                //Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);

                                Bitmap bm = getThumbnail(mImageUri);
                                File file=CommonUtility.bitmapToUriConverter(NonKippinGiftCardListActivity.this,bm);
                                ExifInterface exifInterface = new ExifInterface(file.getPath());
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
                                // float angle = 90;
                                Bitmap oriented = CommonUtility.fixOrientation(bm, angle);


                                try {

                                    cropedBitmap = null;
                                    if (is_iv_cardIcon_cliked) {
                                        /*Bitmap bmp = CommonUtility.decodeSampledBitmapFromResource(resultUri.getPath(), CommonUtility.dpToPx(this, 100),
                                                CommonUtility.dpToPx(this, 100));*/
                                        //  Toast.makeText(this,"data"+bmp.getByteCount(),Toast.LENGTH_LONG).show();
                                        iv_cardIcon.setImageBitmap(oriented);
                                    } else {
                                        // profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath,CommonUtility.dpToPx(this,130),
                                        //CommonUtility.getWidthOfScreen(this)-80
                                        /*Bitmap bmp = CommonUtility.decodeSampledBitmapFromResource(resultUri.getPath(), 160,
                                                CommonUtility.dpToPx(this, 160));*/

                                        fromImage = oriented;
                                        Log.e("fromImage:", "" + fromImage);
                                    }
                                    /*File actualImafe = new File(CommonUtility.getRealPathFromURI(this, Uri.parse(imagePath)));
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
                                    }*/
                                } catch (Exception ex) {
                                    Log.e(ex.getMessage(), ex.getMessage());
                                }


                                // u1 = CommonUtility.bitmapToUriConverter(NonKippinGiftCardListActivity.this, oriented);
                                //UCrop.Options options = new UCrop.Options();
                                // options.setToolbarColor(Color.parseColor("#ffffff"));
                                // options.setToolbarTitle("Merchants List");
                                //  options.setToolbarWidgetColor(Color.parseColor("#000000"));
                                //  UCrop.of(u1, mImageUri)
                                //        .withOptions(options)
                                //        .withAspectRatio(16, 9)
                                //        .withMaxResultSize(CommonUtility.getWidthOfScreen(NonKippinGiftCardListActivity.this), CommonUtility.getHeightOfScreen(NonKippinGiftCardListActivity.this))
                                //        .start(NonKippinGiftCardListActivity.this);


                            } catch (Exception ex) {
                                Log.e("", ex.getMessage());
                            }
                            //   startCropImage();
                            break;
                        case UCrop.REQUEST_CROP:
                            CROP_IMAGE = 111;
                            try {
                                Uri resultUri = UCrop.getOutput(data);
                                Bitmap bmp1 = BitmapFactory.decodeFile(resultUri.getPath());
                                //Toast.makeText(this,"data"+bmp1.getByteCount(),Toast.LENGTH_LONG).show();
                                cropedBitmap = null;
                                if (is_iv_cardIcon_cliked) {
                                    Bitmap bmp = CommonUtility.decodeSampledBitmapFromResource(resultUri.getPath(), CommonUtility.dpToPx(this, 100),
                                            CommonUtility.dpToPx(this, 100));
                                    //  Toast.makeText(this,"data"+bmp.getByteCount(),Toast.LENGTH_LONG).show();
                                    iv_cardIcon.setImageBitmap(bmp);
                                } else {
                                    // profileImage.setImageBitmap(CommonUtility.decodeSampledBitmapFromResource(imgPath,CommonUtility.dpToPx(this,130),
                                    //CommonUtility.getWidthOfScreen(this)-80
                                    Bitmap bmp = CommonUtility.decodeSampledBitmapFromResource(resultUri.getPath(), 160,
                                            CommonUtility.dpToPx(this, 160));
                                    //   Toast.makeText(this,"data"+bmp.getByteCount(),Toast.LENGTH_LONG).show();
                                    fromImage = bmp;
                                    Log.e("fromImage:", "" + fromImage);
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
                            } catch (Exception ex) {
                                Log.e(ex.getMessage(), ex.getMessage());
                            }
                            break;

                    }
                } // IF CLOSE FOR CROP
                else {


                    if (requestCode == CommonUtility.REQUEST_CODE_TAKEMANUAL_INPUT) // if user enter nannual inout else take no from Scanner
                    { // When user coming back after taking Input
                        if (data != null) {

                            String cardNo = data.getStringExtra("cardNo");
                            barcode = cardNo;
                            if (cardNo != "") {
                                BitmapDrawable d = (BitmapDrawable) barcodeUtil.generateBarcode(cardNo);
                                backImage = d.getBitmap();
                                /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                backImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            //    backImage.recycle();
                                byte[] byteArray = stream.toByteArray();
                                backImage_base64 = Base64.encodeToString(byteArray, Base64.URL_SAFE);
                                stream.close();
                                stream = null;*/
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
                                            try {
                                                barcode = s;
                                                BitmapDrawable d = (BitmapDrawable) barcodeUtil.generateBarcode(s);
                                                backImage = d.getBitmap();
                                               /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                backImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                              //  backImage.recycle();
                                                byte[] byteArray = stream.toByteArray();
                                                backImage_base64 = Base64.encodeToString(byteArray, Base64.URL_SAFE);
                                                stream.close();
                                                stream = null;*/
                                            } catch (Exception ex) {

                                            }
                                            break;
                                    }

                                }
                            }
                        });

                    }


                }
            }


            // IF CLOSE FOR RESULT OK
            else if (resultCode == RESULT_CANCELED) {   // When Click on Close or Mannual Input
                CROP_IMAGE = 111; // IF USER DO NOT CROP THEN RESET IT
                if (data != null) {
                    if (data.getStringExtra("clickedButton") != null) {
                        if (data.getStringExtra("clickedButton").equals("manualInput")) {
                            Intent i = new Intent();
                            i.setClass(NonKippinGiftCardListActivity.this, ActivityINputManualBarcode.class);
                            startActivityForResult(i, CommonUtility.REQUEST_CODE_TAKEMANUAL_INPUT);
                            overridePendingTransition(R.anim.animation_from_left, R.anim.animation_from_right);
                        }
                    }
                }

            }// ELSE IF CLOSE FOR RESULT CANCELLED

        } catch (Exception ex) {
            Log.e("898", ex.getMessage());
        }
    }

    private void reSetToNonKippinList() {
        list_data.setVisibility(View.VISIBLE);
        layout_container_search.setVisibility(View.VISIBLE);
        tvTopbarTitle.setText("Non Kippin Loyalty Card");
        layout_dialog.setVisibility(View.GONE);
        btn_upload.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //removeNonKippingGiftCardNotification(position);
        /*if(nonKippinCardType == NonKippinCardType.GIFTCARD){
            iv_star.setVisibility(View.VISIBLE);

        }else*/
        /*if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {  CLICK HANDLE IN ADAPTER


            Intent i = new Intent();
            i.setClass(this, ActivityUpdateNonKippinLoyaltyCard.class);
            i.putExtra("LoyaltyCardId", serverresponse.get(position).getId());
            i.putExtra("NonKippinCardType", nonKippinCardType);
            i.putExtra("FolderName", serverresponse.get(position).getFolderName());
            i.putExtra("FrontImage", serverresponse.get(position).getFrontImage());
            i.putExtra("BackImage", serverresponse.get(position).getBackImage());
            i.putExtra("LogoImage", serverresponse.get(position).getLogoImage());
            i.putExtra("Barcode", serverresponse.get(position).getBarcode());
            i.putExtra("merchantId", serverresponse.get(position).getMerchantId());
            startActivity(i);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {  CLICK HANDLE IN ADAPTER
            removeNonKippingGiftCardNotification(position); // REMOVE NAOTIFICATION FOR nON kIPPIN pHYSICAL CARD AND THEN VIEW
            Intent i = new Intent();
            i.setClass(this, ActivityUpdateNonKippinLoyaltyCard.class);
            i.putExtra("LoyaltyCardId", serverresponse.get(position).getId());
            i.putExtra("NonKippinCardType", nonKippinCardType);
            i.putExtra("FolderName", serverresponse.get(position).getFolderName());
            i.putExtra("FrontImage", serverresponse.get(position).getFrontImage());
            i.putExtra("BackImage", serverresponse.get(position).getBackImage());
            i.putExtra("LogoImage", serverresponse.get(position).getLogoImage());
            i.putExtra("Barcode", serverresponse.get(position).getBarcode());
            i.putExtra("merchantId", serverresponse.get(position).getMerchantId());
            startActivity(i);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }*/
//        else if(shareType == ShareType.DONATEGIFTCARD){   CLICK HANDLE IN ADAPTER
//            Intent i = new Intent();
//            i.setClass(this,ActivityUpdateNonKippinLoyaltyCard.class);
//            i.putExtra("LoyaltyCardId",serverresponse.get(position).getId());
//            i.putExtra("shareType", shareType);
//            i.putExtra("FolderName", serverresponse.get(position).getFolderName());
//            i.putExtra("FrontImage",serverresponse.get(position).getFrontImage());
//            i.putExtra("BackImage", serverresponse.get(position).getBackImage());
//            i.putExtra("LogoImage", serverresponse.get(position).getLogoImage());
//            i.putExtra("Barcode", serverresponse.get(position).getBarcode());
//            i.putExtra("merchantId", serverresponse.get(position).getMerchantId());
//            startActivity(i);
//            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
//        }
        /*else if (shareType == ShareType.SHAREGIFTCARD) {   CLICK HANDLE IN ADAPTER
            Intent i = new Intent();
            i.setClass(this, ActivityUpdateNonKippinLoyaltyCard.class);
            i.putExtra("LoyaltyCardId", serverresponse.get(position).getId());
            i.putExtra("shareType", shareType);
            i.putExtra("FolderName", serverresponse.get(position).getFolderName());
            i.putExtra("FrontImage", serverresponse.get(position).getFrontImage());
            i.putExtra("BackImage", serverresponse.get(position).getBackImage());
            i.putExtra("LogoImage", serverresponse.get(position).getLogoImage());
            i.putExtra("Barcode", serverresponse.get(position).getBarcode());
            i.putExtra("merchantId", serverresponse.get(position).getMerchantId());
            startActivity(i);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
        }*/

    }

    private void removeNonKippingGiftCardNotification(final int index) {
        RestClient.getApiServiceForPojo().RemoveIsNonPhysical(serverresponse.get(index).getSenderId(), serverresponse.get(index).getUserId(), serverresponse.get(index).getId(), "", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.e("output ==>", jsonElement.toString());
                Log.e("URL -->", response.getUrl());
                Type listType = new TypeToken<List<Merchant>>() {
                }.getType();
                Gson gson = new Gson();
                Boolean flag = gson.fromJson(jsonElement.toString(), Boolean.class);
                if (flag) {
                    Log.e("Notification Remove", "Notification Remove");
                    moveToNextActivity(index);
                } else {

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(error.getUrl(), error.getMessage());
                LoadingBox.dismissLoadingDialog();
                MessageDialog.showDialog(NonKippinGiftCardListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
            }
        });
    }

    private void uploadLoyaltyCard() {
        final String cardName = txtcardName.getText().toString();
        try {

           /* Network.With(NonKippinGiftCardListActivity.this).getLocationParam(new OnLocationGet() {
                @Override
                public void onLocationGet(double lattitude, double longitude, String mCountry,String city) {*/

            if (isuploadLoyaltyCardClicked) {
                isuploadLoyaltyCardClicked = false;
                HashMap<String, String> jsonBody = new HashMap<String, String>();
                BitmapDrawable iv_cardIcon_drawable = (BitmapDrawable) iv_cardIcon.getDrawable();
                final String mCountry = CommonData.getUserData(this).getCountry();
                jsonBody.put("Country", mCountry);
                if (kippinGallery) {
                    jsonBody.put("LogoTemplate", LogoTemplate);
                    jsonBody.put("FolderName", cardName);
                    jsonBody.put("FrontImage", "");//CommonUtility.encodeTobase64(fromImage)
                } else {
                    jsonBody.put("LogoImage", CommonUtility.encodeTobase64(iv_cardIcon_drawable.getBitmap()));//CommonUtility.encodeTobase64(iv_cardIcon_drawable.getBitmap())
                    jsonBody.put("FolderName", cardName);
                    jsonBody.put("FrontImage", CommonUtility.encodeTobase64(fromImage));//CommonUtility.encodeTobase64(fromImage)
                }
                //Log.e("fromImage:", "" + CommonUtility.encodeTobase64(fromImage));
                // Log.e("backImage:", "" + backImage);

                jsonBody.put("BackImage", CommonUtility.encodeTobase64(backImage));                                    // CommonUtility.encodeTobase64(backImage)
                jsonBody.put("ActualUserId", String.valueOf(CommonData.getUserData(NonKippinGiftCardListActivity.this).getId()));
                jsonBody.put("MerchantId", "0");
                jsonBody.put("Barcode", barcode);

                LoadingBox.showLoadingDialog(NonKippinGiftCardListActivity.this, "Loading...");
                RestClient.getApiServiceForPojo().CreatePhysicalLoyalityCard(jsonBody, new Callback<JsonElement>() {
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
                            kippinGallery = false;
                            new LoyalityConnection().execute(nonKippincard);
                            MessageDialog.showDialog(NonKippinGiftCardListActivity.this,
                                    "Physical Card successfully uploaded", true);

                        } else {
                            Log.e("Error Message Loyality:", "" + serverresponse1.getResponseMessage());
                            if (serverresponse1.getResponseMessage().equals("Folder Already exist. Try different name.") && serverresponse1.getResponseCode().equals("2")) {
                                MessageDialog.showDialog(NonKippinGiftCardListActivity.this,
                                        serverresponse1.getResponseMessage(), false);
                            } else {
                                kippinGallery = false;
                                MessageDialog.showDialog(NonKippinGiftCardListActivity.this,
                                        serverresponse1.getResponseMessage(), true);
                            }

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        kippinGallery = false;
                        Log.e(error.getUrl(), error.getMessage());
                        LoadingBox.dismissLoadingDialog();
                        MessageDialog.showDialog(NonKippinGiftCardListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
                    }
                });
            }
              /*  }

            });*/

        } catch (Exception ex) {
            Log.e("Error Message", ex.getMessage() + "");
        }
    }


    class LoyalityConnection extends AsyncTask<NonKippincard, Void, NonKippincard> {
        @Override
        protected NonKippincard doInBackground(NonKippincard... nonKippincards) {
            ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCardLocalModel = new ResponseToGetLoyaltyCardLocalModel();
            try {
                NonKippincard nonKippincard = nonKippincards[0];
                String profileImage = nonKippincard.getLogoImage().replace(" ", "%20");
                String result = profileImage.replaceAll("[\\~]", "");
                String profileImage1 = result.replaceAll("(?<!(http:|https:))[//]+", "/");

                String frontImage = nonKippincard.getFrontImage().replace(" ", "%20");
                String result1 = frontImage.replaceAll("[\\~]", "");
                String frontImage1 = result1.replaceAll("(?<!(http:|https:))[//]+", "/");

                String backImage = nonKippincard.getBackImage().replace(" ", "%20");
                String result2 = backImage.replaceAll("[\\~]", "");
                String backImage1 = result2.replaceAll("(?<!(http:|https:))[//]+", "/");

                Bitmap profile_Bitmap = BitmapFactory.decodeStream((InputStream) new URL(profileImage1).getContent());
                Bitmap front_Bitmap = BitmapFactory.decodeStream((InputStream) new URL(frontImage1).getContent());
                Bitmap back_Bitmap = BitmapFactory.decodeStream((InputStream) new URL(backImage1).getContent());


                responseToGetLoyaltyCardLocalModel.setFrontImage(Base64Image(front_Bitmap));
                responseToGetLoyaltyCardLocalModel.setBackImage(Base64Image(back_Bitmap));
                responseToGetLoyaltyCardLocalModel.setLogoImage(Base64Image(profile_Bitmap));
                responseToGetLoyaltyCardLocalModel.setBarcode(barcode);
                responseToGetLoyaltyCardLocalModel.setActualUserId("" + nonKippincard.getActualUserId());
                responseToGetLoyaltyCardLocalModel.setCountry(nonKippincard.getCountry());
                responseToGetLoyaltyCardLocalModel.setFolderName(nonKippincard.getFolderName());
                responseToGetLoyaltyCardLocalModel.setUserId("" + nonKippincard.getUserId());
                responseToGetLoyaltyCardLocalModel.setMerchantId("0");
                responseToGetLoyaltyCardLocalModel.setId("" + nonKippincard.getId());
                DbNew.getInstance(NonKippinGiftCardListActivity.this).CreateNonKippinCard(responseToGetLoyaltyCardLocalModel.getId(),
                        new Gson().toJson(responseToGetLoyaltyCardLocalModel));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NonKippincard nonKippincard) {
            super.onPostExecute(nonKippincard);

        }
    }

    class GiftConnection extends AsyncTask<NonKippincard, Void, NonKippincard> {
        @Override
        protected NonKippincard doInBackground(NonKippincard... nonKippincards) {
            ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCardLocalModel = new ResponseToGetLoyaltyCardLocalModel();
            try {
                NonKippincard nonKippincard = nonKippincards[0];
                String profileImage = nonKippincard.getLogoImage().replace(" ", "%20");
                String result = profileImage.replaceAll("[\\~]", "");
                String profileImage1 = result.replaceAll("(?<!(http:|https:))[//]+", "/");

                String frontImage = nonKippincard.getFrontImage().replace(" ", "%20");
                String result1 = frontImage.replaceAll("[\\~]", "");
                String frontImage1 = result1.replaceAll("(?<!(http:|https:))[//]+", "/");

                String backImage = nonKippincard.getBackImage().replace(" ", "%20");
                String result2 = backImage.replaceAll("[\\~]", "");
                String backImage1 = result2.replaceAll("(?<!(http:|https:))[//]+", "/");

                Bitmap profile_Bitmap = BitmapFactory.decodeStream((InputStream) new URL(profileImage1).getContent());
                Bitmap front_Bitmap = BitmapFactory.decodeStream((InputStream) new URL(frontImage1).getContent());
                Bitmap back_Bitmap = BitmapFactory.decodeStream((InputStream) new URL(backImage1).getContent());


                responseToGetLoyaltyCardLocalModel.setFrontImage(Base64Image(front_Bitmap));
                responseToGetLoyaltyCardLocalModel.setBackImage(Base64Image(back_Bitmap));
                responseToGetLoyaltyCardLocalModel.setLogoImage(Base64Image(profile_Bitmap));
                responseToGetLoyaltyCardLocalModel.setBarcode(barcode);
                responseToGetLoyaltyCardLocalModel.setActualUserId("" + nonKippincard.getActualUserId());
                responseToGetLoyaltyCardLocalModel.setCountry(nonKippincard.getCountry());
                responseToGetLoyaltyCardLocalModel.setFolderName(nonKippincard.getFolderName());
                responseToGetLoyaltyCardLocalModel.setUserId("" + nonKippincard.getUserId());
                responseToGetLoyaltyCardLocalModel.setMerchantId("0");
                responseToGetLoyaltyCardLocalModel.setId("" + nonKippincard.getId());
                long value = DbNew.getInstance(NonKippinGiftCardListActivity.this).CreateNonGiftCard(responseToGetLoyaltyCardLocalModel.getId(),
                        new Gson().toJson(responseToGetLoyaltyCardLocalModel));
                Log.e("Insert value: ", "" + value);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(NonKippincard nonKippincard) {
            super.onPostExecute(nonKippincard);

        }
    }


    private void uploadGiftCard() {
        final String cardName = txtcardName.getText().toString();
        try {

           /* Network.With(NonKippinGiftCardListActivity.this).getLocationParam(new OnLocationGet() {
                @Override
                public void onLocationGet(double lattitude, double longitude, String mCountry,String city) {*/
            final String mCountry = CommonData.getUserData(this).getCountry();
            if (isUploadGiftcard) {
                isUploadGiftcard = false;

                HashMap<String, String> jsonBody = new HashMap<String, String>();
                BitmapDrawable iv_cardIcon_drawable = (BitmapDrawable) iv_cardIcon.getDrawable();

                jsonBody.put("Country", mCountry);
                if (kippinGallery) {
                    jsonBody.put("LogoTemplate", LogoTemplate);
                    jsonBody.put("FrontImage", "");
                } else {
                    jsonBody.put("LogoImage", CommonUtility.encodeTobase64(iv_cardIcon_drawable.getBitmap()));
                    jsonBody.put("FrontImage", CommonUtility.encodeTobase64(fromImage));
                }

                //CommonUtility.encodeTobase64(iv_cardIcon_drawable.getBitmap())
                // CommonUtility.encodeTobase64(fromImage)
                jsonBody.put("BackImage", CommonUtility.encodeTobase64(backImage)); //CommonUtility.encodeTobase64(backImage)
                jsonBody.put("ActualUserId", String.valueOf(CommonData.getUserData(NonKippinGiftCardListActivity.this).getId()));
                jsonBody.put("MerchantId", "0");
                jsonBody.put("FolderName", cardName);
                jsonBody.put("Barcode", barcode);
                LoadingBox.showLoadingDialog(NonKippinGiftCardListActivity.this, "Loading...");
                RestClient.getApiServiceForPojo().CreatePhysicalGiftCard(jsonBody, new Callback<JsonElement>() {
                    @SuppressLint("LongLogTag")
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
                            kippinGallery = false;
                            new GiftConnection().execute(nonKippincard);
                            MessageDialog.showDialog(NonKippinGiftCardListActivity.this,
                                    "Physical Card successfully uploaded", true);
//                                reSetToNonKippinList();
                        } else {
                            Log.e("Error Message Gift card:", "" + serverresponse1.getResponseMessage());
                            if (serverresponse1.getResponseMessage().equals("Folder Already exist. Try different name.") && serverresponse1.getResponseCode().equals("2")) {
                                MessageDialog.showDialog(NonKippinGiftCardListActivity.this,
                                        serverresponse1.getResponseMessage(), false);
                            } else {
                                kippinGallery = false;
                                MessageDialog.showDialog(NonKippinGiftCardListActivity.this,
                                        serverresponse1.getResponseMessage(), true);
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(error.getUrl(), error.getMessage());
                        LoadingBox.dismissLoadingDialog();
                        MessageDialog.showDialog(NonKippinGiftCardListActivity.this, CommonUtility.TIME_OUT_MESSAGE, false);
                    }
                });
            }
            //     }

            //   });
        } catch (Exception ex) {
            Log.e("Error Message", ex.getMessage() + "");
        }
    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {
        if (IsVoucher) {

        }
        if (IsTradePoint) {

        }
        if (IsFriendRequest) {

        }
        if (IstransferGiftCard) {

        }
        if (IsNewMerchant) {

        }
        if (IsNonKippinPhysical) {
            if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                iv_star.setVisibility(View.VISIBLE);
            }

        }
        if (IsNonKippinLoyalty) {

        }

    }

    private void moveToNextActivity(int position) {
        Intent i = new Intent();
        i.setClass(this, ActivityUpdateNonKippinLoyaltyCard.class);
        i.putExtra("LoyaltyCardId", serverresponse.get(position).getId());
        i.putExtra("NonKippinCardType", nonKippinCardType);
        i.putExtra("FolderName", serverresponse.get(position).getFolderName());
        i.putExtra("FrontImage", serverresponse.get(position).getFrontImage());
        i.putExtra("BackImage", serverresponse.get(position).getBackImage());
        i.putExtra("LogoImage", serverresponse.get(position).getLogoImage());
        i.putExtra("Barcode", serverresponse.get(position).getBarcode());
        i.putExtra("merchantId", serverresponse.get(position).getMerchantId());
        startActivity(i);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    public static File createImageFile(Context context) throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // mImageUri=Uri.fromFile(new File(image.getAbsolutePath()));
        // selectedImagePath = image.getAbsolutePath();
        return image;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //  mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void launchCamera() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (!hasPermissions(activity, PERMISSIONS)) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);
            } else {
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        return;
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            mImageUri = FileProvider.getUriForFile(activity,
                                    "com.development.provider",
                                    createImageFile());
                            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            // Locate the ic_gallery to Share
                            mImageUri = Uri.fromFile(createImageFile());
                        }

                        // mImageUri = Uri.fromFile(createImageFile());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("kippin", "Can't create file to take picture!");
            Toast.makeText(activity, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
        }

    }

    private void takePicture() {
        isCamerClicked = true;
        isGalleryClicked = false;
        //Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {

            if (!hasPermissions(activity, PERMISSIONS)) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);
            } else {
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        mImageUri = FileProvider.getUriForFile(activity,
                                "com.development.provider",
                                createImageFile(activity));
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        // Locate the ic_gallery to Share
                        mImageUri = Uri.fromFile(createImageFile(activity));
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE);
                    //  }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception:", "" + e);
            Log.v("kippin", "Can't create file to take picture!");
            Toast.makeText(activity, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CommonUtility.MY_PERMISSION_ACCESS_CAMERA:
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(NonKippinGiftCardListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(NonKippinGiftCardListActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                com.kippin.utils.Utility.MY_PERMISSION_ACCESS_STORAGE);


                    } else {
                        takePicture();
                        lDialog.dismiss();
                    }

                } else {
                    Toast.makeText(NonKippinGiftCardListActivity.this, "This is a required for the app to function", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(NonKippinGiftCardListActivity.this, "This is a required for the app to function", Toast.LENGTH_LONG).show();
                    lDialog.dismiss();
                }
                break;
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public boolean isNetworkAvailable() {
        return AppStatus.getInstance(activity).isOnline(activity);
    }

    /*private void downloadImages() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (serverresponse_.size() > 0) {
                    ResponseToGetLoyaltyCard responseToGetLoyaltyCard = serverresponse_.get(0);
                    try {
                        if (responseToGetLoyaltyCard != null) {
                            frontBitmap = BitmapFactory.decodeStream((InputStream) new URL(responseToGetLoyaltyCard.getFrontImage()).getContent());
                            backBitmap = BitmapFactory.decodeStream((InputStream) new URL(responseToGetLoyaltyCard.getBackImage()).getContent());
                            profileBitmap = BitmapFactory.decodeStream((InputStream) new URL(responseToGetLoyaltyCard.getLogoImage()).getContent());

                            responseToGetLoyaltyCard.setFrontImage(Base64Image(frontBitmap));
                            responseToGetLoyaltyCard.setBackImage(Base64Image(backBitmap));
                            responseToGetLoyaltyCard.setLogoImage(Base64Image(profileBitmap));
                        }
                        Log.e("FOLDER-----DATa ","= =" +  responseToGetLoyaltyCard.getFolderName());
                        //list_GifCard.add(responseToGetLoyaltyCard);
                        if(nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
                            DbNew.getInstance(NonKippinGiftCardListActivity.this).CreateNonKippinCard(responseToGetLoyaltyCard.getId(),
                                    new Gson().toJson(responseToGetLoyaltyCard));
                        }else{
                            DbNew.getInstance(NonKippinGiftCardListActivity.this).CreateNonGiftCard(responseToGetLoyaltyCard.getId(),
                                    new Gson().toJson(responseToGetLoyaltyCard));
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    catch(Error ex){
                        ex.printStackTrace();
                    }
                }
                if (serverresponse_.size() > 0) {
                    serverresponse_.remove(0);

                    downloadImages();
                }
                else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            }
        }).start();

    }*/
    private void downLoadingImages(List<ResponseToGetLoyaltyCard> temp, boolean isLoyaltyCard) {

        if (temp.size() > 0) {
            if (isLoyaltyCard) {
                new ImageDownloader(this, temp).execute();
            } else {
                new GiftImageDownloader(this, temp).execute();
            }

        }

    }


    class LoadingNonKippinCardsFromLocalDataBase extends AsyncTask<NonKippinCardType, String, List<ResponseToGetLoyaltyCardLocalModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingBox.showLoadingDialog(activity, "Loading...");

        }

        @SuppressLint("LongLogTag")
        @Override
        protected List<ResponseToGetLoyaltyCardLocalModel> doInBackground(NonKippinCardType... params) {
            NonKippinCardType temp = params[0];
            if (temp == NonKippinCardType.LOYALTYCARD) {
                Log.e("HEREHEREREHETEREREREERER", "HEREHEREREHETEREREREERER");
                responseToGetLoyaltyCardLocalModels = DbNew.getInstance(NonKippinGiftCardListActivity.this).get_NonKippinData();

            } else if (temp == NonKippinCardType.GIFTCARD) {
                responseToGetLoyaltyCardLocalModels = DbNew.getInstance(NonKippinGiftCardListActivity.this).getNon_GiftCard();

            }

            return responseToGetLoyaltyCardLocalModels;
        }

        @Override
        protected void onPostExecute(List<ResponseToGetLoyaltyCardLocalModel> aVoid) {
            super.onPostExecute(aVoid);
            Log.e("SIZE-------:", "" + aVoid.size());

            if (aVoid.size() == 0) {
                if (nonKippinCardType == NonKippinCardType.LOYALTYCARD) {
                    makeListOfNonKippinLoyaltyCard();
                } else if (nonKippinCardType == NonKippinCardType.GIFTCARD) {
                    makeListOfNonKippinGiftCard();
                }
            } else {
                getResponseToGetLoyaltyCardLocalModels.clear();
                responseToGetLoyaltyCardModels.clear();
                responseToGetLoyaltyCardModels.addAll(aVoid);
                getResponseToGetLoyaltyCardLocalModels.addAll(aVoid);
                //NonKippinGiftCardListAdapterLocal_DB
                nonKippinGiftCardListAdapterLocal_db = new NonKippinGiftCardListAdapterLocal_DB(aVoid, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
                // nonKippinListAdapter_db = new NonKippinGiftCardListAdapter1_DB(aVoid, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
                list_data.setAdapter(nonKippinGiftCardListAdapterLocal_db);
                LoadingBox.dismissLoadingDialog();
            }

        }
    }

    class LoadingNonKippinCardsFromDataBase extends AsyncTask<NonKippinCardType, String, List<ResponseToGetLoyaltyCard>> {
        ArrayList<ServerResponseToGetLoyaltyCard> toGetLoyaltyCards = new ArrayList<>();


        ServerResponseToGetLoyaltyCard serverResponseToGetLoyaltyCard;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingBox.showLoadingDialog(activity, "Loading...");
            toGetLoyaltyCards.clear();
        }

        @Override
        protected List<ResponseToGetLoyaltyCard> doInBackground(NonKippinCardType... params) {
            NonKippinCardType temp = params[0];
            if (temp == NonKippinCardType.LOYALTYCARD) {
                listCards = DbNew.getInstance(NonKippinGiftCardListActivity.this).getNonKippinData();
            } else if (temp == NonKippinCardType.GIFTCARD) {
                listCards = DbNew.getInstance(NonKippinGiftCardListActivity.this).getNonGiftCard();
            }

            return listCards;
        }

        @Override
        protected void onPostExecute(List<ResponseToGetLoyaltyCard> aVoid) {
            super.onPostExecute(aVoid);
            //  getResponseToGetLoyaltyCardLocalModels.addAll(aVoid);
            //NonKippinGiftCardListAdapterLocal_DB
            //nonKippinGiftCardListAdapterLocal_db=new NonKippinGiftCardListAdapterLocal_DB(aVoid, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
            nonKippinListAdapter_db = new NonKippinGiftCardListAdapter1_DB(aVoid, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
            list_data.setAdapter(nonKippinListAdapter_db);
            LoadingBox.dismissLoadingDialog();
        }
    }

    class ImageDownloader extends AsyncTask<Void, Void, List<ResponseToGetLoyaltyCardLocalModel>> {
        Activity context;
        List<? extends Object> dataToDownload;
        ArrayList<ServerResponseToGetLoyaltyCard> toGetLoyaltyCards = new ArrayList<>();

        ArrayList<ResponseToGetLoyaltyCardLocalModel> response_ToGetLoyaltyCardLocalModel = new ArrayList<>();
        ServerResponseToGetLoyaltyCard serverResponseToGetLoyaltyCard;

        public ImageDownloader(Activity context, List<? extends Object> dataToDownload) {
            this.context = context;
            this.dataToDownload = dataToDownload;
            toGetLoyaltyCards.clear();
            response_ToGetLoyaltyCardLocalModel.clear();
        }

        @Override
        protected List<ResponseToGetLoyaltyCardLocalModel> doInBackground(Void... params) {
            if (dataToDownload.size() > 0) {
                try {
                    for (int i = 0; i < dataToDownload.size(); i++) {
                        ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCardLocalModel = new ResponseToGetLoyaltyCardLocalModel();
                        ResponseToGetLoyaltyCard responseToGetLoyaltyCard = (ResponseToGetLoyaltyCard) dataToDownload.get(i);
                        if (responseToGetLoyaltyCard != null) {
                            //String to = responseToGetLoyaltyCard.getFrontImage().replaceAll("(?<!(http:|https:))[//]+", "/");
                            //String profile = responseToGetLoyaltyCard.getLogoImage().replaceAll("(?<!(http:|https:))[//]+", "/");

                            String profileImage = responseToGetLoyaltyCard.getLogoImage().replace(" ", "%20");
                            String frontImage = responseToGetLoyaltyCard.getFrontImage().replace(" ", "%20");
                            String backImage = responseToGetLoyaltyCard.getBackImage().replace(" ", "%20");

                            serverResponseToGetLoyaltyCard = new ServerResponseToGetLoyaltyCard();
                            serverResponseToGetLoyaltyCard.setBackImage(backImage);
                            serverResponseToGetLoyaltyCard.setFrontImage(frontImage);
                            serverResponseToGetLoyaltyCard.setLogoImage(profileImage);
                            serverResponseToGetLoyaltyCard.setBarcode(responseToGetLoyaltyCard.getBarcode());
                            serverResponseToGetLoyaltyCard.setActualUserId(responseToGetLoyaltyCard.getActualUserId());
                            serverResponseToGetLoyaltyCard.setCountry(responseToGetLoyaltyCard.getCountry());
                            serverResponseToGetLoyaltyCard.setFolderName(responseToGetLoyaltyCard.getFolderName());
                            serverResponseToGetLoyaltyCard.setUserId(responseToGetLoyaltyCard.getUserId());
                            serverResponseToGetLoyaltyCard.setId(responseToGetLoyaltyCard.getId());
                            serverResponseToGetLoyaltyCard.setMerchantId(responseToGetLoyaltyCard.getMerchantId());
                            toGetLoyaltyCards.add(serverResponseToGetLoyaltyCard);


                            // local data base work start
                            Log.e("profileImage:", "" + profileImage);
                            Log.e("frontImage:", "" + frontImage);
                            Log.e("backImage:", "" + backImage);
                            responseToGetLoyaltyCardLocalModel.setOrignalBackImage(backImage);
                            responseToGetLoyaltyCardLocalModel.setOrignalFrontImage(frontImage);
                            responseToGetLoyaltyCardLocalModel.setOrignalLogoImage(profileImage);
                            profileBitmap = BitmapFactory.decodeStream((InputStream) new URL(profileImage).getContent());
                            frontBitmap = BitmapFactory.decodeStream((InputStream) new URL(frontImage).getContent());
                            backBitmap = BitmapFactory.decodeStream((InputStream) new URL(backImage).getContent());

                            responseToGetLoyaltyCardLocalModel.setFrontImage(CommonUtility.encodeTobase64(frontBitmap));
                            responseToGetLoyaltyCardLocalModel.setBackImage(CommonUtility.encodeTobase64(backBitmap));
                            responseToGetLoyaltyCardLocalModel.setLogoImage(CommonUtility.encodeTobase64(profileBitmap));
                            responseToGetLoyaltyCardLocalModel.setBarcode(responseToGetLoyaltyCard.getBarcode());
                            responseToGetLoyaltyCardLocalModel.setActualUserId(responseToGetLoyaltyCard.getActualUserId());
                            responseToGetLoyaltyCardLocalModel.setCountry(responseToGetLoyaltyCard.getCountry());
                            responseToGetLoyaltyCardLocalModel.setFolderName(responseToGetLoyaltyCard.getFolderName());
                            responseToGetLoyaltyCardLocalModel.setUserId(responseToGetLoyaltyCard.getUserId());
                            responseToGetLoyaltyCardLocalModel.setMerchantId(responseToGetLoyaltyCard.getMerchantId());
                            responseToGetLoyaltyCardLocalModel.setId(responseToGetLoyaltyCard.getId());
                            responseToGetLoyaltyCardLocalModel.setIsRead(responseToGetLoyaltyCard.getIsRead());
                            responseToGetLoyaltyCardLocalModel.setSenderId(responseToGetLoyaltyCard.getSenderId());
                            // local data base work close


                        }
                        DbNew.getInstance(NonKippinGiftCardListActivity.this).CreateNonKippinCard(responseToGetLoyaltyCard.getId(),
                                new Gson().toJson(responseToGetLoyaltyCardLocalModel));
                        response_ToGetLoyaltyCardLocalModel.add(responseToGetLoyaltyCardLocalModel);

                    }
                } /*catch (MalformedURLException e) {
                    e.printStackTrace();
                }*/ catch (Exception e) {
                    e.printStackTrace();
                } catch (Error ex) {
                    ex.printStackTrace();
                }
            }
            return response_ToGetLoyaltyCardLocalModel;
        }

        @Override
        protected void onPostExecute(List<ResponseToGetLoyaltyCardLocalModel> aVoid) {
            super.onPostExecute(aVoid);
            getResponseToGetLoyaltyCardLocalModels.clear();
            responseToGetLoyaltyCardModels.clear();
            responseToGetLoyaltyCardModels.addAll(aVoid);
            getResponseToGetLoyaltyCardLocalModels.addAll(aVoid);
            nonKippinGiftCardListAdapterLocal_db = new NonKippinGiftCardListAdapterLocal_DB(aVoid, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
            // nonKippinListAdapter_db = new NonKippinGiftCardListAdapter1_DB(aVoid, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
            list_data.setAdapter(nonKippinGiftCardListAdapterLocal_db);
            LoadingBox.dismissLoadingDialog();
            // nonKippinListAdapter_db = new NonKippinGiftCardListAdapter1_DB(toGetLoyaltyCards, serverresponse, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
            // list_data.setAdapter(nonKippinListAdapter_db);
            //LoadingBox.dismissLoadingDialog();
        }
    }


    class GiftImageDownloader extends AsyncTask<Void, Void, List<ResponseToGetLoyaltyCardLocalModel>> {
        Activity context;
        List<ResponseToGetLoyaltyCard> dataToDownload;
        ArrayList<ServerResponseToGetLoyaltyCard> toGetLoyaltyCards = new ArrayList<>();
        ServerResponseToGetLoyaltyCard serverResponseToGetLoyaltyCard;
        ArrayList<ResponseToGetLoyaltyCardLocalModel> response_ToGetLoyaltyCardLocalModel = new ArrayList<>();

        public GiftImageDownloader(Activity context, List<ResponseToGetLoyaltyCard> dataToDownload) {
            this.context = context;
            this.dataToDownload = dataToDownload;
            toGetLoyaltyCards.clear();
            response_ToGetLoyaltyCardLocalModel.clear();
        }

        @Override
        protected List<ResponseToGetLoyaltyCardLocalModel> doInBackground(Void... params) {
            if (dataToDownload.size() > 0) {
                try {

                    for (int i = 0; i < dataToDownload.size(); i++) {
                        ResponseToGetLoyaltyCardLocalModel responseToGetLoyaltyCardLocalModel = new ResponseToGetLoyaltyCardLocalModel();

                        ResponseToGetLoyaltyCard responseToGetLoyaltyCard = (ResponseToGetLoyaltyCard) dataToDownload.get(i);
                        if (responseToGetLoyaltyCard != null) {

                            String profileImage = responseToGetLoyaltyCard.getLogoImage().replace(" ", "%20");
                            String result = profileImage.replaceAll("[\\~]", "");
                            String profileImage1 = result.replaceAll("(?<!(http:|https:))[//]+", "/");

                            String frontImage = responseToGetLoyaltyCard.getFrontImage().replace(" ", "%20");
                            String result1 = frontImage.replaceAll("[\\~]", "");
                            String frontImage1 = result1.replaceAll("(?<!(http:|https:))[//]+", "/");

                            String backImage = responseToGetLoyaltyCard.getBackImage().replace(" ", "%20");
                            String result2 = backImage.replaceAll("[\\~]", "");
                            String backImage1 = result2.replaceAll("(?<!(http:|https:))[//]+", "/");

                            serverResponseToGetLoyaltyCard = new ServerResponseToGetLoyaltyCard();
                            serverResponseToGetLoyaltyCard.setBackImage(backImage);
                            serverResponseToGetLoyaltyCard.setFrontImage(frontImage);
                            serverResponseToGetLoyaltyCard.setLogoImage(profileImage);
                            serverResponseToGetLoyaltyCard.setBarcode(responseToGetLoyaltyCard.getBarcode());
                            serverResponseToGetLoyaltyCard.setActualUserId(responseToGetLoyaltyCard.getActualUserId());
                            serverResponseToGetLoyaltyCard.setCountry(responseToGetLoyaltyCard.getCountry());
                            serverResponseToGetLoyaltyCard.setFolderName(responseToGetLoyaltyCard.getFolderName());
                            serverResponseToGetLoyaltyCard.setUserId(responseToGetLoyaltyCard.getUserId());
                            serverResponseToGetLoyaltyCard.setMerchantId(responseToGetLoyaltyCard.getMerchantId());
                            serverResponseToGetLoyaltyCard.setId(responseToGetLoyaltyCard.getId());
                            toGetLoyaltyCards.add(serverResponseToGetLoyaltyCard);
                            // local data base work start
                            profileBitmap = BitmapFactory.decodeStream((InputStream) new URL(profileImage1).getContent());
                            frontBitmap = BitmapFactory.decodeStream((InputStream) new URL(frontImage1).getContent());
                            backBitmap = BitmapFactory.decodeStream((InputStream) new URL(backImage1).getContent());
                            responseToGetLoyaltyCardLocalModel.setFrontImage(CommonUtility.encodeTobase64(frontBitmap));
                            responseToGetLoyaltyCardLocalModel.setBackImage(CommonUtility.encodeTobase64(backBitmap));
                            responseToGetLoyaltyCardLocalModel.setLogoImage(CommonUtility.encodeTobase64(profileBitmap));
                            responseToGetLoyaltyCardLocalModel.setBarcode(responseToGetLoyaltyCard.getBarcode());
                            responseToGetLoyaltyCardLocalModel.setActualUserId(responseToGetLoyaltyCard.getActualUserId());
                            responseToGetLoyaltyCardLocalModel.setCountry(responseToGetLoyaltyCard.getCountry());
                            responseToGetLoyaltyCardLocalModel.setFolderName(responseToGetLoyaltyCard.getFolderName());
                            responseToGetLoyaltyCardLocalModel.setUserId(responseToGetLoyaltyCard.getUserId());
                            responseToGetLoyaltyCardLocalModel.setMerchantId(responseToGetLoyaltyCard.getMerchantId());
                            responseToGetLoyaltyCardLocalModel.setId(responseToGetLoyaltyCard.getId());
                            responseToGetLoyaltyCardLocalModel.setIsRead(responseToGetLoyaltyCard.getIsRead());
                            responseToGetLoyaltyCardLocalModel.setSenderId(responseToGetLoyaltyCard.getSenderId());
                            // local data base work close
                        }
                        long value = DbNew.getInstance(NonKippinGiftCardListActivity.this).CreateNonGiftCard(responseToGetLoyaltyCard.getId(),
                                new Gson().toJson(responseToGetLoyaltyCardLocalModel));
                        Log.e("--value--:", "" + value);
                        response_ToGetLoyaltyCardLocalModel.add(responseToGetLoyaltyCardLocalModel);
                        // local data entry done
                    }
                } /*catch (MalformedURLException e) {
                    e.printStackTrace();
                } */ catch (Exception e) {
                    e.printStackTrace();
                } catch (Error ex) {
                    ex.printStackTrace();
                }
            }
            return response_ToGetLoyaltyCardLocalModel;
        }

        @Override
        protected void onPostExecute(List<ResponseToGetLoyaltyCardLocalModel> aVoid) {
            super.onPostExecute(aVoid);
            getResponseToGetLoyaltyCardLocalModels.clear();
            responseToGetLoyaltyCardModels.clear();
            responseToGetLoyaltyCardModels.addAll(aVoid);
            getResponseToGetLoyaltyCardLocalModels.addAll(aVoid);


            nonKippinGiftCardListAdapterLocal_db = new NonKippinGiftCardListAdapterLocal_DB(aVoid, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
            // nonKippinListAdapter_db = new NonKippinGiftCardListAdapter1_DB(aVoid, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
            list_data.setAdapter(nonKippinGiftCardListAdapterLocal_db);
            LoadingBox.dismissLoadingDialog();
            // nonKippinListAdapter_db = new NonKippinGiftCardListAdapter1_DB(toGetLoyaltyCards, serverresponse, NonKippinGiftCardListActivity.this, nonKippinCardType, shareType);
            // list_data.setAdapter(nonKippinListAdapter_db);
            // LoadingBox.dismissLoadingDialog();
        }
    }
}
