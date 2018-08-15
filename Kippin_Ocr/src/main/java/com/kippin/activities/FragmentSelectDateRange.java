package com.kippin.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippin.app.Kippin;
import com.kippin.kippin.R;
import com.kippin.superviews.SuperFragment;
import com.kippin.utils.DatePickerSelector;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModalIncomeStatement;
import com.kippin.webclient.model.TemplateData;

import java.util.Calendar;
import java.util.Date;

public class FragmentSelectDateRange extends SuperFragment implements DatePickerSelector.onDatePickerSelector {

  //  @Bind(R.id.tvSelectStartDate)
    TextView tvSelectStartDate;

  //  @Bind(R.id.tvSelectEndDate)
    TextView tvSelectEndDate;

    private final int requestCodeStart = 1001 , requestCodeEnd = 1002;
    DatePickerSelector datePickerSelectorStart,datePickerSelectorEnd;

    ImageView ivNext;

    Date start,end;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        root  = inflater.inflate(R.layout.fragmentselectdaterange , null, false);
        tvSelectStartDate = (TextView)root.findViewById(R.id.tvSelectStartDate);
        tvSelectEndDate = (TextView)root.findViewById(R.id.tvSelectEndDate);
        ivNext = (ImageView )root.findViewById(R.id.iv_Next);
        initialiseUI();
        setUpListeners();
        generateActionBar(R.string.select_period,false,false);


        return root;
    }

    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_date_range);
//        initialiseUI();
//        generateActionBar(R.string.select_period,true,true);
//    }

    @Override
    public void initialiseUI() {
        super.initialiseUI( );
        datePickerSelectorStart = new DatePickerSelector(getActivity(),this) ;
        datePickerSelectorEnd = new DatePickerSelector(getActivity(),this) ;
//        tvSelectStartDate = (TextView)getActivity().findViewById(R.id.tvSelectStartDate);
//        tvSelectEndDate = (TextView)getActivity().findViewById(R.id.tvSelectEndDate);
    }

//    @OnClick(R.id.tvSelectStartDate)
    public void onStartDateClick(View v){
        datePickerSelectorStart.show(requestCodeStart);
    }


 //   @OnClick(R.id.tvSelectEndDate)
    public void onEndDateClick(View v){
        datePickerSelectorEnd.show(requestCodeEnd);
    }


    @Override
    public void setUpListeners() {

        tvSelectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartDateClick(v);
            }
        });


        tvSelectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEndDateClick(v);
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextClick(v);
            }
        });
    }

   // @OnClick(R.id.ivNext)
    public void onNextClick(View v) {

        if(isValidated()){
            WSUtils.hitServiceGet(getActivity(), Url.getSelectPeriodUrl(tvSelectStartDate.getText().toString(),tvSelectEndDate.getText().toString()), new ArrayListPost(), ModalIncomeStatement.class, new WSInterface() {
                @Override
                public void onResult(int requestCode, TemplateData data) {

                    ModalIncomeStatement modalIncomeStatement = (ModalIncomeStatement)data ;

                    if( modalIncomeStatement==null ||  (modalIncomeStatement.getObjExpenseList()==null && modalIncomeStatement.getObjRevenueList()==null )){

                        new DialogBox(getActivity(), Kippin.res.getString(R.string.no_record_found) ,(DialogBoxListener)null ) ;

                        return;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(returnName(ModalIncomeStatement.class),(ModalIncomeStatement)data);
                    Utility.startActivity(getActivity(),ActivityIncomeStatement.class,bundle,true);
                }
            });
        }


    }

    private boolean isValidated() {

        int error = -1;


        boolean isValidated = true;

        if(TextUtils.isEmpty(tvSelectStartDate .getText().toString())){
            isValidated = false;
            error = R.string.start_date_empty ;
        } else if(TextUtils.isEmpty(tvSelectEndDate.getText().toString())){
            isValidated = false;
            error = R.string.end_date_empty ;
        }else if(end.before(start)){
            isValidated = false;
            error = R.string.end_date_before_start;
        }

        if(!isValidated)
            new DialogBox(getActivity(), getString(error), (DialogBoxListener)null) ;

        return isValidated;
    }


    @Override
    public void onDateSelected(int textId, int year, int monthOfYear, int dayOfMonth) {
        String date = Utility.parseDateByHiphenYMD(dayOfMonth, monthOfYear + 1, year) ;
        switch (textId){
            case requestCodeStart:
                start = new Date(year,monthOfYear,dayOfMonth) ;
                tvSelectStartDate.setText(date);
                break;

            case requestCodeEnd:
                end = new Date(year,monthOfYear,dayOfMonth) ;
                tvSelectEndDate.setText(date);
                break;

        }

    }
}
