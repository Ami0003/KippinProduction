package com.kippinretail;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.kippinretail.Adapter.MyAbstractGridAdapter;
import com.kippinretail.ApplicationuUlity.CommonData;
import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.Modal.webclient.Utility;
import com.kippinretail.callbacks.NotificationREveiver;
import com.kippinretail.config.Config;
import com.kippinretail.config.Utils;
import com.kippinretail.facebook.InviteFriends;

import java.util.ArrayList;

import notification.NotificationHandler;

/**
 * Created by gaganpreet.singh on 7/15/2016.
 */
abstract public class MyAbstractGridActivity extends SuperActivity implements NotificationREveiver {


    public GridView gridView;
    ArrayList<ModalGridElement> modalGridElements;
    AdapterView.OnItemClickListener onItemClickListener;
    public static ImageView iv_logo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config.SCREEN_OPEN=3;
        setContentView(R.layout.abstract_parent);
        initUI();
        iv_logo=(ImageView) findViewById(R.id.iv_logo);

    }
    public void changeLogo(){
        iv_logo.setBackgroundResource(R.drawable.kippin_invoicing_logo);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int i = 0;
        Utils.hideKeyboard(this);
        if(CommonUtility.UserType.equals("1")) {
            NotificationHandler.getInstance().getNotificationForCards(MyAbstractGridActivity.this, this);
        }

    }

    //
    protected void setData(ArrayList<ModalGridElement> modalGridElements , AdapterView.OnItemClickListener onItemClickListener){
        this.modalGridElements = modalGridElements;
        this.onItemClickListener = onItemClickListener;
        loadGrid();
        updateToolbar();
    }


    public void initUI() {
        gridView = (GridView) findViewById(R.id.gridView);
    }

        public abstract void updateToolbar();
        public abstract void updateUI();
    private void loadGrid() {
        MyAbstractGridAdapter adapter = new MyAbstractGridAdapter(this, modalGridElements,gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(onItemClickListener);
    }


    protected ModalGridElement getElement(int id, String title) {
        ModalGridElement modalGridElement = new ModalGridElement() ;
        modalGridElement.res = id;
        modalGridElement.string = title;
        return modalGridElement;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        CommonUtility.refreshGridView(this, gridView);
    }
//    private void inviteFacebookFriend() {
//        InviteFriends friends = new InviteFriends(this);
//        friends.sendInvitation();
//
//    }
//
//    private void email() {
//        ArrayList arrayList = new ArrayList();
//        arrayList.add(CommonData.referPath);
//        Utility.email(MyAbstractGridActivity.this, "", "", "", Utility.getBody(), arrayList);
//    }


}
