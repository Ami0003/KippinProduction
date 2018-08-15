package com.kippin.cloudimages;

import android.os.Bundle;
import android.widget.GridView;

import com.kippin.app.Kippin;
import com.kippin.kippin.R;
import com.kippin.photoPreview.Activity_Photo_Preview;
import com.kippin.selectmonthyear.ActivitySelectMonthYear;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModelCloudImage;
import com.kippin.webclient.model.ModelCloudImages;
import com.kippin.webclient.model.TemplateData;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ActivityCloudImages extends SuperActivity  {

    public static String month;
    public static String year,cloud_image ;
   // @Bind(R.id.gvImages)
    GridView gridView;

    private AdapterCloudImages images;
    private String  url = "";
    public final static String CLOUD_IMG  ="cloud_img" ;
    private ArrayList<ModelCloudImage> modelCloudImages = new ArrayList<>();
    public final static String CLICK_NEEDED  ="CLICK_NEEDED" ;
    private boolean isClickNeeded=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_images);
        initialiseUI();
        setUpListeners();

        Utility.activityCloudImages = this;

        Bundle bundle = getIntent().getExtras();
        isClickNeeded = bundle.getBoolean(CLICK_NEEDED);
        if(!isClickNeeded)
        generateActionBar(R.string.uploaded_bills, true, true);
        else{
            generateActionBar(R.string.cloud_images, true, true);
        }
//        http://kippin_finance_api.web1.anzleads.com/KippinFinance_Api/GeneralLedger/GetBillUploadedImages/{userId}/{statementId}
         cloud_image  = bundle.getString(CLOUD_IMG) ;
        year = bundle.getString(ActivitySelectMonthYear.YEAR);
               month =  bundle.getString(ActivitySelectMonthYear.MONTH);
        loadList();
    }

    public void initialiseUI(){
        gridView =(GridView)findViewById(R.id.gvImages);

    }
    @Override
    protected void onResume() {
        super.onResume();

        if(Activity_Photo_Preview.reloadList){
            loadList();
        }

    }

    @Override
    public void setUpListeners() {
    }

    private void loadList(){

        WSUtils.hitServiceGet(this, cloud_image, new ArrayListPost(), ModelCloudImages.class, new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {
                if (data != null) {
                    modelCloudImages = data.getData(ModelCloudImages.class).images;

                    if(modelCloudImages.size()==0){
                        new DialogBox(ActivityCloudImages.this, Kippin.res.getString(R.string.no_image_available), new DialogBoxListener() {
                            @Override
                            public void onDialogOkPressed() {
                                ActivityCloudImages.this.finish();
                            }
                        });
                    }else
                    if(modelCloudImages.get(0).getResponseCode().equalsIgnoreCase("2")){
                        new DialogBox(ActivityCloudImages.this, Kippin.res.getString(R.string.no_image_available), new DialogBoxListener() {
                            @Override
                            public void onDialogOkPressed() {
                                ActivityCloudImages.this.finish();
                            }
                        });
                    }else {

                        int size = modelCloudImages.size();

                      /*  StringBuffer indexes = new StringBuffer();

                        for(int i  = 0 ; i < size; i++ ){
                            String name = modelCloudImages.get(i) .getImageName() ;
                            if(!Utility.isImageFileValid(name)) indexes.append(i+":");
                        }

                        StringTokenizer stringTokenizer = new StringTokenizer(indexes.toString(),":");
                        int tokens = stringTokenizer.countTokens();

                        Integer[] tokens_  = new Integer[tokens];

                        for(int i  = 0 ; i < tokens; i++ ){
                            tokens_[i] =  Integer.parseInt(stringTokenizer.nextToken());
                        }

                        for(int i  = tokens-1; i >= 0; i-- ){
                            modelCloudImages.remove((int)tokens_[i]) ;
                        }*/

                        AdapterCloudImages adapterCloudImages = new AdapterCloudImages(ActivityCloudImages.this, modelCloudImages, isClickNeeded);
                        gridView.setAdapter(adapterCloudImages);
                    }
                }
            }
        }) ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.activityCloudImages=null;
    }

}
