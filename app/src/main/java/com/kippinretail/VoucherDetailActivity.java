package com.kippinretail;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.TouchImageView;
import com.kippinretail.Modal.VoucherList.VoucherDetail;
import com.squareup.picasso.Picasso;

public class VoucherDetailActivity extends SuperActivity implements View.OnClickListener {


    private VoucherDetail voucherDetail;
    private TouchImageView voucherImage;
    TextView txtImageName,txtStartDate,txtEndDate;
    int width , height;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);
        initilization();
    }

    private void initilization()
    {
        generateActionBar(R.string.title_user_voucher_detail,true,true,false);
        voucherImage = (TouchImageView)findViewById(R.id.voucherImage);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            width = CommonUtility.getWidthOfScreen(this)/2-CommonUtility.dpToPx(this,30);
            height = CommonUtility.dpToPx(this,200);
        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            width = CommonUtility.getWidthOfScreen(this)-CommonUtility.dpToPx(this,30);
            height = CommonUtility.dpToPx(this,250);
        }


        txtImageName = (TextView)findViewById(R.id.txtImageName);
        txtStartDate = (TextView)findViewById(R.id.txtStartDate);
        txtEndDate = (TextView)findViewById(R.id.txtEndDate);
        Intent i = getIntent();
        if(i!=null)
        {
            voucherDetail = (VoucherDetail)i.getSerializableExtra("voucherDetail");
            if(voucherDetail!=null)
            {
                if(voucherDetail.getVoucherImage()!=null)
                {
                   Picasso.with(VoucherDetailActivity.this).load(voucherDetail.getVoucherImage())
                           .resize(width,height)
                           .into(voucherImage);
                }
                if(voucherDetail.getVoucherName()!=null){
                    int extPos = voucherDetail.getVoucherName().lastIndexOf(".");
                    if(extPos == -1) {
                        txtImageName.setText(voucherDetail.getVoucherName());
                    }
                    else {
                        txtImageName.setText(voucherDetail.getVoucherName().substring(0, extPos));
                    }
                }
                if (voucherDetail.getStartDate()!=null) {
                    String startDate = voucherDetail.getStartDate().substring(0,10);
                    String startDay = startDate.substring(8, 10);
                    String startMonth = startDate.substring(5,7);
                    String startYear = startDate.substring(0,4);
                    StringBuilder _startDate = new StringBuilder();
                    _startDate.append(startMonth);_startDate.append("-");
                    _startDate.append(startDay);_startDate.append("-");
                    _startDate.append(startYear);
                    txtStartDate.setText("Start date: "+_startDate.toString());
                }
                if(voucherDetail.getEndDate()!=null){
                    String endDate = voucherDetail.getEndDate().substring(0,10);
                    String endDay = endDate.substring(8, 10);
                    String endMonth = endDate.substring(5,7);
                    String endYear = endDate.substring(0, 4);
                    StringBuilder _endDate = new StringBuilder();
                    _endDate.append(endMonth);_endDate.append("-");
                    _endDate.append(endDay);_endDate.append("-");
                    _endDate.append(endYear);
                    txtEndDate.setText("End date: "+_endDate.toString());
                }

            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }
}
