package com.kippinretail;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.kippinretail.Adapter.FolderListAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.ErrorCodes;
import com.kippinretail.ImagePickUtility.CapturePicView;
import com.kippinretail.Modal.FolderList.FolderDetail;
import com.kippinretail.ImagePickUtility.CameraPicListener;
import com.kippinretail.Modal.ModalResponse;
import com.kippinretail.Modal.dialogbox.DialogBoxListener;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.Modal.webclient.model.ArrayListPost;
import com.kippinretail.config.Utils;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyPoint_FolderListActivity extends SuperActivity implements AdapterView.OnItemClickListener, View.OnClickListener {


    public static final String GIFTCARD = "gift_card";
    public static final String LOYALITY_CARD = "loyality_card";
    public static final String OPERATION = "operation";
    public static String PUNCH_CARD = "PUNCH_CARD";
    TextView txtLogo, txtMessage;
    ListView listFolder;
    private String customerID, merchantId;
    private List<FolderDetail> folderDetails;
    private Button btnUpload;
    private int REQUEST_CODE_GALLERY = 100;
    private int REQUEST_CODE_CAMERA = 200;
    private Button btnFrontImage, btnbackImage;
    boolean btnFrontImageClicked = false;
    boolean btnbackImageClicked = false;
    private CapturePicView capturePicView;
    Bitmap bFront, bBack;
    View include_search;
    EditText txtSearch;
    private String heading = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_point__folder_list);
        initilization();
        setUpUI();
    }

    public void setUpUI() {
        makeFolderList();
    }

    private void initilization()
    {
        generateActionBar(R.string.tile_upload_physical_card,true,true,false);
        include_search = (View)findViewById(R.id.include_search);
        listFolder = (ListView) findViewById(R.id.listFolder);
        txtSearch  = (EditText)findViewById(R.id.txtSearch);
        txtLogo = (TextView) findViewById(R.id.txtLogo);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        listFolder.setOnItemClickListener(this);
        customerID = String.valueOf(CommonData.getUserData(MyPoint_FolderListActivity.this).getId());
        btnUpload.setOnClickListener(this);
        Intent i = getIntent();
        if (i != null) {
            merchantId = i.getStringExtra("merchantId");
            heading = i.getStringExtra("heading");

        }
        setLogoText(heading);

        capturePicView = new CapturePicView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideKeyboard(MyPoint_FolderListActivity.this);
    }




    private void makeFolderList() {
        Log.e("customerID  ", customerID);
      //  LoadingBox.showLoadingDialog(MyPoint_FolderListActivity.this, "Loading...");
//        switch (getIntent().getStringExtra(OPERATION)) {
//            case GIFTCARD:
//                RestClient.getApiServiceForPojo().GetPhysicalCardFolderName(customerID, 0 + "", callback);
//                break;
//            case LOYALITY_CARD:
//                RestClient.getApiServiceForPojo().MyLoyalityFolderList(customerID, 0 + "", callback);
//                break;
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpload:
                CommonUtility.startNewActivity(this,UploadPhysicalCardActivity.class);
                break;
        }
    }

    private void showUploadDialog() {
        final Dialog dialog = new Dialog(this,
                R.style.Theme_AppCompat_Translucent);
        dialog.setContentView(R.layout.dialog_uplaod_image);
        final EditText txtCompanyName = (EditText) dialog.findViewById(R.id.txtCompanyName);
        btnFrontImage = (Button) dialog.findViewById(R.id.btnFrontImage);
        btnbackImage = (Button) dialog.findViewById(R.id.btnbackImage);
        Button btnUploadImage = (Button) dialog.findViewById(R.id.btnUploadImage);
        dialog.setCanceledOnTouchOutside(true);
        View focucedView = getCurrentFocus();
        if(focucedView!=null){
            focucedView.clearFocus();
        }
        Utils.hideKeyboard(this);
        btnFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFrontImageClicked = true;
                btnbackImageClicked = false;
                showChoosePhotoDialog();
//                UploadImageDialog.showUploadImageDialog(MyPoint_FolderListActivity.this);
            }
        });

        btnbackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFrontImageClicked = false;
                btnbackImageClicked = true;
                showChoosePhotoDialog();
