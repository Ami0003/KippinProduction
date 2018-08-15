package com.kippin.superviews;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kippin.dashboard.ActivityDashboard;
import com.kippin.kippin.R;
import com.kippin.superviews.listeners.CommonCallbacks;
import com.kippin.topbar.FragmentTopbar;
import com.kippin.topbar.callbacks.TopbarContentCallacks;
import com.kippin.utils.AutoLogoutManager;
import com.kippin.utils.Singleton;
import com.kippin.utils.Utility;
import com.kippin.webclient.model.TemplateData;
import com.pack.kippin.Splash;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;


public abstract class SuperFragment extends Fragment implements CommonCallbacks, TopbarContentCallacks {

    public View root;

    static private View dashboard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if(dashboard==null)
        dashboard = ((ActivityDashboard)getActivity()).findViewById(R.id.side_option_dashboard) ;
    }

    protected View.OnClickListener dashboardOpen= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dashboard.performClick() ;
        }
    } ;

    public  void generateActionBar(int i, boolean b, boolean c){
        ((ActivityDashboard)getActivity()).generateActionBar(i, b, c);
    }

    public  void generateActionBarLogout(){
        ((ActivityDashboard)getActivity()).generateActionBarLogoutInvoice();
    }

    public  void setFragmentTopbarClickListener(int id, View.OnClickListener onClickListener ){
        ((ActivityDashboard)getActivity()).setFragmentTopbarClickListener(id, onClickListener);
    }


    public String getText(TextView textView) {
        return textView.getText().toString();
    }

    public String getText(EditText textView) {
        return textView.getText().toString();
    }


    protected String returnName(Class aClass){
        return aClass.getName();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void initialiseUI(View view) {
        ButterKnife.bind(this, view);
    }



    public void initialiseUI( ) {
        ButterKnife.bind(this, root);
    }


    public void setUpListeners(View.OnClickListener listener, View... view) {
        for (int i = 0; i < view.length; i++) {
            if (view[i] != null) view[i].setOnClickListener(listener);

        }
    }

    @Override
    public void onInitialise(FragmentTopbar fragmentTopbar) {
        this.fragmentTopbar = fragmentTopbar;

    }

    FragmentTopbar fragmentTopbar;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_CANCELED) return;
    }


}

