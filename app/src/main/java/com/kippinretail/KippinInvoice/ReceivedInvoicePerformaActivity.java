package com.kippinretail.KippinInvoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.Modal.ModalGridElement;
import com.kippinretail.MyAbstractGridActivity;
import com.kippinretail.R;

import java.util.ArrayList;

/**
 * Created by kamaljeet.singh on 11/7/2016.
 */
public class ReceivedInvoicePerformaActivity extends MyAbstractGridActivity {
    private String errorMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        BackWithHOME(getString(R.string.received_section), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.finishActivity(ReceivedInvoicePerformaActivity.this);

            }
        });
    }

    @Override
    public void updateUI() {
        ArrayList<ModalGridElement> modalGridElements = new ArrayList<>();
        modalGridElements.add(getElement(R.drawable.received_invoice, "RECEIVED INVOICE"));
        modalGridElements.add(getElement(R.drawable.received_quote, "RECEIVED QUOTE"));
        setData(modalGridElements, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        errorMessage = "No Invoice Available.";
                        callNextIntent(Invoice);

                        break;
                    case 1:
                        errorMessage = "No Quote Available.";
                        callNextIntent(Performa);

                        break;

                }

            }
        });
    }

    private void callNextIntent(String roldeID) {

        // if(!getCustomerId().equals("0")) {
            Intent i = new Intent();
            i.setClass(ReceivedInvoicePerformaActivity.this, ReceivedInvoiceActiivty.class);
            i.putExtra("roleId", roldeID);
            startActivity(i);
            overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
//        }else{
//
//            CommonDialog.With(ReceivedInvoicePerformaActivity.this).Show(errorMessage);
//        }
    }

    @Override
    public void handleNotification(boolean IsVoucher, boolean IsTradePoint, boolean IsFriendRequest, boolean IstransferGiftCard, boolean IsNewMerchant, boolean IsNonKippinPhysical, boolean IsNonKippinLoyalty) {

    }
}

