package com.kippin.topbar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kippin.dashboard.ActivityDashboard;
import com.kippin.dashboard.FragmentDashboard;
import com.kippin.kippin.R;
import com.kippin.superviews.listeners.CommonCallbacks;
import com.kippin.topbar.callbacks.TopbarContentCallacks;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBoxListener;

import static com.kippin.kippin.R.id.ivLogout;

/**
 * Created by gaganpreet.singh on 1/27/2016.
 */
public class FragmentTopbar extends Fragment implements CommonCallbacks, View.OnClickListener{

    View view;
    private TextView tvTitle;

    private ImageView ivHome;

    private ImageView ivBack;

    private TopbarContentCallacks callacks;
    DialogBox dialogBox;
    String[] array={};

    private boolean isInvoice ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tobar,container,false);
        initialiseUI();
        setUpListeners();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callacks = (TopbarContentCallacks)getActivity();

        if(callacks!=null){
            callacks.onInitialise(this);
        }
    }

    public void setTvTitle(String title){
        tvTitle.setText(title.toUpperCase());
    }

    public void hideHome(){
        onHideFunction(ivHome);
    }
    public void showHome(){
        ivHome.setVisibility(View.VISIBLE);
        ivHome.setImageBitmap(null);
        ivHome.setImageBitmap(BitmapFactory.decodeResource(getResources() , R.drawable.home));
        isInvoice = false;
    }

    public void showLogout(){
        ivHome.setVisibility(View.VISIBLE);
        ivHome.setImageBitmap(null);
        ivHome.setImageBitmap(BitmapFactory.decodeResource(getResources() , R.drawable.logout_nutton));
        isInvoice = true;
    }

    public void hideBack(){
        onHideFunction(ivBack);
    }

    private void onHideFunction(View v){
        v.setVisibility(View.INVISIBLE);
        v.setOnClickListener(null);
    }


    @Override
    public void initialiseUI() {
        tvTitle =(TextView) view.findViewById(R.id.tvTopbarTitle);
        ivBack =(ImageView) view.findViewById(R.id.ivBack);
        ivHome =(ImageView) view.findViewById(R.id.ivLogout);

    }



    @Override
    public void setUpListeners() {
    ivBack.setOnClickListener(this);
        ivHome.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.ivBack){
            Utility.onBackPress(getActivity());
        }else if(id== ivLogout){

            if(!isInvoice ){
//            dialogBox = new DialogBox(getResources().getString(R.string.are_sure_uwant_logout),array,getActivity());
                ActivityDashboard.activityDashboard.loadDashboard = true;
                Utility.goToDashboard( );
            }else{
                dialogBox = new DialogBox(getResources().getString(R.string.are_sure_uwant_logout),array,getActivity());
            }


        }
        /*switch (v.getId()){
            case R.id.ivBack:
                Utility.onBackPress(getActivity());
                break;
            case R.id.ivLogout:
//                dialogBox = new DialogBox(getResources().getString(R.string.are_sure_uwant_logout),array,getActivity());
                Utility.goToDashboard( );
                break;

        }*/
    }

    public void hide() {

        view.setVisibility(View.GONE);

    }

    public void show() {
        if(view.getVisibility()==View.GONE)
        view.setVisibility(View.VISIBLE);
    }

    public void addClickListener(int id, View.OnClickListener onClickListener) {
        view.findViewById(id ).setOnClickListener(onClickListener);
    }



}
