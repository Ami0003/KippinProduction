package com.kippinretail.KippinInvoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.MyAbstractGridActivity;
import com.kippinretail.R;

import java.util.ArrayList;

/**
 * Created by kamaljeet.singh on 11/7/2016.
 */
public class CreatePerformaActivity extends MyAbstractGridActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUI();
     }

    @Override
    public void updateToolbar() {
         generateTitleBackWithLogoutString(getString(R.string.customer_supplier));
    }

    @Override
    public void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.invoice_logo, "CUSTOMER"));
        modalGridElements.add(getElement(R.drawable.invoice_logo, "SUPPLIER"));
        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                switch (position) {
                    case 0:
                        i.setClass(CreatePerformaActivity.this, InvoiceCustomerList.class);
                        i.putExtra("roleId",Customer);
                        startActivity(i);
                        overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
                        break;
                    case 1:
                        i.setClass(CreatePerformaActivity.this, InvoiceCustomerList.class);
                        i.putExtra("roleId",Supplier);
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
