package com.kippinretail.KippinInvoice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.MyAbstractGridActivity;
import com.kippinretail.R;
import com.kippinretail.config.Config;

import java.util.ArrayList;

/**
 * Created by kamaljeet.singh on 11/7/2016.
 */
public class SupplierCustomerListActivity extends MyAbstractGridActivity {
    @SuppressLint("LongLogTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config.SCREEN_OPEN=3;
        Log.e("SupplierCustomerListActivity","SupplierCustomerListActivity");
        updateUI();
        changeLogo();
    }
    @Override
    public void changeLogo() {
        super.changeLogo();
        iv_logo.setBackgroundResource(R.drawable.kippin_invoicing);
    }

    @Override
    public void updateToolbar() {
        BackWithHOME(getString(R.string.customer_supplier), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.finishActivity(SupplierCustomerListActivity.this);
            }
        });
     }

    @Override
    public void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.customer, "CUSTOMER"));
        modalGridElements.add(getElement(R.drawable.supplier, "SUPPLIER"));
        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        Config.SCREEN_OPEN=1;
                        i.setClass(SupplierCustomerListActivity.this, InvoiceCustomerList.class);
                        i.putExtra("roleId",Customer);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 1:
                        Config.SCREEN_OPEN=2;
                        i.setClass(SupplierCustomerListActivity.this, InvoiceCustomerList.class);
                        i.putExtra("roleId",Supplier);
                        i.putExtra("supplier","supplier");
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;

                }

            }
        });
    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

    }
}

