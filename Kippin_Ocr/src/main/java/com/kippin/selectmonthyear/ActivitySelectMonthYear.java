package com.kippin.selectmonthyear;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.kippin.app.Kippin;
import com.kippin.cloudimages.ActivityCloudImages;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperActivity;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.model.TemplateData;

import java.util.ArrayList;

public class ActivitySelectMonthYear extends SuperActivity implements AdapterView.OnItemClickListener, View.OnClickListener{

    private boolean selectMonth = true;
    public static String MONTH = "month";
    public static String YEAR = "year";
    private ArrayList<TemplateMonth> datas = new ArrayList<>();
    private String url = "";
    private ListView list;
    private int selectedPosition = -1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activity_select_month_year);
        list = (ListView)findViewById(R.id.listView);


        Bundle bundle = getIntent().getExtras();


        if (bundle == null) {
            //        means person has to first select year first
            url = Url.getImages_month();
            selectMonth=false ;
            generateActionBar(R.string.select_year,true,true);
            Utility.yearActivity = ActivitySelectMonthYear.this;
        } else {
            generateActionBar(R.string.select_month,true,true);
            String year = bundle.getString(YEAR);
            url = Url.getImagesByyear(year);
            Utility.monthActivity = ActivitySelectMonthYear.this;
        }

        WSUtils.hitServiceGet(ActivitySelectMonthYear.this, url, new ArrayListPost(), TemplateMonths.class, new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {
                if(data!=null ){
                    datas = data.getData(TemplateMonths.class).months;
                    if(datas.get(0).getResponseCode().equalsIgnoreCase("2")){
                        new DialogBox(ActivitySelectMonthYear.this, Kippin.kippin.getString(R.string.no_image_found), new DialogBoxListener() {
                            @Override
                            public void onDialogOkPressed() {
                                ActivitySelectMonthYear.this.finish();
                            }
                        });
                    }else{
                        AdapterMonth adapterMonth = new AdapterMonth(ActivitySelectMonthYear.this,datas);
                        list.setAdapter(adapterMonth);
                        list.setOnItemClickListener(ActivitySelectMonthYear.this);
                    }
                }

            }
        });

    }

    @Override
    public void setUpListeners() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(selectedPosition>-1){
        list.getChildAt(selectedPosition).setBackgroundColor(Color.WHITE);
        }
        view.setBackgroundColor(Color.LTGRAY);
        selectedPosition = position;
        onItemClick();
    }



    @Override
    public void onClick(View v) {
    onItemClick();
    }

    private void onItemClick() {
        try {
            Bundle bundle = getIntent().getExtras()==null?(new Bundle()):getIntent().getExtras();
            if(selectMonth){
                bundle.putString(MONTH, datas.get(selectedPosition).getFolderName());
                bundle.putString(ActivityCloudImages.CLOUD_IMG , Url.getImagesByyearAndMonth(bundle.getString(YEAR), bundle.getString(MONTH)));
                bundle.putBoolean(ActivityCloudImages.CLICK_NEEDED, true);
                Utility.startActivity(this, ActivityCloudImages.class, bundle, true);
            }else{
                bundle.putString(YEAR,datas.get(selectedPosition).getFolderName());
                Utility.startActivity(this, ActivitySelectMonthYear.class, bundle, true);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utility.monthActivity = null;
        Utility.yearActivity = null;
    }
}