//                UploadImageDialog.showUploadImageDialog(MyPoint_FolderListActivity.this);
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {

                                              @Override
                                              public void onClick(View v) {
                                                  uploadPhysicalCard();
                                              }

                                              private void uploadPhysicalCard() {

                                                  ArrayListPost list = new ArrayListPost();
                                                  list.add("FolderName", txtCompanyName.getText().toString());
                                                  list.add("MerchantId", "0");
                                                  try {
                                                      list.add("FrontImage", Utility.convertBitmap2Base64(bFront));
                                                  } catch (Exception e) {
                                                      e.printStackTrace();
                                                  }

                                                  try {
                                                      list.add("BackImage", Utility.convertBitmap2Base64(bBack));
                                                  } catch (Exception e) {
                                                      e.printStackTrace();
                                                  }

                                                  list.add("Country", "India");
                                                  list.add("ActualUserId", CommonData.getUserData(MyPoint_FolderListActivity.this).getId() + "");

                                                  LoadingBox.showLoadingDialog(MyPoint_FolderListActivity.this, "Loading...");

                                                  switch (getIntent().getStringExtra(OPERATION)){

                                                      case LOYALITY_CARD:
                                                          RestClient.getApiServiceForPojo().CreatePhysicalLoyalityCard(Utility.getTypedInput(list.getJson()), create_physical_card_callback);
                                                          break;

                                                      case GIFTCARD:
                                                      //    RestClient.getApiServiceForPojo().CreatePhysicalGiftCard(Utility.getTypedInput(list.getJson()), create_physical_card_callback);
                                                          break;


                                                  }
                                              }

                                              Callback create_physical_card_callback = new Callback<JsonElement>() {
                                                  @Override
                                                  public void success(JsonElement jsonElement, Response response) {
                                                      Gson gson = new Gson();
                                                      ModalResponse modalResponse = gson.fromJson(jsonElement, ModalResponse.class);
                                                      startDialog(modalResponse.getResponseMessage(), new DialogBoxListener() {
                                                          @Override
                                                          public void onDialogOkPressed() {
                                                              handler.post(new Runnable() {
                                                                  @Override
                                                                  public void run() {
                                                                      dialog.dismiss();
                                                                  }
                                                              });
                                                              makeFolderList();
                                                          }
                                                      });
                                                      LoadingBox.dismissLoadingDialog();
                                                  }

                                                  @Override
                                                  public void failure(RetrofitError error) {
                                                      startDialog(error.getMessage());
                                                  }
                                              };
                                          }
        );

        WindowManager.LayoutParams layoutParams = dialog.getWindow()
                .getAttributes();
        layoutParams.dimAmount = 0.6f;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FolderDetail folderDetail = folderDetails.get(position);
        Intent intent = new Intent();
        intent.setClass(MyPoint_FolderListActivity.this, MyPoint_UserLoyalityCardActivity.class);
        intent.putExtra(OPERATION, getIntent().getStringExtra(OPERATION));
        intent.putExtra("folderName", folderDetail.getFolderName());
        intent.putExtra("merchantId", merchantId);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }

    private void showChoosePhotoDialog() {

        try {
            final Dialog lDialog = new Dialog(MyPoint_FolderListActivity.this);
            lDialog.setCancelable(false);
            lDialog.setCanceledOnTouchOutside(true);
            lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialog.setContentView(R.layout.dialog_choose_photo);
            lDialog.show();

            Button buttonGallery = (Button) lDialog.findViewById(R.id.gallery_library);
            buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivityForResult(new Intent(MyPoint_FolderListActivity.this, GalleryActivity.class), REQUEST_CODE_GALLERY);
//                    overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                    capturePicView.launchGallery();
                    lDialog.dismiss();
                }
            });

            Button buttonCamera = (Button) lDialog.findViewById(R.id.camera);
            buttonCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivityForResult(new Intent(MyPoint_FolderListActivity.this, CameraActivity.class), REQUEST_CODE_CAMERA);
//                    overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                    capturePicView.launchCamera();
                    lDialog.dismiss();
                }
            });


            Button cancel= (Button) lDialog.findViewById(R.id.Cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lDialog.dismiss();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            capturePicView.onActivityResult(requestCode, resultCode, data, new CameraPicListener() {
                @Override
                public void onImageSelected(Bitmap optionSelected) {

                    if (btnFrontImageClicked) {
                        bFront = optionSelected;
//                            btnFrontImage.setBackgroundColor(getResources().getColor(R.color.frontImageButtonClick));
                        changeBackgroundColor(btnFrontImage, getResources().getColor(R.color.frontImageButtonClick));
                    } else if (btnbackImageClicked) {
                        bBack = optionSelected;
//                            btnbackImage.setBackgroundColor(getResources().getColor(R.color.frontImageButtonClick));
                        changeBackgroundColor(btnbackImage, getResources().getColor(R.color.frontImageButtonClick));
                    }
                }


                @Override
                public void onError() {
                    startDialog(R.string.error_while_fetching_image);
                }
            });



    }


    private void changeBackgroundColor(final View view, final int color) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                view.setBackgroundColor(color);
            }
        });
    }


    Callback callback = new Callback<JsonElement>() {
        @Override
        public void success(JsonElement jsonElement, Response response) {
            Log.e("output -->", jsonElement.toString());
            Log.e("URL -->", response.getUrl());
            Type listType = new TypeToken<List<FolderDetail>>() {
            }.getType();
            Gson gson = new Gson();
            folderDetails = (List<FolderDetail>) gson.fromJson(jsonElement.toString(), listType);
            if (folderDetails != null) {
                if (folderDetails.size() == 1 && !folderDetails.get(0).getResponseMessage().equals("Success")) {
//                   MessageDialog.showDialog(MyPoint_FolderListActivity.this, "No Folder Avaiable");


                } else {
                    listFolder.setAdapter(new FolderListAdapter(MyPoint_FolderListActivity.this, folderDetails));
                }
            }
            LoadingBox.dismissLoadingDialog();
        }

        @Override
        public void failure(RetrofitError error) {
            LoadingBox.dismissLoadingDialog();
            ErrorCodes.checkCode(MyPoint_FolderListActivity.this,error);
        }
    };



}
